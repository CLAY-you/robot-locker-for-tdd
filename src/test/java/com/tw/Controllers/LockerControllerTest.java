package com.tw.Controllers;

import com.tw.LockerStatus;
import com.tw.Services.LockerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LockerControllerTest extends BaseControllerTest{

    @MockBean
    LockerService lockerService;

    /*** Test case: locker 有可用的 slot
     设计：方法 lockerService.getLockerStatus(): LockerStatus {hasAvailableSlot: boolean}
     **Given** lockerController收到请求，并通过lockerService获取locker的status
     **When** 向 /locker发送get请求
     **Then** get /locker response返回Object LockerStatus {hasAvailableSlot: true}
    */
    @Test
    void should_return_hasAvailableSlot_as_true_given_locker_has_available_slot() throws Exception {
        LockerStatus lockerStatus = new LockerStatus(Boolean.TRUE);
        when(lockerService.getLockerStatus()).thenReturn(lockerStatus);
        this.mockMvc.perform(get("/locker")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.hasAvailableSlot").value(true));
    }

    /*** Test case: locker 没有可用的 slot
     **Given** lockerController收到请求，并通过lockerService获取locker的status
     **When** 向 /locker发送get请求
     **Then** get /locker response返回Object LockerStatus {hasAvailableSlot: false}
     */
    @Test
    void should_return_hasAvailableSlot_as_false_given_locker_has_no_available_slot() throws Exception {
        LockerStatus lockerStatus = new LockerStatus(Boolean.FALSE);
        when(lockerService.getLockerStatus()).thenReturn(lockerStatus);
        this.mockMvc.perform(get("/locker")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.hasAvailableSlot").value(false));

    }

    /*** Test case: locker 服务不可用
     **Given** lockerController收到请求，并通过lockerService获取locker的status
     **When** 向 /locker发送get请求
     **Then** get /locker response返回  {status: 500, message: "Service not available"}
     */

    @Test
    void should_return_500_given_locker_service_is_not_available() throws Exception {
        when(lockerService.getLockerStatus()).thenThrow(new RuntimeException());
        this.mockMvc.perform(get("/locker"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Internal Server Error"));
    }
    //TODO: locker 中存在可使用的slot的时候，返回8位随机数字返回 作为ticket number

    @Test
    void should_return_ticket_number_when_get_available_slot() throws Exception {
        String ticketNo = "12345678";
        when(lockerService.getTicketNoBindWithDispatchedSlot()).thenReturn(ticketNo);
        this.mockMvc.perform(get("/slot")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ticketNo));
    }

    //TODO: locker 中不存在可使用的slot的时候，返回提示信息 "储物柜已满，请稍后再试"

    @Test
    void should_return_warning_message_no_available_slot_when_not_find_available_slot() throws Exception {
        String warningMessage = "no available slot can be dispatched, try it later";
        when(lockerService.getTicketNoBindWithDispatchedSlot()).thenReturn(warningMessage);
        this.mockMvc.perform(get("/slot")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(warningMessage));
    }
}
