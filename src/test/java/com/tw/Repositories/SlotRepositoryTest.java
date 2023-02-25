package com.tw.Repositories;

import com.tw.Entities.Locker;
import com.tw.Entities.Slot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class SlotRepositoryTest {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private LockerRepository lockerRepository;

    Locker firstLocker = new Locker(10);
    Locker secondLocker = new Locker(2);

    @BeforeEach
    void initUseCase() {

        List<Locker> lockerList = Arrays.asList(firstLocker, secondLocker);
        lockerRepository.saveAll(lockerList);

        Slot firstSlot = new Slot(firstLocker.getId(), true);
        Slot secondSlot = new Slot(firstLocker.getId(), false);
        Slot thirdSlot = new Slot(secondLocker.getId(), true);
        Slot forthSlot = new Slot(secondLocker.getId(), true);

        List<Slot> slots = Arrays.asList(firstSlot, secondSlot,thirdSlot, forthSlot);
        slotRepository.saveAll(slots);
    }

    @AfterEach
    public void destroyAll(){
        slotRepository.deleteAll();
        lockerRepository.deleteAll();
    }

    /***
     * 设计：新方法 lockerRepository.countByHasBagFalseAndLockerId(): long
     **Given**：数据库中对应的locker有empty slot
     **When**：lockerService 通过lockerRepository.countByHasBagFalseAndLockerId 获取对应locker的empty slot数量
     **Then**： lockerRepository.countByHasBagFalseAndLockerId应该返回empty slot的数量不为0
     */

    @Test
    void should_return_slot_size_is_1_by_lockerId_and_hasBag_is_false_when_there_is_empty_slot() {
        long size = slotRepository.countByHasBagFalseAndLockerId(firstLocker.getId());
        assertThat(size).isEqualTo(1);
    }

    /***
     **Given**：数据库中对应的locker有empty slot
     **When**：lockerService 通过lockerRepository.countByHasBagFalseAndLockerId 获取对应locker的empty slot数量
     **Then**： lockerRepository.countByHasBagFalseAndLockerId应该返回empty slot的数量为0
     */

    @Test
    void should_return_slot_size_is_0_by_lockerId_and_hasBag_is_false_when_there_are_no_empty_slot() {
        long size = slotRepository.countByHasBagFalseAndLockerId(secondLocker.getId());
        assertThat(size).isEqualTo(0);
    }

    /***
     **Given**：数据库没有对应的locker
     **When**：lockerService 通过lockerRepository.countByHasBagFalseAndLockerId 获取对应locker的empty slot数量
     **Then**： lockerRepository.countByHasBagFalseAndLockerId应该返回empty slot的数量
    */

    @Test
    void should_return_slot_size_is_0_by_lockerId_and_hasBag_is_false_when_locker_is_not_exist() {
        long size = slotRepository.countByHasBagFalseAndLockerId(99999);
        assertThat(size).isEqualTo(0);
    }

    @Test
    void should_return_available_slots_when_available_slots_in_locker_exist() {
        List<Slot> availableSlots = slotRepository.findByHasBagAndLockerId(false, firstLocker.getId());

        assertThat(availableSlots.size()).isEqualTo(1);
    }

    @Test
    void should_return_occupied_slot_when_slot_in_locker_have_been_used() {
        Slot slot = new Slot(firstLocker.getId(), Boolean.TRUE, "12345678");
        Slot expectedResult = slotRepository.save(slot);

        Slot occupiedSlot = slotRepository.findByHasBagAndTicketNoAndLockerId(Boolean.TRUE, "12345678", firstLocker.getId()).get();
        assertThat(occupiedSlot).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedResult);
    }
}