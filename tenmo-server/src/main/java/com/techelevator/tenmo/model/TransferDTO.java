package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferDTO
{
    private String username;


    @JsonProperty("amount")
    private BigDecimal transferAmount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
        public String toString() {
            return "LoginDTO{" +
                    "username='" + username + '\'' +
                    ", transferAmount='" + transferAmount + '\'' +
                    '}';
        }

}
