package com.zero.triptalk.planner.service;

import com.zero.triptalk.exception.custom.PlannerDetailException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.image.service.ImageService;
import com.zero.triptalk.planner.dto.response.PlannerDetailListResponse;
import com.zero.triptalk.planner.dto.response.PlannerDetailResponse;
import com.zero.triptalk.planner.dto.response.PlannerDetailSearchResponse;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.CustomPlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.NOT_FOUND_PLANNER_DETAIL;
import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final CustomPlannerDetailRepository customPlannerDetailRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;


    public List<PlannerDetailListResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailListResponse.of(detailList);
    }

    public PlannerDetailResponse getPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId).orElseThrow(
                () -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL)
        );
        return PlannerDetailResponse.from(plannerDetail);
    }

    //userService 에서 합칠예정
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND));
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        return imageService.uploadFiles(files);
    }

    public void savePlannerDetail(PlannerDetail plannerDetail) {
        plannerDetailRepository.save(plannerDetail);
    }

    public void savePlannerDetailList(List<PlannerDetail> plannerDetailList) {
        List<PlannerDetail> plannerDetails = plannerDetailRepository.saveAll(plannerDetailList);
    }

    public PlannerDetail findById(Long plannerDetailId) {
        return plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL));
    }

    public void deletePlannerDetail(Long id) {
        plannerDetailRepository.deleteById(id);
        plannerDetailSearchRepository.deleteById(id);
    }

    public void NotInDbDeletePlannerDetail(List<Long> updateIds, Long plannerId){
        customPlannerDetailRepository.deletePlannerDetail(updateIds,plannerId);
    }

    public List<PlannerDetail> findByPlannerId(Long plannerId) {
        List<PlannerDetail> byPlanner = plannerDetailRepository.findByPlanner_PlannerId(plannerId);
        if (byPlanner.isEmpty()) {
            throw new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL);
        }
        return byPlanner;
    }

    /**
     * 범위 검색 결과 from MariaDB
     */
    public List<PlannerDetailSearchResponse> getPlannerDetailsByDistance(double longitude, double latitude) {

        List<Object[]> results = plannerDetailRepository.findTop6PlannerDetailsWithinDistance(longitude, latitude);

        List<PlannerDetailSearchResponse> plannerDetails = new ArrayList<>();

        for (Object[] result : results) {

            PlannerDetailSearchResponse response = PlannerDetailSearchResponse.builder()
                    .plannerDetailId(((Number) result[1]).longValue())
                    .description((String) result[2])
                    .date(((Timestamp) result[3]).toLocalDateTime())
                    .nickname((String) result[4])
                    .profile((String) result[5])
                    .lon((Double) result[6])
                    .lat((Double) result[7])
                    .build();

            plannerDetails.add(response);
        }

        return plannerDetails;
    }
}
