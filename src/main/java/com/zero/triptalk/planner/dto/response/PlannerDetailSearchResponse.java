package com.zero.triptalk.planner.dto.response;

import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerDetailSearchResponse {

    private Long plannerDetailId;
    private String nickname;
    private String profile;
    private String description;
    private List<String> images;
    private String place;
    private Double lat;
    private Double lon;
    private LocalDateTime date;

    @Builder
    public PlannerDetailSearchResponse(Long plannerDetailId, String nickname, String profile, String description, List<String> images, String place, Double lat, Double lon, LocalDateTime date) {
        this.plannerDetailId = plannerDetailId;
        this.nickname = nickname;
        this.profile = profile;
        this.description = description;
        this.images = images;
        this.place = place;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
    }

    public static PlannerDetailSearchResponse ofDocument(PlannerDetailDocument document) {

        return PlannerDetailSearchResponse.builder()
                .plannerDetailId(document.getPlannerDetailId())
                .nickname(document.getUserNickname())
                .profile(document.getProfile())
                .description(document.getDescription())
                .images(document.getImages())
                .place(document.getPlace())
                .lat(document.getPoint().lat())
                .lon(document.getPoint().lon())
                .date(document.getDate())
                .build();
    }
}
