package com.zero.triptalk.planner.dto.response;

import com.querydsl.core.Tuple;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlanner.planner;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerSearchResponse {

    private Long plannerId;
    private String title;
    private String thumbnail;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long views;
    private Long likeCount;

    @Builder
    public PlannerSearchResponse(Long plannerId, String title, String thumbnail, String nickname, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likeCount) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likeCount = likeCount;
    }

    public static PlannerSearchResponse ofDocument(PlannerDocument document) {

        return PlannerSearchResponse.builder()
                .plannerId(document.getPlannerId())
                .title(document.getTitle())
                .thumbnail(document.getThumbnail())
                .nickname(document.getUserNickname())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .views(document.getViews())
                .likeCount(document.getLikes())
                .build();
    }

    public static PlannerSearchResponse ofTuple(Tuple tuple) {

        Planner plannerOfTuple = Objects.requireNonNull(tuple.get(planner));

        return PlannerSearchResponse.builder()
                .plannerId(plannerOfTuple.getPlannerId())
                .title(plannerOfTuple.getTitle())
                .thumbnail(plannerOfTuple.getThumbnail())
                .nickname(tuple.get(userEntity.nickname))
                .startDate(plannerOfTuple.getStartDate())
                .endDate(plannerOfTuple.getEndDate())
                .views(plannerOfTuple.getViews())
                .likeCount(tuple.get(plannerLike.likeCount))
                .build();
    }
}
