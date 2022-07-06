package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    public boolean transferAmountsById(int accountFrom, int accountTo) {

        String sql = "SELECT balance " +
                "FROM tenmo_account " +
                "WHERE account_From = ?;";
        BigDecimal results = jdbcTemplate.queryForObject(sql,BigDecimal.class, );


        return results;

    }
*/
}
