package com.tw.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "locker_id")
    private Integer lockerId;

    @Column(name = "has_bag")
    private Boolean hasBag;

    @Column(name = "ticket_no")
    private String ticketNo;

    public Slot() {
    }

    public Slot(Integer lockerId, Boolean hasBag) {
        this.lockerId = lockerId;
        this.hasBag = hasBag;
    }

    public Slot(Integer id, Integer lockerId, Boolean hasBag) {
        this.id = id;
        this.lockerId = lockerId;
        this.hasBag = hasBag;
    }

    public Slot(Integer id, Integer lockerId, Boolean hasBag, String ticketNo) {
        this.id = id;
        this.lockerId = lockerId;
        this.hasBag = hasBag;
        this.ticketNo = ticketNo;
    }

    public Slot(Integer lockerId, Boolean hasBag, String ticketNo) {
        this.lockerId = lockerId;
        this.hasBag = hasBag;
        this.ticketNo = ticketNo;
    }

    public String dispatchTicketNumber() {
        String generateTicketNo = generateTicketNumber();
        setTicketNo(generateTicketNo);
        return getTicketNo();
    }


    public String generateTicketNumber() {
        return RandomNumberGenerator.generate8BitRandomNumber();
    }

    private void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public boolean getHasBag() {
        return hasBag;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLockerId(Integer lockerId) {
        this.lockerId = lockerId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLockerId() {
        return lockerId;
    }

    public void updateOccupiedStatus() {
        this.hasBag = Boolean.FALSE;
    }

    public Integer releaseSlotResource() {
        setTicketNo(null);
        return getId();
    }
}
