package com.pao.project.bank.model.account;

import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.User;

public class CheckingAccount extends Account {

    private double overdraftLimit;

    public CheckingAccount() {}

    // Constructor with custom limit
    public CheckingAccount(String ownerId, Currency currency, double overdraftLimit) {
        super(ownerId, currency);

        if (overdraftLimit <= 100.0 || overdraftLimit >= 10000.0){
            this.overdraftLimit = 1000.0;
        }
        else {
            this.overdraftLimit = overdraftLimit;
        }
    }

    // Constructor with default limit 1000
    public CheckingAccount(String ownerId, Currency currency) {
        super(ownerId, currency);
        this.overdraftLimit = 1000.0;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double newOverdraftLimit){
        this.overdraftLimit = newOverdraftLimit;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "💳 CheckingAccount\n" +
                "• IBAN:        " + getIban() + "\n" +
                "• Owner ID:       " + getUserId() + "\n" +
                "• Balance:     💰 " + String.format("%.2f", getBalance()) + " " + getCurrency() + "\n" +
                "• Overdraft:   " + overdraftLimit + "\n" +
                "• Active:      " + (isActive() ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }

}
