package com.running_tracker.service.impl;

import com.running_tracker.exception.RunCalculationException;
import com.running_tracker.service.RunCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@Slf4j
public class RunCalculatorServiceImpl implements RunCalculatorService {

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    @Override
    public double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        Instant start = Instant.now();
        try {
            // Convert latitude and longitude from degrees to radians
            double deltaLatitude = Math.toRadians(endLatitude - startLatitude);
            double deltaLongitude = Math.toRadians(endLongitude - startLongitude);

            // Convert latitude to radians
            startLatitude = Math.toRadians(startLatitude);
            endLatitude = Math.toRadians(endLatitude);

            // Calculate the haversine formula
            double haversineLatitude = haversine(deltaLatitude);
            double haversineLongitude = haversine(deltaLongitude);


            // Calculate the haversine formula
            double haversineFormula = haversineLatitude + Math.cos(startLatitude) * Math.cos(endLatitude) * haversineLongitude;

            // Calculate the central angle
            double centralAngle = 2 * Math.atan2(Math.sqrt(haversineFormula), Math.sqrt(1 - haversineFormula));

            // Calculate the distance in meters
            double distanceInMeters = EARTH_RADIUS_METERS * centralAngle;

            log.info(String.format("Calculated distance in meters %s, for startLat %s, startLong %s, endLat %s, endLong %s",
                    distanceInMeters, startLatitude, startLongitude, endLatitude, endLongitude));

            return round(distanceInMeters);
        } catch (Exception exception) {
            throw new RunCalculationException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Calculate distance logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }


    @Override
    public double calculateAverageSpeed(double distance, LocalDateTime startDatetime, LocalDateTime finishDatetime) {
        Instant start = Instant.now();
        try {
            // Calculate the duration in seconds and hours
            long durationInSeconds = Duration.between(startDatetime, finishDatetime).getSeconds();
            double durationInHours = durationInSeconds / 3600.0;

            // If the duration is 0, return 0
            if (durationInHours == 0) {
                return 0;
            }

            // Calculate speed in kilometers per hour and round it to 2 decimal places
            double averageSpeed = round(distance / (durationInHours * 1000));

            log.info(String.format("calculated average speed [averageSpeed:%s km/h]", averageSpeed));

            return averageSpeed;
        } catch (Exception exception) {
            throw new RunCalculationException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Calculate average speed kilometers per hour logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    private double round(double distanceInMeters) {
        return Math.round(distanceInMeters * 100.0) / 100.0;
    }
}
