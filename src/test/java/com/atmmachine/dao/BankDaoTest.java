package com.atmmachine.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("unit_test")
public class BankDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BankDao bankDao;
}
