package com.atmmachine.service;

import com.atmmachine.constants.BankConstants;
import com.atmmachine.dao.BankDao;
import com.atmmachine.exceptions.AlreadyAuthenticatedException;
import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.Card;
import com.atmmachine.model.request.ChangePinRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@Service
@Slf4j

public class CardService {

    @Getter
    private Integer cardId;

    @Autowired
    BankDao bankDao;

    public Card getCard(int cardId) throws CardNotFoundException, SQLException {
        try {
            return bankDao.getCard(cardId);
        } catch (CardNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void changePin(ChangePinRequest request, int cardId) throws SQLException, CardNotFoundException {
        try {
            Card card = bankDao.getCard(cardId);
            if (card.getPin() != request.getOldPin()) {
                log.error("wrong pin inserted");
                throw new IllegalArgumentException(BankConstants.WRONG_PIN);
            } else {
                bankDao.changeCardPin(cardId, request.getNewPin());
            }
        } catch (EmptyResultDataAccessException e) {
            throw new CardNotFoundException();
        } catch (Exception e) {
            throw new SQLException(BankConstants.PIN_CHANGED_FAILED);
        }
    }

    public void checkIfCardIsAuthenticated() throws AccessDeniedException {
        log.debug("Checking if card is authenticated");
        if (cardId == null) {
            log.error("Card is not authenticated");
            throw new AccessDeniedException("Please enter a card!");
        }
        log.debug("Card with id {} is authenticated", cardId);
    }

    public void authenticateCard(int cardId) throws AlreadyAuthenticatedException {
        if(this.cardId != null)
            throw new AlreadyAuthenticatedException();
        this.cardId = cardId;
    }
}
