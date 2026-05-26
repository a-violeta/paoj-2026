package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

public class InternationalTransfer extends Transfer {

    private double fee;

    public InternationalTransfer(double amount, String fromIban, String toIban) {
        super(amount, fromIban, toIban);
        this.fee = amount * 0.02; // 2%
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "🌍 International Transfer\n" +
                "• ID:          " + getId() + "\n" +
                "• Timestamp:   " + getTimestamp() + "\n" +
                "• Amount:      💰 " + getAmount() + "\n" +
                "• From:        " + getSourceIban() + "\n" +
                "• To:          " + getDestinationIban() + "\n" +
                "• Fee:         💸 " + fee + "\n" +
                "----------------------------------------";
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee){
        this.fee = fee;
    }
}
