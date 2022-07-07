package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao
{
    public Transfer createNewTransfer(int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal amount);

    public List<Transfer> getTransferByUser(int userId);

    public List<Transfer> getTransfersByTransferId(int transferId);
}
