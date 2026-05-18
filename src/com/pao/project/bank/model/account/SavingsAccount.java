package com.pao.project.bank.model.account;

import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.User;

public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount() {}

    public SavingsAccount(User owner, Currency currency, double interestRate) {
        super(owner, currency);

        if (interestRate <= 0 || interestRate >= 0.2){
            this.interestRate = 0.05;
        }
        else {
            this.interestRate = interestRate;
        }
    }

    // Constructor with default interest rate 2%
    public SavingsAccount(User owner, Currency currency) {
        super(owner, currency);
        this.interestRate = 0.02;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double newInterestRate){
        this.interestRate = newInterestRate;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "💳 SavingsAccount\n" +
                "• IBAN:        " + getIban() + "\n" +
                "• Owner:       " + getOwner().getName() + "\n" +
                "• Balance:     💰 " + String.format("%.2f", getBalance()) + " " + getCurrency() + "\n" +
                "• Interest:    " + interestRate + "\n" +
                "• Active:      " + (isActive() ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }

}
