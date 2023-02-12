package com.tw.Services;

import com.tw.Entities.Locker;
import com.tw.Entities.Slot;
import com.tw.LockerStatus;
import com.tw.Repositories.LockerRepository;
import com.tw.Repositories.SlotRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String getAvailableSlot() {
        Locker locker = lockerRepository.findAll().get(0);

        List<Slot> availableSlots = slotRepository.findByHasBagAndLockerId(false, locker.getId());
        Slot availableSlot = availableSlots.get(0);
        String ticketNumber = availableSlot.dispatchTicketNumber();
        return ticketNumber;
    }
}
