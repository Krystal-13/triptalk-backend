package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPlannerSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PlannerDocument> getAllByUserId(Long userId, Pageable pageable) {

        Criteria criteria = new Criteria("userId").is(userId);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSourceFilter(new FetchSourceFilter(
                        new String[]{"plannerId", "title",
                                        "thumbnail", "views",
                                            "createdAt", "likes"}, null))
                .withSort(Sort.by("createdAt").descending())
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, PlannerDocument.class)
                .stream().map(SearchHit::getContent).collect(Collectors.toList());
    }


    // ElasticsearchConfig 에서 해결안되면 해당 매서드로 불러와보기 - 차선책
    public List<PlannerDocument> getAllByCreatedAtAndModifiedAt(LocalDateTime oneMonthAgo, Pageable pageable) {

        Criteria criteria = Criteria.where("createdAt").and("modifiedAt").greaterThanEqual(oneMonthAgo);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSourceFilter(new FetchSourceFilter(
                        new String[]{"plannerId", "title",
                                "thumbnail", "userId",
                                "userNickname", "startDate",
                                "endDate", "views", "likes", "createdAt"}, null))
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, PlannerDocument.class)
                .stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
