package com.zero.triptalk.search.schedule;

import com.querydsl.core.Tuple;
import com.zero.triptalk.like.entity.PlannerDocument;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.repository.CustomPlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zero.triptalk.planner.entity.QPlanner.planner;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElasticScheduler {

    LocalDateTime now = LocalDateTime.now();
    LocalTime time = LocalTime.of(now.getHour(), 0);
    LocalDateTime from = LocalDateTime.of(now.toLocalDate(), time.minusHours(1));
    LocalDateTime to = LocalDateTime.of(now.toLocalDate(), time.minusSeconds(1));

    private final PlannerSearchRepository plannerSearchRepository;
    private final CustomPlannerDetailRepository customPlannerDetailRepository;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;

    @Scheduled(cron = "${scheduler.elasticsearch}")
    public void savePlannersToElasticSearch() {

        List<Tuple> planners = customPlannerDetailRepository.getPlannerListByLikeAndViewUpdateDt(from, to);
        List<PlannerDocument> plannerDocuments = PlannerDocument.ofTuple(planners);
        plannerSearchRepository.saveAll(plannerDocuments);
        log.info(LocalDateTime.now() + "=====================");
        log.info(from + " 부터 " + to + " 까지 PlannerDocument 저장완료. 총 : " + plannerDocuments.size() + "개");

        List<Long> ids = planners.stream().map(x -> Objects.requireNonNull(
                                           x.get(planner)).getPlannerId()).collect(Collectors.toList());
        savePlannerDetailsToElasticSearch(ids);

    }

    public void savePlannerDetailsToElasticSearch(List<Long> ids) {

        List<Tuple> plannerDetails = customPlannerDetailRepository.getPlannerDetailListByPlannerId(ids);
        List<PlannerDetailDocument> plannerDetailDocuments = PlannerDetailDocument.ofTuple(plannerDetails);
        plannerDetailSearchRepository.saveAll(plannerDetailDocuments);
        log.info(LocalDateTime.now() + "=====================");
        log.info(from + " 부터 " + to + " 까지 PlannerDetailDocument 저장완료. 총 : " + plannerDetailDocuments.size() + "개");

    }

}
