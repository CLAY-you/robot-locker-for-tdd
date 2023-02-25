package com.tw.Services;

import com.tw.Entities.Locker;
import com.tw.Entities.Slot;
import com.tw.LockerStatus;
import com.tw.Repositories.LockerRepository;
import com.tw.Repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LockerService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private LockerRepository lockerRepository;

    public LockerStatus getLockerStatus() {
        // Assumption: there is one locker
        Locker locker = lockerRepository.findAll().get(0);

        long size = slotRepository.countByHasBagFalseAndLockerId(locker.getId());
        return new LockerStatus(size > 0);
    }

    public String getTicketNoBindWithDispatchedSlot() {
        Locker locker = lockerRepository.findAll().get(0);
        List<Slot> availableSlots = slotRepository.findByHasBagAndLockerId(false, locker.getId());
        if (availableSlots.isEmpty()) return "no available slot can be dispatched, try it later";
        Slot availableSlot = availableSlots.get(0);
        availableSlot.dispatchTicketNumber();
        return slotRepository.save(availableSlot).getTicketNo();
    }

    public String getSlotInfoByTicketNoDispatched(String ticketNo) {
        String warningMessage = "ticket number is not valid";
        Locker locker = lockerRepository.findAll().get(0);
        Optional<Slot> slotOptional = slotRepository.findByHasBagAndTicketNoAndLockerId(Boolean.TRUE, ticketNo, locker.getId());
        return slotOptional.map(slot -> slot.releaseSlotResource().toString()).orElse(warningMessage);
    }
}
