package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController
{

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao)
    {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
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

    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public boolean newTransfer(Principal user, @Valid @RequestBody TransferDTO transferDTO)
    {
        int userFromId = userDao.findIdByUsername(user.getName());
        int userToId = userDao.findIdByUsername(transferDTO.getUsername());
        int accountFrom = accountDao.getAccountById(userFromId).getAccountId();
        int accountTo = accountDao.getAccountById(userToId).getAccountId();
        BigDecimal transferAmount = transferDTO.getTransferAmount();

        transferDao.createNewTransfer(2,2,accountFrom,accountTo,transferAmount);
        return true;
    }

    @RequestMapping(path = "/transfers/filter", method = RequestMethod.GET)
    public List<Transfer> getTransferByUser(Principal user)
    {
        int userId = userDao.findIdByUsername(user.getName());

        return transferDao.getTransferByUser(userId);
    }
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByTransferId(@PathVariable("id") int transferId)
    {
        return transferDao.getTransfersByTransferId(transferId);
    }
}
