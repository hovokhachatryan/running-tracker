package com.running_tracker.service;

import com.running_tracker.service.impl.RunCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunCalculatorServiceImplTest {

    private RunCalculatorService runCalculatorService;

    @BeforeEach
    void setUp() {
        runCalculatorService = new RunCalculatorServiceImpl();
    }

    @Test
    void calculateDistanceTest() {
        // Coordinates for popular landmarks: Eiffel Tower (Paris) and Statue of Liberty (New York)
        double eiffelTowerLatitude = 48.8584;
        double eiffelTowerLongitude = 2.2945;
        double statueOfLibertyLatitude = 40.6892;
        double statueOfLibertyLongitude = -74.0445;

        // Expected distance between Eiffel Tower and Statue of Liberty in kilometers (approximate value)
        double expectedDistanceMeters = 5837415.83; // 5837.42 km;

        double calculatedDistance = runCalculatorService.calculateDistance(
                eiffelTowerLatitude, eiffelTowerLongitude,
                statueOfLibertyLatitude, statueOfLibertyLongitude
        );

        // Assert the calculated distance is approximately equal to the expected distance (with small tolerance for rounding)
        assertEquals(expectedDistanceMeters, calculatedDistance);
    }

    @Test
    void calculateDistanceSameCoordinatesTest() {
        double startLat = 40.7128;
        double startLong = -74.0060;

        double distance = runCalculatorService.calculateDistance(startLat, startLong, startLat, startLong);

        assertEquals(0, distance);
    }

    @Test
    void calculateAverageSpeedTest() {
        LocalDateTime startDatetime = LocalDateTime.now().minusHours(1);
        LocalDateTime finishDatetime = LocalDateTime.now();
        double distance = 5000;

        double averageSpeed = runCalculatorService.calculateAverageSpeed(distance, startDatetime, finishDatetime);

        assertEquals(5.0, averageSpeed);
    }

    @Test
    void calculateAverageSpeedZeroDurationTest() {
        LocalDateTime startDatetime = LocalDateTime.now();
        double distance = 5000;

        double averageSpeed = runCalculatorService.calculateAverageSpeed(distance, startDatetime, startDatetime);

        assertEquals(0, averageSpeed);
    }

}

