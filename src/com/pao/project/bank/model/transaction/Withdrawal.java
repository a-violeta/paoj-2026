package com.pao.project.bank.model.transaction;

public class Withdrawal extends Transaction{

    public Withdrawal(double amount, String sourceAccountIban){
        super(amount, sourceAccountIban, null, TransactionType.WITHDRAWAL);
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "➖ Withdrawal\n" +
                "• ID:          " + getId() + "\n" +
                "• Timestamp:   " + getTimestamp() + "\n" +
                "• Amount:      💰 " + getAmount() + "\n" +
                "• From account:" + getSourceIban() + "\n" +
                "----------------------------------------";
    }
}
