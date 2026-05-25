package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class Transfer extends Transaction{

    public Transfer(double amount, String sourceAccountIban, String destinationAccountIban){
        super(amount, sourceAccountIban, destinationAccountIban, TransactionType.TRANSFER);
    }

    //could be inherited, could be its own function
    //@Override
    protected void validate() {
        //super.validate();
        if (getSourceIban() == getDestinationIban()) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
    }
}
