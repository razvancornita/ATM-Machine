package com.atmmachine.dao;

import com.atmmachine.exceptions.CardNotFoundException;
import com.atmmachine.model.BankAccount;
import com.atmmachine.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
@Slf4j
public class BankDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${atmMachine.getAccount}")
    private String getAccountQuery;

    @Value("${atmMachine.getCard}")
    private String getCardQuery;

    @Value("${atmMachine.getAccountId}")
    private String getAccountIdQuery;

    @Value("${atmMachine.changePin}")
    private String changePinQuery;

    public BankAccount getAccount(int bankAccountId) {
        log.debug("getting bank account  for id = {}", bankAccountId);
        try {
            Object[] params = {bankAccountId};
            BankAccount bankAccount = jdbcTemplate.queryForObject(getAccountQuery, params, new BeanPropertyRowMapper<>(BankAccount.class));
            log.debug("got bank account = {} for id = {}", bankAccount, bankAccountId);
            return bankAccount;
        } catch (EmptyResultDataAccessException e) {
            log.error("no account found for id = {}", bankAccountId);
            throw e;
        } catch (Exception e) {
            log.error("error getting bank account for id = {}", bankAccountId, e);
            throw e;
        }

    }

    public Card getCard(int cardId) throws CardNotFoundException {
        log.debug("getting card for cardId = {}", cardId);
        try {
            Object[] params = {cardId};
            Card card = jdbcTemplate.queryForObject(getCardQuery, params, new BeanPropertyRowMapper<>(Card.class));
            log.debug("got card = {} for id = {}", card, cardId);
            return card;
        } catch (EmptyResultDataAccessException e) {
            log.error("no card found for id = {}", cardId);
            throw new CardNotFoundException();
        } catch (Exception e) {
            log.error("error getting card for id = {}", cardId, e);
            throw e;
        }
    }

    public void changeCardPin(int cardId, String oldPin) {
        log.debug("changing pin for cardId = {}", cardId);
        try {
            Object[] params = {oldPin, cardId};
            int[] types = {Types.VARCHAR, Types.INTEGER};
            jdbcTemplate.update(changePinQuery, params, types);
            log.debug("changed pin for cardId = {}", cardId);
        } catch (Exception e) {
            log.error("error changing pin for cardId = {}", cardId, e);
            throw e;
        }
    }

    public void deposit() {

    }

    public void withdraw() {


    }
}