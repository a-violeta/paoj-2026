package com.pao.project.bank.model.transaction;

public class Deposit extends Transaction {

    public Deposit(double amount, String accountIban) {
        super(
                amount,
                accountIban,        // sourceAccount
                null,           // destinationAccount
                TransactionType.DEPOSIT
        );
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "➕ Deposit\n" +
                "• ID:          " + getId() + "\n" +
                "• Timestamp:   " + getTimestamp() + "\n" +
                "• Amount:      💰 " + getAmount() + "\n" +
                "• To account:  " + getDestinationIban() + "\n" +
                "----------------------------------------";
    }
}

