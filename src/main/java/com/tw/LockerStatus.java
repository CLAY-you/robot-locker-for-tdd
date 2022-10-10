package com.tw;

public class LockerStatus {

    private final boolean hasAvailableSlot;

    public boolean isHasAvailableSlot() {
        return hasAvailableSlot;
    }

    public LockerStatus(boolean hasAvailableSlot) {
        this.hasAvailableSlot = hasAvailableSlot;
    }
}
