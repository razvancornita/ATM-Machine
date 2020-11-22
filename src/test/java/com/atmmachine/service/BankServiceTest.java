package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.exceptions.InsufficientFundsException;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.AuthenticateRequest;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.model.request.DepositOrWithdrawRequest;
import com.atmmachine.model.request.OperationType;
import com.atmmachine.util.RequestGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankServiceTest {
    private static final int TEST_CARD_ID = 1;
    private BankService bankService;

    @Mock
    private BankDao bankDao;

    @Mock
    private CardService cardService;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        bankService = new BankService(bankDao, cardService, accountService);
    }

    @Test
    public void testValidRequest() {
        BankOperationRequest authenticateRequest = RequestGenerator.generateAuthenticateRequest();
        BankOperationRequest depositRequest = RequestGenerator.generateDepositRequest(190.5);

        bankService.validateRequest(authenticateRequest);
        bankService.validateRequest(depositRequest);
    }

    @Test
    public void testRequestWithoutOperationType() {
        BankOperationRequest emptyRequest = new BankOperationRequest();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bankService.validateRequest(emptyRequest));
    }

    @Test
    public void testInvalidAuthenticateRequest() {
        BankOperationRequest emptyAuthenticateRequest = new BankOperationRequest(OperationType.AUTHENTICATE, new AuthenticateRequest(), null);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bankService.validateRequest(emptyAuthenticateRequest));
    }

    @Test
    public void testInvalidDepositRequest() {
        BankOperationRequest emptyDepositRequest = new BankOperationRequest(OperationType.DEPOSIT, null, new DepositOrWithdrawRequest());
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> bankService.validateRequest(emptyDepositRequest));
    }

    @Test
    public void testAlreadyAuthenticated() throws AlreadyAuthenticatedException {
        BankOperationRequest request = RequestGenerator.generateAuthenticateRequest();
        doThrow(new AlreadyAuthenticatedException(1)).when(cardService).checkIfCardIsAuthenticated();
        assertThatExceptionOfType(AlreadyAuthenticatedException.class).isThrownBy(() -> bankService.handleOperation(request));
    }

    @Test
    public void testWrongPinAuthentication() throws CardNotFoundException, SQLException {
        BankOperationRequest request = RequestGenerator.generateAuthenticateRequest();
        when(cardService.getCard(TEST_CARD_ID)).thenReturn(new Card(TEST_CARD_ID, 1, "5555"));
        assertThatExceptionOfType(FailedLoginException.class).isThrownBy(() -> bankService.handleOperation(request));
    }

    @Test
    public void testCardNotFoundAuthentication() throws CardNotFoundException, SQLException {
        BankOperationRequest request = RequestGenerator.generateAuthenticateRequest();
        doThrow(new CardNotFoundException()).when(cardService).getCard(TEST_CARD_ID);
        assertThatExceptionOfType(CardNotFoundException.class).isThrownBy(() -> bankService.handleOperation(request));
    }

    @Test
    public void testSuccessfulAuthentication() throws CardNotFoundException, SQLException, InsufficientFundsException,
            AlreadyAuthenticatedException, FailedLoginException, AccessDeniedException {
        BankOperationRequest request = RequestGenerator.generateAuthenticateRequest();
        when(cardService.getCard(TEST_CARD_ID)).thenReturn(new Card(TEST_CARD_ID, 1, "1234"));
        assertEquals(bankService.handleOperation(request), BankConstants.AUTHENTICATED + request.getAuthenticateRequest().getCardId());
    }
}