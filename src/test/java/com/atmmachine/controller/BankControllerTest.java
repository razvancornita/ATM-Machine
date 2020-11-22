package com.atmmachine.controller;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Currency;
import com.atmmachine.model.request.ChangePinRequest;
import com.atmmachine.service.AccountService;
import com.atmmachine.service.BankService;
import com.atmmachine.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final int TEST_CARD_ID = 4657321;
    private static final int CARD_SERVICE_ID = 0;
    private static final ChangePinRequest changePinRequest = new ChangePinRequest("3333", "4444");

    @Autowired
    private MockMvc mockMvc;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @MockBean
    private BankService bankService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CardService cardService;

    @Test
    void testGetBalanceHappyFlow() throws Exception {
        doNothing().when(cardService).checkIfCardIsAuthenticated();
        when(cardService.getCardId()).thenReturn(TEST_CARD_ID);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0.00);
        bankAccount.setCurrency(Currency.RON);

        when(accountService.getAccountByCardId(eq(TEST_CARD_ID))).thenReturn(bankAccount);

        this.mockMvc
                .perform(get("/atmOperation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(bankAccount.getBalance() + " "
                        + bankAccount.getCurrency() + " left")
                );
    }

    @Test
    void testGetBalanceReturnsUnauthorized() throws Exception {
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED))
                .when(cardService).checkIfCardIsAuthenticated();

        this.mockMvc
                .perform(get("/atmOperation"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(BankConstants.CARD_NOT_INSERTED));
    }

    @Test
    void testDeauthenticateHappyFlow() throws Exception {
        doNothing().when(cardService).checkIfCardIsAuthenticated();
        doNothing().when(cardService).deauthenticate();

        this.mockMvc
                .perform(delete("/atmOperation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(BankConstants.DEAUTHENTICATED));
    }

    @Test
    void testDeauthenticateUnauthorized() throws Exception {
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED))
                .when(cardService).checkIfCardIsAuthenticated();

        this.mockMvc
                .perform(delete("/atmOperation"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(BankConstants.CARD_NOT_INSERTED));
    }

    @Test
    void testChangePinHappyFlow() throws Exception {
        doNothing().when(cardService).checkIfCardIsAuthenticated();
        doNothing().when(cardService).changePin(changePinRequest, CARD_SERVICE_ID);
        doNothing().when(cardService).deauthenticate();

        String changePinJSON = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(BankConstants.PIN_CHANGED_SUCCESS));
    }

    @Test
    void testChangePinWrongPin() throws Exception {
        doNothing().when(cardService).checkIfCardIsAuthenticated();
        doThrow(new IllegalArgumentException(BankConstants.WRONG_PIN))
                .when(cardService).changePin(changePinRequest, CARD_SERVICE_ID);

        String changePinJSON = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(BankConstants.WRONG_PIN));
    }

    @Test
    void testChangePinUnauthorized() throws Exception {
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED))
                .when(cardService).checkIfCardIsAuthenticated();

        String changePinJSON = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(BankConstants.CARD_NOT_INSERTED));
    }
}