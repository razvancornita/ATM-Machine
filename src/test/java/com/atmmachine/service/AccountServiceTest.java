package com.atmmachine.service;

import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Currency;
import com.atmmachine.model.request.BankOperationRequest;
import com.atmmachine.util.RequestGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    private static final String NEW_PIN_INVALID_LENGTH = "12345";
    private static final String NEW_PIN_INVALID_CHARS = "A42@";
    private static final String RECEIVED_OLD_PIN = "4359";
    private static final String EXPECTED_OLD_PIN = "4358";
    private static final String VALID_NEW_PIN = "3498";
    private static final int TEST_CARD_ID = 677323;
    private AccountService accountService;

    @Mock
    private BankDao bankDao;

    @Before
    public void setUp() {
        accountService = new AccountService(bankDao);
    }

    @Test
    public void testGetNonExistentAccount() throws CardNotFoundException {
        doThrow(new CardNotFoundException()).when(bankDao).getCard(eq(TEST_CARD_ID));

        assertThatExceptionOfType(CardNotFoundException.class)
                .isThrownBy(() -> accountService.getAccountByCardId(TEST_CARD_ID));
    }

    @Test
    public void testChangingBalanceWithInvalidAmount() {
        BankOperationRequest request = RequestGenerator.generateDepositRequest(195.5);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.RON);
        bankAccount.setBalance(195.5);

        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> accountService.changeAccountBalance(request, bankAccount));
    }


    @Test
    public void testNewAmount() {
        BankOperationRequest request = RequestGenerator.generateDepositRequest(300);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.EUR);
        bankAccount.setBalance(195.5);
    }
}