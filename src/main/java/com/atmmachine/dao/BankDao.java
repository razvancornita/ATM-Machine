package com.atmmachine.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class BankDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${atmMachine.getBalance}")
    private String getBalanceQuery;

    @Value("${atmMachine.getCurrency}")
    private String getCurrencyQuery;

    @Value("${atmMachine.getAccountId}")
    private String getAccountIdQuery;


    public BigDecimal getAccountBalance(int bankAccountId) {
        log.debug("getting bank account balance for id = {}", bankAccountId);
        try {
            Object[] params = {bankAccountId};
            BigDecimal balance = jdbcTemplate.queryForObject(getBalanceQuery, params, BigDecimal.class);
            log.debug("got bank account balance = {} for id = {}", balance, bankAccountId);
            return balance;
        } catch (Exception e) {
            log.error("error getting bank account balance for id = {}", bankAccountId, e);
            throw e;
        }
    }

    public String getAccountCurrency(int bankAccountId) {
        log.debug("getting bank account currency for id = {}", bankAccountId);
        try {
            Object[] params = {bankAccountId};
            String currency = jdbcTemplate.queryForObject(getCurrencyQuery, params, String.class);
            log.debug("got bank account currency = {} for id = {}", currency, bankAccountId);
            return currency;
        } catch (Exception e) {
            log.error("error getting bank account currency for id = {}", bankAccountId, e);
            throw e;
        }
    }

    public int getAccountIdForCardId(int cardId) {
        log.debug("getting bank account id for cardId = {}", cardId);
        try {
            Object[] params = {cardId};
            Integer bankAccountId = jdbcTemplate.queryForObject(getAccountIdQuery, params, Integer.class);
            log.debug("got bank account id = {} for cardId = {}", bankAccountId, cardId);
            return bankAccountId;
        } catch(EmptyResultDataAccessException e) {
            log.error("no bank account found for cardId = {}", cardId, e);
            throw e;
        }catch (Exception e) {
            log.error("error getting bank account id for cardId = {}", cardId, e);
            throw e;
        }
    }
}