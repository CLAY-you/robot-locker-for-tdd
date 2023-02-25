package com.tw.Repositories;

import com.tw.Entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    long countByHasBagFalseAndLockerId(Integer lockerId);

    List<Slot> findByHasBagAndLockerId(Boolean hasBag, Integer lockerId);

    Optional<Slot> findByHasBagAndTicketNoAndLockerId(Boolean hasBag, String ticketNo, Integer lockerId);
}