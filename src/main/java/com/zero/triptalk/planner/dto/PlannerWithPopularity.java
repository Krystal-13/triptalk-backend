package com.zero.triptalk.planner.dto;

import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.Getter;

@Getter
public class PlannerWithPopularity {
    private PlannerDocument planner;
    private double popularity;

    public PlannerWithPopularity(PlannerDocument planner, double popularity) {
        this.planner = planner;
        this.popularity = popularity;
    }
}