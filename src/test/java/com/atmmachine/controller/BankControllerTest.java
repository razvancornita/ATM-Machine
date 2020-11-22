package com.atmmachine.controller;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.InsufficientFundsException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Currency;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.ChangePinRequest;
import com.atmmachine.service.AccountService;
import com.atmmachine.service.BankService;
import com.atmmachine.service.CardService;
import com.atmmachine.util.RequestGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = RequestGenerator.generateJsonMediaType();
    private static final int TEST_CARD_ID = 4657321;
    private static final int CARD_SERVICE_ID = 0;

    private static final ChangePinRequest changePinRequest = RequestGenerator.generateChangePinRequest();

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
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED)).when(cardService).checkIfCardIsAuthenticated();

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
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED)).when(cardService).checkIfCardIsAuthenticated();

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

        String changePinJson = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(BankConstants.PIN_CHANGED_SUCCESS));
    }

    @Test
    void testChangePinWrongPin() throws Exception {
        doNothing().when(cardService).checkIfCardIsAuthenticated();
        doThrow(new IllegalArgumentException(BankConstants.WRONG_PIN)).when(cardService).changePin(changePinRequest, CARD_SERVICE_ID);

        String changePinJson = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJson))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(BankConstants.WRONG_PIN));
    }

    @Test
    void testChangePinUnauthorized() throws Exception {
        doThrow(new AccessDeniedException(BankConstants.CARD_NOT_INSERTED)).when(cardService).checkIfCardIsAuthenticated();

        String changePinJson = objectWriter.writeValueAsString(changePinRequest);
        this.mockMvc
                .perform(put("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(changePinJson))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(BankConstants.CARD_NOT_INSERTED));
    }

    @Test
    void testWithdrawInsufficientFunds() throws Exception {
        BankOperationRequest withdrawOperationRequest = RequestGenerator.generateWithdrawRequest(300.00);
        doThrow(new InsufficientFundsException()).when(bankService).handleOperation(withdrawOperationRequest);

        String withdrawJSON = objectWriter.writeValueAsString(withdrawOperationRequest);
        this.mockMvc
                .perform(post("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(withdrawJSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(BankConstants.INSUFFICIENT_FUNDS));
    }

    @Test
    void testDeposit() throws Exception {
        BankOperationRequest withdrawOperationRequest = RequestGenerator.generateDepositRequest();

        String depositJson = objectWriter.writeValueAsString(withdrawOperationRequest);
        this.mockMvc
                .perform(post("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(depositJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testAlreadyAuthenticated() throws Exception {
        BankOperationRequest authenticateRequest = RequestGenerator.generateAuthenticateRequest();
        doThrow(new AlreadyAuthenticatedException(1)).when(bankService).handleOperation(authenticateRequest);

        String authenticateJson = objectWriter.writeValueAsString(authenticateRequest);
        this.mockMvc
                .perform(post("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(authenticateJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(BankConstants.ALREADY_AUTHENTICATED + 1));
    }

    @Test
    void testWithdrawNotDivisibleAmount() throws Exception {
        BankOperationRequest withdrawOperationRequest = RequestGenerator.generateWithdrawRequest(307.00);
        doThrow(new ArithmeticException(BankConstants.MONEY_NOT_DIVISIBLE)).when(bankService).handleOperation(withdrawOperationRequest);

        String withdrawJSON = objectWriter.writeValueAsString(withdrawOperationRequest);
        this.mockMvc
                .perform(post("/atmOperation").contentType(APPLICATION_JSON_UTF8).content(withdrawJSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(BankConstants.MONEY_NOT_DIVISIBLE));
    }
}