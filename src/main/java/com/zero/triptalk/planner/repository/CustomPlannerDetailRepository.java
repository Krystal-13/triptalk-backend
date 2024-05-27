package com.zero.triptalk.planner.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.zero.triptalk.place.entity.QPlace.place;
import static com.zero.triptalk.planner.entity.QPlanner.planner;
import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class CustomPlannerDetailRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * mariaDB <-> elasticsearch 동기화 테스트에 필요한 데이터 조회 3.PlannerDetails (Spring Scheduler 방식)
     */
    public List<Tuple> getPlannerDetailListByModifiedAt() {

        return queryFactory.select(plannerDetail, userEntity.nickname, userEntity.profile, place.roadAddress, place.addressName, place.latitude, place.longitude)
                .from(plannerDetail)
                .leftJoin(plannerDetail.images).fetchJoin()
                .leftJoin(planner).on(plannerDetail.planner.eq(planner)).fetchJoin()
                .leftJoin(userEntity).on(plannerDetail.userId.eq(userEntity.userId)).fetchJoin()
                .leftJoin(place).on(plannerDetail.place.eq(place)).fetchJoin()
                .where(plannerDetail.modifiedAt.after(LocalDateTime.of(1970,1,1,9,0,0, 0)))
                .orderBy(plannerDetail.modifiedAt.asc())
                .fetch();
    }

    public void deletePlannerDetail(List<Long> updateIds, Long plannerId) {

        queryFactory.delete(plannerDetail)
                .where(plannerDetail.planner.plannerId.eq(plannerId))
                .where(plannerDetail.plannerDetailId.notIn(updateIds))
                .execute();

    }

}
