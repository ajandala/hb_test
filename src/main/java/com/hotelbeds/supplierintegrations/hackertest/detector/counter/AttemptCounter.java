package com.hotelbeds.supplierintegrations.hackertest.detector.counter;

import com.hotelbeds.supplierintegrations.hackertest.detector.LogEntry;

public interface AttemptCounter {

    boolean hasTooManyFailedAttempts(LogEntry logEntry);

    void init();
}
