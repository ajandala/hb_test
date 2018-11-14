package com.hotelbeds.supplierintegrations.hackertest.detector.parser;

import com.hotelbeds.supplierintegrations.hackertest.detector.LogEntry;

public interface LogEntryParser {

    LogEntry parse(String line);
}
