package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.detector.counter.AttemptCounter;
import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParser;
import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HackerDetectorImpl implements HackerDetector {

    private LogEntryParser logEntryParser;

    private AttemptCounter attemptCounter;

    @Autowired
    public HackerDetectorImpl(LogEntryParserImpl logEntryParser, AttemptCounter attemptCounter) {
        this.logEntryParser = logEntryParser;
        this.attemptCounter = attemptCounter;
    }

    //TODO properties
    //TODO concurrency
    //TODO memory problem
    //TODO fixing test context
    //TODO java 8 feature???
    public String parseLine(String line) {

        LogEntry logEntry = logEntryParser.parse(line);

        if (attemptCounter.hasTooManyFailedAttempts(logEntry)) {
            return logEntry.getIpAddress();
        } else {
            return null;
        }
    }

    public void init() {
        attemptCounter.init();
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();

        strings.add(null);

        System.out.println(strings.size());
    }

}
