package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.PUT)
    public boolean transferBalance(Principal user, @PathVariable String username, @PathVariable BigDecimal transferAmount)
    {
        int userFromId = userDao.findIdByUsername(user.getName());
        int userToId = userDao.findIdByUsername(username);
        if(user.getName().equals(username) || transferAmount.compareTo(BigDecimal.valueOf(0)) <= 0 ||
        accountDao.getBalanceById(userFromId).compareTo(transferAmount) < 0)
        {
            return false;
        } else
        {
            accountDao.subtractAccountBalance(userFromId, transferAmount);
            accountDao.addAccountBalance(userToId, transferAmount);
            return true;
        }
    }
}
