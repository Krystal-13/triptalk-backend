package com.zero.triptalk.planner.entity;

import com.zero.triptalk.base.BaseEntity;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.planner.dto.request.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.request.UpdatePlannerDetailListRequest;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlannerDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerDetailId;

    @JoinColumn(name = "user_id")
    private Long userId;

    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "planner_detail_images", joinColumns = @JoinColumn(name = "planner_detail_id"))
    @Column(name = "image")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id")
    private Planner planner;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @Builder
    public PlannerDetail(
            Long userId, String description, Place place,
            List<String> images, Planner planner, LocalDateTime date) {
        this.planner = planner;
        this.userId = userId;
        this.description = description;
        this.place = place;
        this.images = images;
        this.date = date;
    }

    public void updatePlannerDetail(UpdatePlannerDetailListRequest request,
                                    Planner planner, Place place, Long userId) {
        this.images = request.getImages();
        this.place = place;
        this.planner = planner;
        this.userId = userId;
        this.description = request.getDescription();
    }

    public static PlannerDetail buildPlannerDetail(
            Planner planner, PlannerDetailRequest request,
            UserEntity user, Place place, List<String> images) {
        return PlannerDetail.builder()
                .planner(planner)
                .userId(user.getUserId())
                .description(request.getDescription())
                .place(place)
                .images(images)
                .date(request.getDate())
                .build();
    }

    public static PlannerDetail createNewPlannerDetail(
            UpdatePlannerDetailListRequest request,
            Planner planner, UserEntity user, Place place
    ) {
        return PlannerDetail.builder()
                .planner(planner)
                .userId(user.getUserId())
                .description(request.getDescription())
                .place(place)
                .images(request.getImages())
                .date(request.getDate())
                .build();
    }

}
