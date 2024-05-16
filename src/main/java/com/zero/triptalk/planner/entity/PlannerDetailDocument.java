package com.zero.triptalk.planner.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner_detail", writeTypeHint = WriteTypeHint.FALSE)
public class PlannerDetailDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long plannerDetailId;
    private String userNickname;
    private String profile;
    private String place;
    private String addressName;
    @GeoPointField
    private GeoPoint point;
    private String description;
    private List<String> images;
    private LocalDateTime date;

    @Builder
    public PlannerDetailDocument(Long plannerDetailId, String userNickname, String profile, String place, String addressName, GeoPoint point, String description, List<String> images, LocalDateTime date) {
        this.plannerDetailId = plannerDetailId;
        this.userNickname = userNickname;
        this.profile = profile;
        this.place = place;
        this.addressName = addressName;
        this.point = point;
        this.description = description;
        this.images = images;
        this.date = date;
    }
}
