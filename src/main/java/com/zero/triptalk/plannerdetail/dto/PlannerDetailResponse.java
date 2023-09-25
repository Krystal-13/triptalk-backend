package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceResponse;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDetailResponse {

    private LocalDateTime createAt;
    private PlaceResponse placeResponse;
    private String description;
    private List<String> imagesUrl;



    public static PlannerDetailResponse from(PlannerDetailDto dto){
        Place place = dto.getPlace();
        PlaceResponse from = PlaceResponse.from(place);
        return null;
    }

}
