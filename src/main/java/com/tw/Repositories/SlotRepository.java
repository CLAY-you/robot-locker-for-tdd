package com.tw.Repositories;

import com.tw.Entities.Slot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    long countByHasBagFalseAndLockerId(Integer lockerId);

    List<Slot> findByHasBagAndLockerId(Boolean hasBag, Integer lockerId);
}