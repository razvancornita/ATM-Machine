package com.atmmachine.dao;

import com.atmmachine.model.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class BankDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${atmMachine.getAccount}")
    private String getAccountQuery;

    @Value("${atmMachine.getAccountId}")
    private String getAccountIdQuery;

    public BankAccount getAccount(int bankAccountId) {
        log.debug("getting bank account  for id = {}", bankAccountId);
        try {
            Object[] params = {bankAccountId};
            BankAccount bankAccount = jdbcTemplate.queryForObject(getAccountQuery, params, new BeanPropertyRowMapper<>(BankAccount.class));
            log.debug("got bank account = {} for id = {}", bankAccount, bankAccountId);
            return bankAccount;
        } catch (Exception e) {
            log.error("error getting bank account for id = {}", bankAccountId, e);
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