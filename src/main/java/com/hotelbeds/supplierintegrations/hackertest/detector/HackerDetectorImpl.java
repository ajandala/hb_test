package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.detector.counter.AttemptCounter;
import com.hotelbeds.supplierintegrations.hackertest.detector.model.LogEntry;
import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParser;
import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HackerDetectorImpl implements HackerDetector {

    private LogEntryParser logEntryParser;

    private AttemptCounter attemptCounter;

    @Autowired
    public HackerDetectorImpl(LogEntryParserImpl logEntryParser, AttemptCounter attemptCounter) {
        this.logEntryParser = logEntryParser;
        this.attemptCounter = attemptCounter;
    }

    public String parseLine(String line) {

        LogEntry logEntry = logEntryParser.parse(line);

        if (attemptCounter.hasTooManyFailedAttempts(logEntry)) {
            return logEntry.getIpAddress();
        } else {
            return null;
        }
    }

    void init() {
        attemptCounter.init();
    }

    int getAttemptCounterChacheSize() {
        return attemptCounter.getAttemptCacheSize();
    }

}
