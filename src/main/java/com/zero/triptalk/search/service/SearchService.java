package com.zero.triptalk.search.service;

import com.zero.triptalk.component.RedisUtil;
import com.zero.triptalk.exception.code.SearchErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.SearchException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.planner.dto.response.PlannerDetailSearchResponse;
import com.zero.triptalk.planner.dto.response.PlannerSearchResponse;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.CustomPlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.CustomPlannerSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.planner.dto.PlannerWithPopularity;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.repository.UserSearchRepository;
import com.zero.triptalk.user.response.UserInfoSearchResponse;
import com.zero.triptalk.user.response.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final PlannerSearchRepository plannerSearchRepository;
    private final CustomPlannerSearchRepository customPlannerSearchRepository;
    private final CustomPlannerDetailSearchRepository customPlannerDetailSearchRepository;
    private final UserSearchRepository userSearchRepository;
    private final RedisUtil redisUtil;

    private static final String CACHE_KEY = "planner_top6";

    /**
     * Top6 planner from redis
     */
    public List<PlannerSearchResponse> getTop6Planners() throws NoSuchIndexException {

        List<PlannerDocument> plannerTop6 = redisUtil.getPlannerTop6(CACHE_KEY);

        if (plannerTop6 == null || plannerTop6.isEmpty()) {
            List<PlannerDocument> plannerTop6FromES = this.fetchPlannerTop6FromES();
            redisUtil.setPlannerTop6(CACHE_KEY, plannerTop6FromES);
            return plannerTop6FromES.stream().map(PlannerSearchResponse::ofDocument).collect(Collectors.toList());
        }

        return plannerTop6.stream().map(PlannerSearchResponse::ofDocument).collect(Collectors.toList());
    }

    /**
     * Top6 planner from elasticsearch
     */
    public List<PlannerSearchResponse> getTop6PlannersFromES() {

        List<PlannerDocument> plannerTop6 = plannerSearchRepository.findTop6ByOrderByLikesDesc();

        return plannerTop6.stream().map(PlannerSearchResponse::ofDocument).collect(Collectors.toList());
    }


    public List<PlannerDetailSearchResponse> searchByRegionAnySort(
                                        String region, String searchType, Pageable pageable)
                                                                throws NoSuchIndexException {

        if (region.isEmpty() || region.trim().equals("")) {
            throw new SearchException(SearchErrorCode.INVALID_REQUEST);
        }

        List<PlannerDetailDocument> searchResponses = customPlannerDetailSearchRepository
                                .searchByRegionAndSearchType(region, searchType, pageable);

        return searchResponses.stream().map(PlannerDetailSearchResponse::ofDocument)
                                                            .collect(Collectors.toList());

    }

    public List<UserSearchResponse> getUserNicknameList(String keyword)
                                                                throws NoSuchIndexException {

        List<UserDocument> documents = userSearchRepository.findByNicknameContains(keyword);

        return UserSearchResponse.ofDocument(documents);
    }

    public UserInfoSearchResponse searchByUserId(Long userId,Pageable pageable)
                                                                throws NoSuchIndexException {

        UserDocument userDocument = userSearchRepository.findById(userId).orElseThrow(() ->
                                                new UserException(UserErrorCode.USER_NOT_FOUND));

        List<PlannerDocument> plannerDocuments = customPlannerSearchRepository
                                                    .getAllByUserId(userId, pageable);

        return UserInfoSearchResponse.ofDocument(userDocument, plannerDocuments);
    }

    public List<PlannerDetailSearchResponse> searchByGeoPointBox(
                                GeoPoint topLeft, GeoPoint bottomRight, Pageable pageable) {

        List<PlannerDetailDocument> detailDocuments = customPlannerDetailSearchRepository
                                        .searchByGeoPointBox(topLeft, bottomRight, pageable);

        return detailDocuments.stream().map(PlannerDetailSearchResponse::ofDocument)
                                        .collect(Collectors.toList());
    }

    public List<PlannerDetailSearchResponse> searchByGeoPointDistance(
                                    Double x, Double y, String distance, Pageable pageable) {

        GeoPoint geoPoint = new GeoPoint(y, x);
        List<PlannerDetailDocument> detailDocuments = customPlannerDetailSearchRepository
                                        .searchByGeoPointDistance(geoPoint, distance, pageable);

        return detailDocuments.stream().map(PlannerDetailSearchResponse::ofDocument)
                                        .collect(Collectors.toList());

    }

    /**
     * 최근 한달동안 업데이트된 planner 가져오기
     */
    public List<PlannerDocument> fetchPlannerTop6FromES() {

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Pageable pageable = PageRequest.of(0, 50);
        List<PlannerDocument> planners = customPlannerSearchRepository.getAllByCreatedAtAndModifiedAt(oneMonthAgo, pageable);

        PriorityQueue<PlannerWithPopularity> plannerTop6Queue = new PriorityQueue<>(Comparator.comparingDouble(PlannerWithPopularity::getPopularity));

        for(PlannerDocument planner : planners) {
            double popularity = calculatePopularity(planner);
            PlannerWithPopularity plannerWithPopularity = new PlannerWithPopularity(planner, popularity);

            if (plannerTop6Queue.size() < 6) {
                plannerTop6Queue.add(plannerWithPopularity);
            } else if (plannerTop6Queue.peek().getPopularity() < popularity) {
                plannerTop6Queue.poll();
                plannerTop6Queue.add(plannerWithPopularity);
            }
        }

        return plannerTop6Queue.stream()
                .sorted(Comparator.comparingDouble(PlannerWithPopularity::getPopularity).reversed())
                .map(PlannerWithPopularity::getPlanner)
                .collect(Collectors.toList());
    }

    private double calculatePopularity(PlannerDocument planner) {

        long hoursSinceCreated = ChronoUnit.HOURS.between(planner.getCreatedAt(), LocalDateTime.now());
        double score = planner.getViews() + (planner.getLikes() * 2); // 조회수와 좋아요 수에 가중치 부여
        return score / Math.pow((hoursSinceCreated + 2), 1.8); // Hacker News 알고리즘 기반의 가중치 적용
    }
}
