package com.tw.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Locker {

    public Locker() {
    }

    public Locker(Integer capacity) {
        this.capacity = capacity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "capacity")
    private Integer capacity;

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
