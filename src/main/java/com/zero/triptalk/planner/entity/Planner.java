package com.zero.triptalk.planner.entity;

import com.zero.triptalk.planner.dto.PlannerRequest;
import com.zero.triptalk.planner.type.PlannerStatus;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long plannerId;

    private String title;

    private String description;

    private String thumbnail;

    @Builder.Default
    private Long views = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private PlannerStatus plannerStatus;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime startDate;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;

    public void changeThumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }

    public void updatePlanner(PlannerRequest request){
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

}
