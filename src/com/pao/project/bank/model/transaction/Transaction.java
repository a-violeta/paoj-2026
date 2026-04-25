package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaction {

    private final String id;
    protected LocalDateTime timestamp;
    private final double amount;
    private final Account sourceAccount;
    private final Account destinationAccount;
    protected TransactionType type;

    public Transaction(double amount, Account sourceAccount, Account destinationAccount, TransactionType type) {

        if (amount < 0){
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (type == null){
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }

        switch (type) {

            case DEPOSIT -> {
                if (sourceAccount == null) {
                    throw new IllegalArgumentException("Deposit must have a source account.");
                }
                if (destinationAccount != null) {
                    throw new IllegalArgumentException("Deposit cannot have a destination account.");
                }
            }

            case WITHDRAWAL -> {
                if (sourceAccount == null) {
                    throw new IllegalArgumentException("Withdrawal must have a source account.");
                }
                if (destinationAccount != null) {
                    throw new IllegalArgumentException("Withdrawal cannot have a destination account.");
                }
            }

            case TRANSFER -> {
                if (sourceAccount == null || destinationAccount == null) {
                    throw new IllegalArgumentException("Transfer must have both source and destination accounts.");
                }
                if (sourceAccount == destinationAccount) {
                    throw new IllegalArgumentException("Transfer cannot use the same account as both source and destination.");
                }
            }

            case INTERNATIONALTRANSFER -> {
                if (sourceAccount == null || destinationAccount == null) {
                    throw new IllegalArgumentException("Transfer must have both source and destination accounts.");
                }
                if (sourceAccount == destinationAccount) {
                    throw new IllegalArgumentException("Transfer cannot use the same account as both source and destination.");
                }
                if(sourceAccount.getCurrency() == destinationAccount.getCurrency()){
                    throw new IllegalArgumentException("Accounts must have different currencies.");
                }
            }
        }

        // validate accounts
        if (sourceAccount != null && !sourceAccount.isActive()) {
            throw new IllegalStateException("Source account is inactive.");
        }

        if (destinationAccount != null && !destinationAccount.isActive()) {
            throw new IllegalStateException("Destination account is inactive.");
        }

        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        String title =
                type == TransactionType.DEPOSIT ? "➕ Deposit" :
                        type == TransactionType.WITHDRAWAL ? "➖ Withdrawal" :
                                "🔄 Transfer";

        return  "----------------------------------------\n" +
                title + "\n" +
                "• ID:          " + id + "\n" +
                "• Timestamp:   " + timestamp + "\n" +
                "• Amount:      💰 " + amount + "\n" +
                "• From:        " + (sourceAccount != null ? sourceAccount.getIban() : "-") + "\n" +
                "• To:          " + (destinationAccount != null ? destinationAccount.getIban() : "-") + "\n" +
                "----------------------------------------";
    }
}
