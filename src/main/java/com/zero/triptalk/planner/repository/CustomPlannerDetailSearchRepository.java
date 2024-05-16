package com.zero.triptalk.planner.repository;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.zero.triptalk.exception.code.SearchErrorCode;
import com.zero.triptalk.exception.custom.SearchException;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.GeoDistanceOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPlannerDetailSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PlannerDetailDocument> searchByRegionAndSearchType(String region, String searchType, Pageable pageable) {

        Criteria criteria = Criteria.where("place")
                .and("placeName")
                .contains(region);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSourceFilter(new FetchSourceFilter(
                        new String[]{"plannerDetailId", "userNickname",
                                "profile", "description",
                                "images", "place",
                                "point", "date"}, null))
                .withSort(Sort.by(searchType).descending())
                .withPageable(pageable)
                .build();

        try {
            return elasticsearchOperations.search(query, PlannerDetailDocument.class)
                    .stream().map(SearchHit::getContent).collect(Collectors.toList());
        } catch (ElasticsearchException e) {
            log.error(getClass() + " 의 Exception -> " + e.getMessage());
            throw new SearchException(SearchErrorCode.RESULT_NOT_FOUND);
        }

    }

    public List<PlannerDetailDocument> searchByGeoPointBox(GeoPoint topLeft, GeoPoint bottomRight, Pageable pageable) {

        Criteria criteria = Criteria.where("point").boundedBy(topLeft, bottomRight);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSourceFilter(new FetchSourceFilter(
                        new String[]{"plannerDetailId", "userNickname",
                                        "profile", "place",
                                            "point", "description",
                                                "images", "date"}, null))
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, PlannerDetailDocument.class)
                                .stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public List<PlannerDetailDocument> searchByGeoPointDistance(GeoPoint point, String distance, Pageable pageable) {

        Criteria criteria = Criteria.where("point").within(point, distance);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSourceFilter(new FetchSourceFilter(
                        new String[]{"plannerDetailId", "userNickname",
                                        "profile", "place",
                                            "point", "description",
                                                "images", "date"}, null))
                .withSort(Sort.by(new GeoDistanceOrder("point", point)))
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, PlannerDetailDocument.class)
                .stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
