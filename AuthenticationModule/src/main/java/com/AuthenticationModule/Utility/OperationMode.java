package com.AuthenticationModule.Utility;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public enum OperationMode {
    RETREAT,
    TURNBACK,
    CHARGE;

    private static OperationMode currentMode = RETREAT;

    public static void setMode(OperationMode mode) {
        currentMode = mode;
    }

    public static OperationMode getMode() {
        return currentMode;
    }

    public static boolean isRetreatMode() {
        return currentMode == RETREAT;
    }

    public static boolean isTurnbackMode() {
        return currentMode == TURNBACK;
    }

    public static boolean isChargeMode() {
        return currentMode == CHARGE;
    }
}

