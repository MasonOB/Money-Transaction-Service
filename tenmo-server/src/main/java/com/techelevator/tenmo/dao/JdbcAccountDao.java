package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao
{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalanceById(int userId)
    {
        String sql = "SELECT balance " +
                "FROM tenmo_account " +
                "WHERE user_id = ?;";
        BigDecimal results = jdbcTemplate.queryForObject(sql,BigDecimal.class,userId);

            
        return results;

    }

    @Override
    public Account getAccountById(int userId) {
        String sql = "SELECT * FROM tenmo_account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "select * from tenmo_account";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }

        return accounts;
    }

    @Override
    public Account subtractAccountBalance(int id, BigDecimal transferAmount) {
        String sql = "UPDATE tenmo_account " +
                "SET balance = ? " +
                "WHERE user_id = ?;";


       Account account = getAccountById(id);
       BigDecimal newBalance = account.getBalance().subtract(transferAmount);
       account.setBalance(newBalance);
       jdbcTemplate.update(sql,newBalance,id);
        return account;
    }

    @Override
    public Account addAccountBalance(int id, BigDecimal transferAmount)
    {
        String sql = "UPDATE tenmo_account " +
                "SET balance = ? " +
                "WHERE user_id = ?;";

        Account account = getAccountById(id);
        BigDecimal newBalance = account.getBalance().add(transferAmount);
        account.setBalance(newBalance);
        jdbcTemplate.update(sql,newBalance,id);
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rowSet)
    {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }

}
