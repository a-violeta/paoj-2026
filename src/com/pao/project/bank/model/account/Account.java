package com.pao.project.bank.model.account;

import com.pao.project.bank.exception.IllegalCurrencyException;
import com.pao.project.bank.exception.InactiveAccountException;
import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.CurrencyConverter;
import com.pao.project.bank.model.User;
import com.pao.project.bank.model.transaction.Transaction;
import com.pao.project.bank.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Account implements Comparable<Account> {

    protected String iban;
    private double balance;
    //private User owner;
    private String userId;
    protected Currency currency;
    //private List<Transaction> transactionHistory;
    protected LocalDateTime createdAt;
    private boolean active = true;

    public Account() {}

    public Account(String ownerId, Currency currency) {

        if (ownerId == null || ownerId.isBlank() || currency == null){
            throw new IllegalArgumentException("Account cannot have a null owner or currency.");
        }

        this.iban = generateIBAN();
        this.balance = 0.0;
        //this.owner = owner;
        this.currency = currency;
        //this.transactionHistory = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.userId = ownerId;
    }

    private String generateIBAN() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder("RO49BANK");

        for (int i = 0; i < 14; i++) {
            sb.append(r.nextInt(10));
        }

        return sb.toString();
    }

    // useful for transaction service
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
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

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean getActive(){
        return this.active;
    }

    public String getUserId() {
        return userId;
    }

    public void setActive(boolean newActive){
        this.active = newActive;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive(){
        return this.active;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "💳 " + getClass().getSimpleName() + "\n" +
                "• IBAN:        " + iban + "\n" +
                "• Owner ID:       " + userId + "\n" +
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
        return this.iban.compareTo(other.iban);
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

