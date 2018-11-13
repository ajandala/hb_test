package com.hotelbeds.supplierintegrations.hackertest.detector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertNull;

 class HackerDetectorTest {

    private HackerDetector testSubject = new HackerDetectorImpl();

    @Test
     void emptyLine_returnNull() {

        //GIVEN


        //WHEN
        String result = testSubject.parseLine("");

        //THEN
        assertNull(result);
    }

    @Test
     void oneLogEntry_returnNull() {

        //GIVEN


        //WHEN
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith");

        //THEN
        assertNull(result);
    }

    @Test
    void fiveLogEntriesFromSameIpWithinFiveMinutes_returnNull() {

        //GIVEN


        //WHEN
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_SUCCESS,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_SUCCESS,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_SUCCESS,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_SUCCESS,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110700,SIGNIN_SUCCESS,Will.Smith");

        //THEN
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        Assertions.assertEquals("80.238.9.179", result5);
    }
}
