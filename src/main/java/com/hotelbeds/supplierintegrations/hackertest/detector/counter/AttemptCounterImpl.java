package com.hotelbeds.supplierintegrations.hackertest.detector.counter;

import com.hotelbeds.supplierintegrations.hackertest.detector.LogEntry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AttemptCounterImpl implements AttemptCounter {

    private static Map<String, FailedLoginAttempts> failedLoginAttemptsByIp = new HashMap<>();

    @Override
    public boolean hasTooManyFailedAttempts(LogEntry logEntry) {

        synchronized (failedLoginAttemptsByIp) {
            if (LogEntry.LoginAction.SIGNIN_FAILURE.equals(logEntry.getLoginAction())) {

                FailedLoginAttempts failedLoginAttempts = failedLoginAttemptsByIp.get(logEntry.getIpAddress());

                if (failedLoginAttempts == null) {
                    failedLoginAttemptsByIp.put(logEntry.getIpAddress(), new FailedLoginAttempts(logEntry.getDateTime()));
                    System.out.println("------> NEW ATTEMPTS");
                    return false;
                } else {

                    int failedAttempts = failedLoginAttempts.getFailedAttemptsWithinTimeWindow(logEntry.getDateTime(), 5);

                    System.out.println("------> AFTER CALCULATE ATTEMPTS: " + failedAttempts);
                    if (failedAttempts >= 5) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void init() {
        this.failedLoginAttemptsByIp = new HashMap<>();
    }


}
