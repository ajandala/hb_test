package com.hotelbeds.supplierintegrations.hackertest.timecalculator;

import com.hotelbeds.supplierintegrations.timecalculator.TimeCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeCalculatorTest {

    @Test
    void sameTime_returnZero() {

        //GIVEN
        String timeStamp1 = "Thu, 21 Dec 2000 16:01:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:01:00 +0200";

        //WHEN
        long result = TimeCalculator.getDifferenceInMinutes(timeStamp1, timeStamp2);

        //THEN
        assertEquals(0, result);
    }

    @Test
    void oneMinuteDifference_returnOne() {

        //GIVEN
        String timeStamp1 = "Thu, 21 Dec 2000 16:01:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:00 +0200";

        //WHEN
        long result = TimeCalculator.getDifferenceInMinutes(timeStamp1, timeStamp2);

        //THEN
        assertEquals(1, result);
    }

    @Test
    void firstTimestampIsOneMinuteLaterThanSecond_returnOne() {

        //GIVEN
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0200";
        String timeStamp2 = "Thu, 21 Dec 2000 16:01:00 +0200";

        //WHEN
        long result = TimeCalculator.getDifferenceInMinutes(timeStamp1, timeStamp2);

        //THEN
        assertEquals(1, result);
    }

    @Test
    void sameMinuteButOneHourTimeZoneDifference_returnSixty() {

        //GIVEN
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0100";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:00 +0200";

        //WHEN
        long result = TimeCalculator.getDifferenceInMinutes(timeStamp1, timeStamp2);

        //THEN
        assertEquals(60, result);
    }

    @Test
    void fiftyNineSecondsDifference_returnZero() {

        //GIVEN
        String timeStamp1 = "Thu, 21 Dec 2000 16:02:00 +0100";
        String timeStamp2 = "Thu, 21 Dec 2000 16:02:59 +0100";

        //WHEN
        long result = TimeCalculator.getDifferenceInMinutes(timeStamp1, timeStamp2);

        //THEN
        assertEquals(0, result);
    }
}
