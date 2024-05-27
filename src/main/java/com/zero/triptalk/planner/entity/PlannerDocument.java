package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlanner.planner;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner", writeTypeHint = WriteTypeHint.FALSE)
public class PlannerDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long plannerId;
    private String title;
    private String thumbnail;
    private Long userId;
    private String userNickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Field(type = FieldType.Integer)
    private Long views;
    @Field(type = FieldType.Integer)
    private Long likes;
    private LocalDateTime createdAt;

    @Builder
    public PlannerDocument(Long plannerId, String title, String thumbnail, Long userId, String userNickname, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likes, LocalDateTime createdAt) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.userId = userId;
        this.userNickname = userNickname;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public static PlannerDocument ofEntity(Tuple tuple) {

        Planner plannerFromTuple = Objects.requireNonNull(tuple.get(planner));

        return PlannerDocument.builder()
                .plannerId(plannerFromTuple.getPlannerId())
                .title(plannerFromTuple.getTitle())
                .thumbnail(plannerFromTuple.getThumbnail())
                .userId(tuple.get(userEntity.userId))
                .userNickname(tuple.get(userEntity.nickname))
                .startDate(plannerFromTuple.getStartDate())
                .endDate(plannerFromTuple.getEndDate())
                .views(plannerFromTuple.getViews())
                .likes(tuple.get(plannerLike.likeCount))
                .createdAt(plannerFromTuple.getCreatedAt())
                .build();
    }
}
