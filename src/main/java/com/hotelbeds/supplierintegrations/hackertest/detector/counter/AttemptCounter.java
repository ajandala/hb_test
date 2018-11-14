package com.hotelbeds.supplierintegrations.hackertest.detector.counter;

import com.hotelbeds.supplierintegrations.hackertest.detector.model.LogEntry;

public interface AttemptCounter {

    boolean hasTooManyFailedAttempts(LogEntry logEntry);

    void init();

    int getAttemptCacheSize();
}
