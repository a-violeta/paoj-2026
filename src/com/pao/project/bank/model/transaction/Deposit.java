package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Deposit extends Transaction {

    public Deposit(double amount, String accountIban) {
        super(
                amount,
                accountIban,        // sourceAccount
                null,           // destinationAccount
                TransactionType.DEPOSIT
        );
    }
}

