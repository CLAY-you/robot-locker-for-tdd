package com.tw.Controllers;

import com.tw.Entities.Slot;


public class PickFromSlotResponse {
    private Slot slot;
    private String warningMessage;

    public PickFromSlotResponse() {
    }

    public PickFromSlotResponse(Slot slot, String warningMessage) {
        this.slot = slot;
        this.warningMessage = warningMessage;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
