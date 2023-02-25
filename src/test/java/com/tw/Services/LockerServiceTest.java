package com.tw.Services;

import com.tw.Entities.Locker;
import com.tw.Entities.Slot;
import com.tw.LockerStatus;
import com.tw.Repositories.LockerRepository;
import com.tw.Repositories.SlotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
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

    @Captor
    private ArgumentCaptor<Boolean> booleanArgumentCaptor;

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

    //TODO: locker 中存在可使用的slot的时候，匹配到可使用的slot，返回8位随机数字返回 作为ticket number
    @Test
    void should_find_available_slot_and_return_8_bit_random_number_as_ticket_number() {
        List<Slot> availableSlots = new ArrayList<>(List.of(new Slot(1, 1, false)));
        List<Slot> spyAvailableSlots = spy(availableSlots);
        when(slotRepository.findByHasBagAndLockerId(any(Boolean.class), any(Integer.class))).thenReturn(spyAvailableSlots);

        Slot spyAvailableSlot = spy(availableSlots.get(0));
        when(spyAvailableSlots.get(0)).thenReturn(spyAvailableSlot);
        when(spyAvailableSlot.dispatchTicketNumber()).thenReturn("12345678");

        String ticketNo = lockerService.getTicketNoBindWithDispatchedSlot();

        verify(slotRepository, times(1))
                .findByHasBagAndLockerId(booleanArgumentCaptor.capture(), integerArgumentCaptor.capture());
        Boolean hasBag = booleanArgumentCaptor.getValue();
        Integer lockerId = integerArgumentCaptor.getValue();
        assertThat(ticketNo).isEqualTo("12345678");
        assertThat(hasBag).isEqualTo(spyAvailableSlot.getHasBag());
        assertThat(lockerId).isEqualTo(locker.getId());
    }

    //TODO: locker中不存在可以使用的slot的时候，返回提示对应的提示信息
    @Test
    void should_return_warning_message_to_alert_user_there_is_no_available_slot() {
        List<Slot> availableSlots = new ArrayList<>(List.of());
        List<Slot> spyAvailableSlots = spy(availableSlots);
        when(slotRepository.findByHasBagAndLockerId(any(Boolean.class), any(Integer.class))).thenReturn(spyAvailableSlots);

        String warningMessage = lockerService.getTicketNoBindWithDispatchedSlot();

        assertThat(warningMessage).isEqualTo("no available slot can be dispatched, try it later");
    }

    //TODO: locker中存在可使用的slot的时候，遇到internal error，返回对应的提示信息

    @Test
    @Disabled
    void should_return_internal_error_message_when_it_occurred_given_there_is_available_slots() {

    }

    //TODO: should_return_slot_info_which_has_saved_packages_when_received_dispatched_ticket_number

    @Test
    void should_return_occupied_slot_info_which_has_saved_packages_when_received_dispatched_ticket_number() {
        String ticketNo = "12345678";
        Optional<Slot> slotOptional = Optional.of(new Slot(123, 1, true, ticketNo));
        Slot occupiedSlot = new Slot(123, 1, false, null);
        Integer lockerId = locker.getId();

        when(slotRepository.findByHasBagAndTicketNoAndLockerId(eq(Boolean.TRUE), eq(ticketNo), eq(lockerId))).thenReturn(slotOptional);
        Slot slot = slotOptional.get();
        slot.releaseSlotResource();

        Slot actualResult = lockerService.getSlotInfoByTicketNoDispatched(ticketNo);
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(occupiedSlot);
    }

    //TODO: 使用已经使用过的ticket number 取包时，搜索不到对应包的信息，返回不包含任何信息的Slot

    @Test
    void should_return_empty_slot_when_given_used_ticket_number() {
        String ticketNo = "12345678";
        Integer lockerId = locker.getId();
        when(slotRepository.findByHasBagAndTicketNoAndLockerId(eq(Boolean.TRUE), eq(ticketNo), eq(lockerId))).thenReturn(Optional.empty());

        Slot actualResult = lockerService.getSlotInfoByTicketNoDispatched(ticketNo);

        Assertions.assertNull(actualResult);
    }
}
