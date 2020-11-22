package com.atmmachine.service;

import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.ChangePinRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.nio.file.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    private static final String NEW_PIN_INVALID_LENGTH = "12345";
    private static final String NEW_PIN_INVALID_CHARS = "A42@";
    private static final String RECEIVED_OLD_PIN = "4359";
    private static final String EXPECTED_OLD_PIN = "4358";
    private static final String VALID_NEW_PIN = "3498";
    private static final int TEST_CARD_ID = 677323;
    private CardService cardService;

    @Mock
    private BankDao bankDao;

    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        cardService = new CardService(bankDao, accountService);
    }

    @Test
    public void testGetCardThrowsExceptionWhenNoCardIsFound() throws CardNotFoundException {
        doThrow(new CardNotFoundException()).when(bankDao).getCard(eq(TEST_CARD_ID));

        assertThatExceptionOfType(CardNotFoundException.class)
                .isThrownBy(() -> cardService.getCard(TEST_CARD_ID));
    }

    @Test
    public void testChangePinThrowsCardNotFoundExceptionWhenCardDoesNotExist() throws CardNotFoundException {
        cardService.setCardId(TEST_CARD_ID);
        ChangePinRequest changePinRequest = new ChangePinRequest();
        changePinRequest.setNewPin("1234");
        doThrow(new EmptyResultDataAccessException(1)).when(bankDao).getCard(eq(TEST_CARD_ID));

        assertThatExceptionOfType(CardNotFoundException.class)
                .isThrownBy(() -> cardService.changePin(changePinRequest));
    }

    @Test
    public void testChangePinThrowsIllegalArgumentExceptionWhenPinIsInvalid() {
        ChangePinRequest changePinRequest = new ChangePinRequest();
        changePinRequest.setNewPin(NEW_PIN_INVALID_LENGTH);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cardService.changePin(changePinRequest));

        changePinRequest.setNewPin(NEW_PIN_INVALID_CHARS);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cardService.changePin(changePinRequest));
    }

    @Test
    public void testChangePinThrowsIllegalArgumentExceptionWhenOldPinDoesNotMatch()
            throws CardNotFoundException {
        cardService.setCardId(TEST_CARD_ID);
        ChangePinRequest changePinRequest = new ChangePinRequest();

        changePinRequest.setOldPin(RECEIVED_OLD_PIN);
        changePinRequest.setNewPin(VALID_NEW_PIN);

        Card card = new Card();
        card.setId(TEST_CARD_ID);
        card.setPin(EXPECTED_OLD_PIN);

        when(bankDao.getCard(eq(TEST_CARD_ID))).thenReturn(card);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cardService.changePin(changePinRequest));
    }

    @Test
    public void testCheckIfCardIsAuthenticatedThrowsAccessDeniedException() {
        cardService.setCardId(null);
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> cardService.checkIfCardIsNotAuthenticated());
    }

    @Test
    public void testCheckIfCardIsAuthenticated() throws AccessDeniedException {
        cardService.setCardId(TEST_CARD_ID);
        cardService.checkIfCardIsNotAuthenticated();
    }

    @Test
    public void testCheckIfCardIsNotAuthenticatedThrowsAlreadyAuthenticatedException() {
        cardService.setCardId(TEST_CARD_ID);
        assertThatExceptionOfType(AlreadyAuthenticatedException.class).isThrownBy(() -> cardService.checkIfCardIsAuthenticated());
    }

    @Test
    public void testCheckIfCardIsNotAuthenticated() throws AlreadyAuthenticatedException {
        cardService.setCardId(null);
        cardService.checkIfCardIsAuthenticated();
    }

    @Test
    public void testDeauthenticate() {
        cardService.setCardId(TEST_CARD_ID);
        cardService.deauthenticate();
        assertThat(cardService.getCardId()).isNull();
    }

    @Test
    public void testAuthenticate() throws AlreadyAuthenticatedException {
        assertThat(cardService.getCardId()).isNull();
        cardService.authenticate(TEST_CARD_ID);
        assertThat(cardService.getCardId()).isEqualTo(TEST_CARD_ID);
    }
}