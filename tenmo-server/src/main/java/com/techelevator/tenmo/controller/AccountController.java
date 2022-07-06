package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController
{
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao)
    {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal user)
    {
        int id = userDao.findIdByUsername(user.getName());
        return accountDao.getBalanceById(id);
    }
}
