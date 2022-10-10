package com.tw.Repositories;

import com.tw.Entities.Slot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    long countByHasBagFalseAndLockerId(Integer lockerId);
}