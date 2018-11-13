package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.detector.parser.LogEntryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class HackerDetectorImpl implements  HackerDetector {

    private LogEntryParser logEntryParser;

    private Map<String, FailedLoginAttempts> failedLoginAttemptsForIp = new HashMap<>();

    class FailedLoginAttempts  {

        private int attempts = 1;

        private final LocalDateTime firstAttempt;

        public FailedLoginAttempts(LocalDateTime firstAttempt) {
            this.firstAttempt = firstAttempt;
        }

        public void increaseAttempts() {
            attempts++;
        }

        public int getAttempts() {
            return attempts;
        }

        public LocalDateTime getFirstAttempt() {
            return firstAttempt;
        }
    }

    @Autowired
    public HackerDetectorImpl(LogEntryParser logEntryParser) {
        this.logEntryParser = logEntryParser;
    }

    public String parseLine(String line) {

        LogEntry logEntry = logEntryParser.parse(line);

        if (LogEntry.LoginAction.SIGNIN_SUCCESS.equals(logEntry.getLoginAction())) {
            return null;
        } else {
            FailedLoginAttempts failedLoginAttempts = failedLoginAttemptsForIp.get(logEntry.getIpAddress());

            if (failedLoginAttempts == null) {
                failedLoginAttemptsForIp.put(logEntry.getIpAddress(), new FailedLoginAttempts(logEntry.getDateTime()));
            } else {
                LocalDateTime firstAttempt = failedLoginAttempts.getFirstAttempt();
                LocalDateTime lastAttempt = logEntry.getDateTime();

                if (firstAttempt.isBefore(lastAttempt) && firstAttempt.plusMinutes(5).isAfter(lastAttempt)) {
                    failedLoginAttempts.increaseAttempts();
                } else {
                    failedLoginAttemptsForIp.put(logEntry.getIpAddress(), new FailedLoginAttempts(lastAttempt));
                }


                if (failedLoginAttempts.getAttempts() >= 5) {
                    return logEntry.getIpAddress();
                }
            }

        }



        return null;
    }






}
