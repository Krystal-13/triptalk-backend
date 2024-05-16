package com.zero.triptalk.planner.entity;

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
}
