package com.pao.project.bank.model.account;

import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.User;

import java.time.LocalDate;

public class LoanAccount extends Account {

    private final double loanAmount;
    private double remainingAmount;
    private double interestRate;
    private final LocalDate dueDate;

    // Complete constructor
    public LoanAccount(User owner, Currency currency, double loanAmount, double interestRate, LocalDate dueDate) {
        super(owner, currency);

        if (interestRate <= 0 || interestRate >= 0.2){
            this.interestRate = 0.05;
        }
        else {
            this.interestRate = interestRate;
        }

        if (loanAmount <= 100 || loanAmount >= 1000000){
            this.loanAmount = 5000.0;
            this.remainingAmount = 5000.0;
        }
        else{
            this.loanAmount = loanAmount;
            this.remainingAmount = loanAmount;
        }

        if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Due date must be in the future.");
        }

        this.dueDate = dueDate;
    }

    // Constructor with default values: interest rate 5%, due date after 1 year
    public LoanAccount(User owner, Currency currency, double loanAmount) {
        super(owner, currency);
        this.loanAmount = loanAmount;
        this.remainingAmount = loanAmount;
        this.interestRate = 0.05;
        this.dueDate = LocalDate.now().plusYears(1);
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setInterestRate(double newInterestRate){
        this.interestRate = newInterestRate;
    }

    public void payLoan(double amount) {
        if (amount > 0) {
            remainingAmount -= amount;
            if (remainingAmount < 0) {
                remainingAmount = 0;
            }
        }
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "💳 LoanAccount\n" +
                "• IBAN:        " + getIban() + "\n" +
                "• Owner:       " + getOwner().getName() + "\n" +
                "• LoanAmount:  " + loanAmount  + " " + getCurrency() + "\n"+
                "• Interest:    " + interestRate + "\n" +
                "• DueDate:     " + dueDate + "\n" +
                "• Active:      " + (isActive() ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }

}
