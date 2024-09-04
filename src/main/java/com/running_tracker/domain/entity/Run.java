package com.running_tracker.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a run details in the running tracker system.
 */
@Table(name = "runs")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "start_latitude")
    private Double startLatitude;
    @Column(name = "start_longitude")
    private Double startLongitude;
    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;
    @Column(name = "finish_latitude")
    private Double finishLatitude;
    @Column(name = "finish_longitude")
    private Double finishLongitude;
    @Column(name = "finish_datetime")
    private LocalDateTime finishDatetime;
    private Double distance;
    @Column(name = "distance_unit")
    private String distanceUnit;
    @Column(name = "average_speed")
    private Double averageSpeed;
    @Column(name = "average_speed_unit")
    private String averageSpeedUnit;

}
