package com.tw.Services;

import com.tw.Entities.Locker;
import com.tw.LockerStatus;
import com.tw.Repositories.LockerRepository;
import com.tw.Repositories.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LockerServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private LockerRepository lockerRepository;

    @InjectMocks
    LockerService lockerService;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    List<Locker> lockers = new ArrayList<>(List.of(new Locker(1, 2)));
    Locker locker = lockers.get(0);

    @BeforeEach
    void initUseCase() {
        when(lockerRepository.findAll()).thenReturn(lockers);
    }


    /*** Test case: locker 有可用的 slot
     设计：新方法 lockerRepository.countByHasBagFalseAndLockerId(): LockerStatus {hasAvailableSlot: boolean}
     **Given**：lockerService.getLockerStatus，通过lockerId，向lockerRepository获取locker status
     **When**：lockerController 通过lockerService.getLockerStatus 获取locker状态
     **Then**： lockerService.getLockerStatus应该返回Object LockerStatus {hasAvailableSlot: true}
     */

    @Test
    void should_return_hasAvailableSlot_as_true_given_locker_has_available_slot() {
        when(slotRepository.countByHasBagFalseAndLockerId(any(Integer.class))).thenReturn(10L);

        LockerStatus lockerStatus = lockerService.getLockerStatus();

        verify(slotRepository, times(1)).countByHasBagFalseAndLockerId(integerArgumentCaptor.capture());
        Integer id = integerArgumentCaptor.getValue();
        assertThat(id).isEqualTo(locker.getId());
        assertThat(lockerStatus.isHasAvailableSlot()).isEqualTo(true);
    }

    /*** Test case: locker 没有可用的 slot
     **Given**：lockerService.getLockerStatus，通过lockerId，向lockerRepository获取locker status
     **When**：lockerController 通过lockerService.getLockerStatus 获取locker状态
     **Then**： lockerService.getLockerStatus应该返回Object LockerStatus {hasAvailableSlot: false}
     */

    @Test
    void should_return_hasAvailableSlot_as_false_given_locker_has_no_available_slot() {
        when(slotRepository.countByHasBagFalseAndLockerId(any())).thenReturn(0L);

        LockerStatus lockerStatus = lockerService.getLockerStatus();
        assertThat(lockerStatus.isHasAvailableSlot()).isEqualTo(false);
    }
}