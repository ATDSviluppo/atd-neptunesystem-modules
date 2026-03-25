package com.AuthenticationModule.Configuration;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Getter
@Component
public class LatchManager {

    private CountDownLatch badgeScanLatch;

    public void createBadgeScanLatch() {
        badgeScanLatch = new CountDownLatch(1);
    }

    public void countDownBadgeScanLatch() {
        if (badgeScanLatch != null) {
            badgeScanLatch.countDown();
        }
    }
}
