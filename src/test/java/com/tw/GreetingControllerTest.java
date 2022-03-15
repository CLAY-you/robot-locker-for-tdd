package com.tw;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GreetingControllerTest extends BaseControllerTest {

    @Test
    public void no_param_greeting_should_return_default_message() throws Exception {
        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

    @Test
    void param_greeting_should_return_tailored_message() throws Exception{
        this.mockMvc.perform(get("/greeting").param("name", "TW Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, TW Community!"));
    }
}