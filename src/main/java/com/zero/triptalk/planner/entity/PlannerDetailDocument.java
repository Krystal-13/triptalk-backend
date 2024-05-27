package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
import com.zero.triptalk.place.entity.QPlace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;
import static com.zero.triptalk.user.entity.QUserEntity.userEntity;

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

    public static PlannerDetailDocument ofEntity(Tuple tuple) {

        PlannerDetail plannerDetailFromTuple = Objects.requireNonNull(tuple.get(plannerDetail));

        return PlannerDetailDocument.builder()
                .plannerDetailId(plannerDetailFromTuple.getPlannerDetailId())
                .userNickname(tuple.get(userEntity.nickname))
                .profile(String.valueOf(userEntity.profile))
                .addressName(tuple.get(QPlace.place.addressName))
                .point(new GeoPoint(Objects.requireNonNull(tuple.get(QPlace.place.latitude)), Objects.requireNonNull(tuple.get(QPlace.place.longitude))))
                .description(plannerDetailFromTuple.getDescription())
                .images(plannerDetailFromTuple.getImages())
                .date(plannerDetailFromTuple.getDate())
                .build();
    }
}
