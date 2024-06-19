package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerDetailRepository extends JpaRepository<PlannerDetail, Long> {

    List<PlannerDetail> findByPlanner_PlannerId(Long plannerId);

    @Query(value = "SELECT ST_Distance_Sphere(point(p.longitude ,p.latitude), point(:longitude, :latitude)) AS distance, " +
            "pd.planner_detail_id AS plannerDetailId, pd.description, pd.date, u.nickname, u.profile, p.road_address, p.longitude, p.latitude, GROUP_CONCAT(pdi.image) AS images " +
            "FROM planner_detail pd " +
            "LEFT JOIN user u ON pd.user_id = u.user_id " +
            "LEFT JOIN planner_detail_images pdi ON pd.planner_detail_id = pdi.planner_detail_planner_detail_id " +
            "LEFT JOIN place p ON p.place_id = pd.place_id " +
            "WHERE ST_Distance_Sphere(point(p.longitude ,p.latitude), point(:longitude, :latitude)) <= 3000 " +
            "GROUP BY pd.planner_detail_id, pd.description, pd.date, u.nickname, u.profile, p.road_address, p.longitude, p.latitude " +
            "ORDER BY distance " +
            "LIMIT 6", nativeQuery = true)
    List<Object[]> findTop6PlannerDetailsWithinDistance(@Param("longitude") double longitude, @Param("latitude") double latitude);
}
