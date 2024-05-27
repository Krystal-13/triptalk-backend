package com.zero.triptalk.component;

import com.querydsl.core.Tuple;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.CustomPlannerDetailRepository;
import com.zero.triptalk.planner.repository.CustomPlannerRepository;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElasticSearchSynchronizer {

    private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    private final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final PlannerSearchRepository plannerSearchRepository;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;
    private final CustomPlannerDetailRepository customPlannerDetailRepository;
    private final CustomPlannerRepository customPlannerRepository;

//    @Scheduled(fixedRate = 300000)
    public void syncData() {
        long startTime = System.currentTimeMillis();
        logResourceUsage("Log Resource started");

        syncUsers();
        syncPlanners();
        syncPlannerDetails();

        long endTime = System.currentTimeMillis();
        logResourceUsage("Log Resource ended");
        System.out.println("Data sync duration: " + (endTime - startTime) + " ms");
    }

    private void syncUsers() {
        List<UserEntity> users = userRepository.findAllByUpdateAtAfter(LocalDateTime.of(1970,1,1,9,0,0, 0));
        List<UserDocument> collect = users.stream().map(UserDocument::ofEntity).collect(Collectors.toList());
        userSearchRepository.saveAll(collect);
    }

    private void syncPlanners() {
        List<Tuple> planners = customPlannerRepository.getPlannerByModifiedAt();
        List<PlannerDocument> collect = planners.stream().map(PlannerDocument::ofEntity).collect(Collectors.toList());
        plannerSearchRepository.saveAll(collect);
    }

    private void syncPlannerDetails() {
        List<Tuple> plannerDetails = customPlannerDetailRepository.getPlannerDetailListByModifiedAt();
        List<PlannerDetailDocument> collect = plannerDetails.stream().map(PlannerDetailDocument::ofEntity).collect(Collectors.toList());
        plannerDetailSearchRepository.saveAll(collect);
    }

    private void logResourceUsage(String message) {
        long uptime = runtimeMXBean.getUptime();
        int availableProcessors = osBean.getAvailableProcessors();
        double systemLoadAverage = osBean.getSystemLoadAverage();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.println(message + " at: " + System.currentTimeMillis());
        System.out.println("Uptime: " + uptime + " ms");
        System.out.println("Available processors: " + availableProcessors);
        System.out.println("System load average: " + systemLoadAverage);
        System.out.println("Used memory: " + usedMemory / 1024 / 1024 + " MB");
        System.out.println("Free memory: " + freeMemory / 1024 / 1024 + " MB");
        System.out.println("Total memory: " + totalMemory / 1024 / 1024 + " MB");
    }

}
