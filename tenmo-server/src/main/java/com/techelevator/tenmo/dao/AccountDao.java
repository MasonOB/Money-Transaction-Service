package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao
{

    public BigDecimal getBalanceById(int userId);

    public Account getAccountById(int userId);

    public List<Account> findAll();

    public Account subtractAccountBalance(int id, BigDecimal transferAmount);

    public Account addAccountBalance(int id, BigDecimal transferAmount);
}
