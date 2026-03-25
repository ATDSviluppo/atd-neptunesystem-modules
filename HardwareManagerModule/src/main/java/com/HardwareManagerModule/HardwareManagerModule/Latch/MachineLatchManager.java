package com.HardwareManagerModule.HardwareManagerModule.Latch;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class MachineLatchManager {
    private CountDownLatch sectorLatch;
    private CountDownLatch openLatch;
    private CountDownLatch closeLatch;

    public void createSectorLatch() {
        sectorLatch = new CountDownLatch(1);
    }

    public CountDownLatch getSectorLatch() {
        return sectorLatch;
    }

    public void countDownSectorLatch() {
        if (sectorLatch != null) {
            sectorLatch.countDown();
        }
    }

    public void createOpenLatch() {
        openLatch = new CountDownLatch(1);
    }

    public CountDownLatch getOpenLatch() {
        return openLatch;
    }

    public void countDownOpenLatch() {
        if (openLatch != null) {
            openLatch.countDown();
        }
    }

    public void createCloseLatch() {
        closeLatch = new CountDownLatch(1);
    }

    public CountDownLatch getCloseLatch() {
        return closeLatch;
    }

    public void countDownCloseLatch() {
        if (closeLatch != null) {
            closeLatch.countDown();
        }
    }
}
