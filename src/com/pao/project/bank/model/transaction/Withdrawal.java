package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Withdrawal extends Transaction{

    public Withdrawal(double amount, Account sourceAccount){
        super(amount, sourceAccount, null, TransactionType.WITHDRAWAL);
    }
}
