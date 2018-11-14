package com.hotelbeds.supplierintegrations.hackertest.detector.parser;

import com.hotelbeds.supplierintegrations.hackertest.detector.model.LogEntry;
import com.hotelbeds.supplierintegrations.hackertest.detector.model.LogEntry.LoginAction;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.StringTokenizer;

@Component
public class LogEntryParserImpl implements LogEntryParser {

    public LogEntry parse(String line) {

        StringTokenizer stringTokenizer = validateLine(line);

        String ipAddress = stringTokenizer.nextToken();
        String epochInSeconds = stringTokenizer.nextToken();
        String actionString = stringTokenizer.nextToken();
        String userName = stringTokenizer.nextToken();

        return new LogEntry(ipAddress, parseEpoch(epochInSeconds), parseLoginAction(actionString), userName);
    }

    private StringTokenizer validateLine(String line) {
        if (line == null) {
            throw new LogEntryParserException("Input line is null!");
        }

        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

        if (stringTokenizer.countTokens() != 4) {
            throw new LogEntryParserException("Erroneous line format: " + line);
        }

        return stringTokenizer;
    }

    private LocalDateTime parseEpoch(String epochInSeconds) {

        try {
            long epochInMillis = Long.valueOf(epochInSeconds) * 1000;
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochInMillis), ZoneId.systemDefault());
        } catch (NumberFormatException e) {
            throw new LogEntryParserException("Invalid epoch format: " + epochInSeconds, e);
        }
    }

    private LoginAction parseLoginAction(String actionString) {

        if (LoginAction.SIGNIN_FAILURE.name().equals(actionString)) {
            return LoginAction.SIGNIN_FAILURE;
        } else if (LoginAction.SIGNIN_SUCCESS.name().equals(actionString)) {
            return LoginAction.SIGNIN_SUCCESS;
        } else {
            throw new LogEntryParserException("Invalid login action: " + actionString);
        }
    }
}
