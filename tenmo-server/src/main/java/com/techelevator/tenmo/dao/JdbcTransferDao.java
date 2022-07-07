package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean createNewTransfer(int transfer_type_id, int transfer_status_id, int account_from,
                                     int account_to, BigDecimal transferAmount)
    {
        String sql = "INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?) RETURNING transfer_id;";

        try {
            jdbcTemplate.update(sql, Integer.class, transfer_type_id,
                    transfer_status_id, account_from, account_to, transferAmount);
        } catch (DataAccessException e) {
        return false;
        }

        return true;
    }

    @Override
    public List<Transfer> getTransferByUser(int userId)
    {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM tenmo_transfer " +
                "WHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId,userId);
        while (results.next())
        {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByTransferId(int transferId)
    {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM tenmo_transfer " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,transferId);
        while (results.next())
        {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet)
    {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }
}
