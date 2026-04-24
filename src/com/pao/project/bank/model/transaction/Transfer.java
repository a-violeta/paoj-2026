package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Transfer extends Transaction{

    public Transfer(double amount, Account sourceAccount, Account destinationAccount){
        super(amount, sourceAccount, destinationAccount, TransactionType.TRANSFER);
    }

    //could be inherited, could be its own function
    //@Override
    protected void validate() {
        //super.validate();
        if (getSourceAccount() == getDestinationAccount()) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }
}
