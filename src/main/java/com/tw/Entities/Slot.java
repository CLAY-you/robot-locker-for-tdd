package com.tw.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Slot {

    public Slot() {
    }

    public Slot(Integer lockerId, Boolean hasBag) {
        this.lockerId = lockerId;
        this.hasBag = hasBag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @JoinColumn(name="locker_id")
    private Integer lockerId;

    @Column(name = "has_bag")
    private Boolean hasBag;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLockerId(Integer lockerId) {
        this.lockerId = lockerId;
    }

    public void setHasBag(Boolean hasBag) {
        this.hasBag = hasBag;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getHasBag() {
        return hasBag;
    }

    public Integer getLockerId() {
        return lockerId;
    }
}
