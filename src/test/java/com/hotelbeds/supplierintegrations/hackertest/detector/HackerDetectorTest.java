package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParserException;
import com.hotelbeds.supplierintegrations.hackertest.spring.configuration.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class HackerDetectorTest {

    private static long currentEpoch = 1542110400;

    @Autowired
    private HackerDetector testSubject;

    @BeforeEach
    void setup() {
        ((HackerDetectorImpl)testSubject).init();
    }

    @Test
    void nullLine_throwsException() {

        assertThrows(LogEntryParserException.class, () -> testSubject.parseLine(null));
    }

    @Test
     void emptyLine_throwsException() {

        assertThrows(LogEntryParserException.class, () -> testSubject.parseLine(""));
    }

    @Test
    void wrongLineLength_throwsException() {

        assertThrows(LogEntryParserException.class, () -> testSubject.parseLine("80.238.9.179,133612947,SIGNIN_FAILURE"));
    }

    @Test
    void invalidEpochFormat_throwsException() {

        assertThrows(LogEntryParserException.class, () -> testSubject.parseLine("80.238.9.179,INVALIDEPOCH,SIGNIN_FAILURE,Will.Smith"));
    }

    @Test
    void invalidActionFormat_throwsException() {

        assertThrows(LogEntryParserException.class, () -> testSubject.parseLine("80.238.9.179,133612947,INVALID_ACTION,Will.Smith"));
    }

    @Test
     void oneFailedLogEntry_returnNull() {

        //WHEN
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result);
    }

    @Test
    void oneSuccessfulLogEntry_returnNull() {

        //WHEN
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith");

        //THEN
        assertNull(result);
    }

    @Test
    void fourFailedLogEntriesFromSameIpWithinFiveMinutes_returnNull() {

        //WHEN
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_SUCCESS,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110699,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinFiveMinutes_returnIp() {

        //WHEN
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_FAILURE,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110699,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertEquals("80.238.9.179", result5);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinMoreThanFiveMinutes_returnNull() {

        //WHEN
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_FAILURE,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.179,1542110700,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    void fiveFailedLogEntriesFromDifferentIpsWithinFiveMinutes_returnNull() {

        //WHEN
        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.179,1542110460,SIGNIN_FAILURE,Will.Smith");
        String result3 = testSubject.parseLine("80.238.9.179,1542110520,SIGNIN_FAILURE,Will.Smith");
        String result4 = testSubject.parseLine("80.238.9.179,1542110580,SIGNIN_SUCCESS,Will.Smith");
        String result5 = testSubject.parseLine("80.238.9.180,1542110699,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);
        assertNull(result5);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinFiveMinutesByTwoThreads_returnIp() throws InterruptedException {

        final List<String> results = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            for (int i = 0;  i < 2; i++){
                results.add(testSubject.parseLine("80.238.9.179," + getNextEpoch() + ",SIGNIN_FAILURE,Will.Smith"));
            }

            countDownLatch.countDown();
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0;  i < 3; i++){
                results.add(testSubject.parseLine("80.238.9.179," + getNextEpoch() + ",SIGNIN_FAILURE,Will.Smith"));
            }

            countDownLatch.countDown();
        });

        t1.start();
        t2.start();

        countDownLatch.await();

        assertEquals("80.238.9.179", results.get(4));

    }

    @Test
    void memoryTest_oldEntriesShouldeBeDropped() {

        String result1 = testSubject.parseLine("80.238.9.179,1542110400,SIGNIN_FAILURE,Will.Smith");
        String result2 = testSubject.parseLine("80.238.9.180,1542110701,SIGNIN_FAILURE,Will.Smith");

        assertNull(result1);
        assertNull(result2);
        assertEquals(1, ((HackerDetectorImpl)testSubject).getAttemptCounterChacheSize() );

    }

    private static String getNextEpoch() {

        return String.valueOf(currentEpoch++);
    }

}
