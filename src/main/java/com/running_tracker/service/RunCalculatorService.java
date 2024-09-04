package com.running_tracker.service;

import java.time.LocalDateTime;

/**
 * Service interface for calculating run-related metrics such as distance and average speed.
 */
public interface RunCalculatorService {

    /**
     * Calculates the distance in meters between two geographical coordinates (latitude and longitude).
     *
     * @param startLatitude the starting point's latitude
     * @param startLongitude the starting point's longitude
     * @param endLatitude the ending point's latitude
     * @param endLongitude the ending point's longitude
     * @return the calculated distance in kilometers
     */
    double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude);

    /**
     * Calculates the average speed in kilometers per hour for a run based on the distance and time duration.
     *
     * @param distance the distance covered in kilometers
     * @param startDatetime the start time of the run
     * @param finishDatetime the finish time of the run
     * @return the average speed in kilometers per hour
     */
    double calculateAverageSpeed(double distance, LocalDateTime startDatetime, LocalDateTime finishDatetime);

}
