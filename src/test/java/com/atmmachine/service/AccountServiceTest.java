package com.atmmachine.service;

import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.exceptions.InsufficientFundsException;
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
import static org.mockito.Mockito.verify;

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
    public void testDepositSameCurrency() throws InsufficientFundsException {
        BankOperationRequest bankOperationRequest = RequestGenerator.generateDepositRequest(300.00);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.EUR);
        bankAccount.setBalance(195.00);
        double newAmount = 495;


        accountService.changeAccountBalance(bankOperationRequest, bankAccount);
        verify(bankDao).changeAccountBalance(eq(bankAccount.getId()), eq(newAmount));
    }

    @Test
    public void testDepositDifferentCurrency() throws InsufficientFundsException {
        BankOperationRequest bankOperationRequest = RequestGenerator.generateDepositRequest(300.00);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.RON);
        bankAccount.setBalance(195.00);
        double newAmount = 1656.00;

        accountService.changeAccountBalance(bankOperationRequest, bankAccount);
        verify(bankDao).changeAccountBalance(eq(bankAccount.getId()), eq(newAmount));
    }

    @Test
    public void testWithdrawSameCurrency() throws InsufficientFundsException {
        BankOperationRequest bankOperationRequest = RequestGenerator.generateWithdrawRequest(100.00);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.EUR);
        bankAccount.setBalance(200.00);
        double newAmount = 100;

        accountService.changeAccountBalance(bankOperationRequest, bankAccount);
        verify(bankDao).changeAccountBalance(eq(bankAccount.getId()), eq(newAmount));
    }

    @Test
    public void testWithdrawDifferentCurrency() throws InsufficientFundsException {
        BankOperationRequest bankOperationRequest = RequestGenerator.generateDepositRequest(100.00);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.RON);
        bankAccount.setBalance(600.00);
        double newAmount = 1087.00;

        accountService.changeAccountBalance(bankOperationRequest, bankAccount);
        verify(bankDao).changeAccountBalance(eq(bankAccount.getId()), eq(newAmount));
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFunds() throws InsufficientFundsException {
        BankOperationRequest bankOperationRequest = RequestGenerator.generateWithdrawRequest(300.00);
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(Currency.EUR);
        bankAccount.setBalance(200.00);
        double newAmount = 100;

        accountService.changeAccountBalance(bankOperationRequest, bankAccount);
        verify(bankDao).changeAccountBalance(eq(bankAccount.getId()), eq(newAmount));
    }
}