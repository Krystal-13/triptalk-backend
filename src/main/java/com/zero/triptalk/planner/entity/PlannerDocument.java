package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

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
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startDate;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime endDate;
    @Field(type = FieldType.Integer)
    private Long views;
    @Field(type = FieldType.Integer)
    private Long likes;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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

    public Long getLikes() {
        return likes == null ? 0 : likes;
    }
}
