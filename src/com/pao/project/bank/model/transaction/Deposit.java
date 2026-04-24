package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Deposit extends Transaction {

    public Deposit(double amount, Account account) {
        super(
                amount,
                account,        // sourceAccount
                null,           // destinationAccount
                TransactionType.DEPOSIT
        );
    }
}

