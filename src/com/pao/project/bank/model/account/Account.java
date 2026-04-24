package com.pao.project.bank.model.account;

import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.CurrencyConverter;
import com.pao.project.bank.model.User;
import com.pao.project.bank.model.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Account implements Comparable<Account> {

    protected String iban;
    private double balance;
    private User owner;
    protected Currency currency;
    private List<Transaction> transactionHistory;
    protected LocalDateTime createdAt;
    private boolean active = true;

    public Account(User owner, Currency currency) {

        if (owner == null || currency == null){
            throw new IllegalArgumentException("Account cannot have a null owner or currency.");
        }

        this.iban = generateIBAN();
        this.balance = 0.0;
        this.owner = owner;
        this.currency = currency;
        this.transactionHistory = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    private String generateIBAN() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder("RO49BANK");

        for (int i = 0; i < 14; i++) {
            sb.append(r.nextInt(10));
        }

        return sb.toString();
    }

    public void addTransaction(Transaction t) {
        if(t == null) return;

        transactionHistory.add(t);
    }

    // useful for transaction service
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void changeCurrency(Currency newCurrency) {
        if (newCurrency == null) {
            throw new IllegalArgumentException("Currency cannot be null.");
        }
        if (!active){
            System.out.println("⚠️ Account is not active.");
            return;
        }
        if (newCurrency == this.currency) {
            System.out.println("⚠️ Account is already in " + newCurrency);
            return;
        }

        double newBalance = CurrencyConverter.convert(this.balance, this.currency, newCurrency);

        this.balance = newBalance;
        this.currency = newCurrency;
    }

    //----------------------------
    //get, set, toString, equals, hashCode, compareTo
    //----------------------------

    public String getIban() {
        return iban;
    }

    public double getBalance() {
        return balance;
    }

    public User getOwner() {
        return owner;
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean getActive(){
        return this.active;
    }

    public void setActive(boolean newActive){
        this.active = newActive;
    }

    public boolean isActive(){
        return this.active;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "💳 " + getClass().getSimpleName() + "\n" +
                "• IBAN:        " + iban + "\n" +
                "• Owner:       " + owner.getName() + "\n" +
                "• Balance:     💰 " + String.format("%.2f", balance) + " " + currency + "\n" +
                "• Active:      " + (active ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(iban, account.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public int compareTo(Account other) {
        return this.iban.toString().compareTo(other.iban.toString());
    }
}

