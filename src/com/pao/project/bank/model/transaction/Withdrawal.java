package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Withdrawal extends Transaction{

    public Withdrawal(double amount, String sourceAccountIban){
        super(amount, sourceAccountIban, null, TransactionType.WITHDRAWAL);
    }
}
