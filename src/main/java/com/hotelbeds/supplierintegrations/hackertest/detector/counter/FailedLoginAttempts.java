package com.hotelbeds.supplierintegrations.hackertest.detector.counter;

import java.time.LocalDateTime;

class FailedLoginAttempts  {

    private int attempts = 1;

    private LocalDateTime firstAttempt;

    public FailedLoginAttempts(LocalDateTime firstAttempt) {
        this.firstAttempt = firstAttempt;
    }


    public int getFailedAttemptsWithinTimeWindow(LocalDateTime lastAttempt, int timeWindowInMinutes) {

        if (firstAttempt.isBefore(lastAttempt) && firstAttempt.plusMinutes(timeWindowInMinutes).isAfter(lastAttempt)) {
            attempts++;
            System.out.println("-----------------> Within range, increase");
        } else {
            System.out.println("-----------------> Out of range, set to 1");
            attempts = 1;
            firstAttempt = lastAttempt;
        }

        return attempts;
    }
}
