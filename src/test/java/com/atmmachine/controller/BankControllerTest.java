package com.atmmachine.controller;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Test
    void testGetBalanceReturnsUnauthorized() throws Exception {
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED))
                .when(cardService).checkIfCardIsAuthenticated();

        this.mockMvc
                .perform(get("/atmOperation"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}