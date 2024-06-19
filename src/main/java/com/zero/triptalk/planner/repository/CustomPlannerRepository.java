package com.zero.triptalk.planner.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.triptalk.planner.dto.response.PlannerListResponse;
import com.zero.triptalk.planner.dto.response.PlannerListResult;
import com.zero.triptalk.planner.type.SortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlanner.planner;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

@Repository
public class CustomPlannerRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPlannerRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public PlannerListResult PlannerList(Pageable pageable, SortType sortType) {

        final ConstructorExpression<PlannerListResponse> plannerListResponse =
                Projections.constructor(PlannerListResponse.class,
                        planner.plannerId,
                        planner.title,
                        planner.thumbnail,
                        plannerLike.likeCount,
                        planner.views,
                        planner.createdAt
                );

        final List<PlannerListResponse> result = queryFactory
                .query()
                .select(plannerListResponse)
                .from(planner)
                .leftJoin(plannerLike)
                .on(planner.eq(plannerLike.planner))
                .offset(pageable.getOffset())
                .groupBy(planner.plannerId)
                .orderBy(ordering(sortType))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return PlannerListResult.builder()
                .plannerListResponses(new SliceImpl<>(result, pageable, hasNext))
                .hasNext(hasNext)
                .build();

    }

    private OrderSpecifier<?> ordering(final SortType sortType) {
        OrderSpecifier<?> orderSpecifier;
        switch (sortType) {
            case LIKES:
                orderSpecifier = plannerLike.likeCount.desc();
                break;
            case RECENT:
                orderSpecifier = planner.createdAt.desc();
                break;
            case VIEWS:
                orderSpecifier = planner.views.desc();
                break;
            default:
                orderSpecifier = planner.plannerId.desc();
        }
        return orderSpecifier;
    }

    /**
     * mariaDB <-> elasticsearch 동기화 테스트에 필요한 데이터 조회 2.Planners (Spring Scheduler 방식)
     */
    public List<Tuple> getPlannerByModifiedAt() {

        return queryFactory.select(planner, userEntity.userId, userEntity.nickname, plannerLike.likeCount)
                .from(planner)
                .leftJoin(userEntity).on(planner.user.eq(userEntity))
                .leftJoin(plannerLike).on(planner.eq(plannerLike.planner))
                .where(planner.modifiedAt.after(LocalDateTime.of(1970,1,1,9,0,0, 0)))
                .orderBy(planner.modifiedAt.asc())
                .fetch();

    }

    public List<Tuple> getPlannerTop6ByLikes() {

        return queryFactory.select(planner, userEntity.nickname, plannerLike.likeCount)
                .from(planner)
                .leftJoin(userEntity).on(planner.user.eq(userEntity))
                .leftJoin(plannerLike).on(planner.eq(plannerLike.planner))
                .orderBy(plannerLike.likeCount.desc())
                .limit(6)
                .fetch();
    }


}