package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParserException;
import com.hotelbeds.supplierintegrations.hackertest.spring.configuration.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class HackerDetectorTest {

    @Autowired
    private HackerDetector testSubject;

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

        //GIVEN


        //WHEN
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith");

        //THEN
        assertNull(result);
    }

    @Test
    void oneSuccessfulLogEntry_returnNull() {

        //GIVEN


        //WHEN
        String result = testSubject.parseLine("80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith");

        //THEN
        assertNull(result);
    }

    @Test
    void fiveFailedLogEntriesFromSameIpWithinFiveMinutes_returnNull() {

        //GIVEN


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
}
