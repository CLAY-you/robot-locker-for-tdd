package com.tw.Controllers;

import com.tw.Entities.Slot;
import com.tw.LockerStatus;
import com.tw.Services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class LockerController {

    @Autowired
    private LockerService lockerService;

    @GetMapping("/locker")
    public LockerStatus locker() {
        try {
            return lockerService.getLockerStatus();
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/slot")
    public String getAvailableSlot() {
        try {
            return lockerService.getTicketNoBindWithDispatchedSlot();
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/slot/{ticketNo}")
    public Slot getSlotInfo(@PathVariable String ticketNo) {
        return lockerService.getSlotInfoByTicketNoDispatched(ticketNo);
    }
}
