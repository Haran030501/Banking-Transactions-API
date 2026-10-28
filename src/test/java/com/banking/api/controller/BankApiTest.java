package com.banking.api.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BankApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAccount_returns201() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolder\":\"Amy\",\"initialBalance\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountHolder").value("Amy"));
    }

    @Test
    void createAccount_withBlankName_returns400() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountHolder\":\"\",\"initialBalance\":100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void getTransactions_forUnknownAccount_returns404() throws Exception {
        mockMvc.perform(get("/accounts/does-not-exist/transactions"))
                .andExpect(status().isNotFound());
    }
}
