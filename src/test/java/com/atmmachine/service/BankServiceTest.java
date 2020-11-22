package com.atmmachine.service;

import com.atmmachine.dao.BankDao;
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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(MockitoJUnitRunner.class)
public class BankServiceTest {

    private static final String NEW_PIN_INVALID_LENGTH = "12345";
    private static final String NEW_PIN_INVALID_CHARS = "A42@";
    private static final String RECEIVED_OLD_PIN = "4359";
    private static final String EXPECTED_OLD_PIN = "4358";
    private static final String VALID_NEW_PIN = "3498";
    private static final int TEST_CARD_ID = 677323;
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


    /*
    private String authenticate(BankOperationRequest request) throws FailedLoginException, CardNotFoundException, SQLException, AlreadyAuthenticatedException {
        AuthenticateRequest authenticateRequest = request.getAuthenticateRequest();
        Card card = cardService.getCard(authenticateRequest.getCardId());
        if (card.getPin().equals(authenticateRequest.getPin())) {
            cardService.authenticate(authenticateRequest.getCardId());
            return BankConstants.AUTHENTICATED + authenticateRequest.getCardId();
        } else {
            throw new FailedLoginException(BankConstants.WRONG_PIN);
        }
    }
     */

    @Test
    public void testValidRequest() {
        BankOperationRequest authenticateRequest = RequestGenerator.generateAuthenticateRequest();
        BankOperationRequest depositRequest = RequestGenerator.generateDepositRequest();

        bankService.validateRequest(authenticateRequest);
        bankService.validateRequest(depositRequest);
    }

    @Test
    public void testRequestWithoutOperationType() {
        BankOperationRequest emptyRequest = new BankOperationRequest();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> bankService.validateRequest(emptyRequest));
    }

    @Test
    public void testInvalidAuthenticateRequest() {
        BankOperationRequest emptyAuthenticateRequest = new BankOperationRequest(OperationType.AUTHENTICATE, new AuthenticateRequest(), null);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> bankService.validateRequest(emptyAuthenticateRequest));
    }

    @Test
    public void testInvalidDepositRequest() {
        BankOperationRequest emptyDepositRequest = new BankOperationRequest(OperationType.DEPOSIT, null, new DepositOrWithdrawRequest());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> bankService.validateRequest(emptyDepositRequest));
    }
}