package com.pao.project.bank.model.transaction;

import com.pao.project.bank.model.account.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaction {

    private final String id;
    protected LocalDateTime timestamp;
    private final double amount;
    //private final Account sourceAccount;
    //private final Account destinationAccount;
    // should store their iban instead:
    private String sourceIban;
    private String destinationIban;
    protected TransactionType type;

    public Transaction(double amount, String sourceIban, String destinationIban, TransactionType type) {

        if (amount < 0){
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (type == null){
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }

        switch (type) {

            case DEPOSIT -> {
                if (sourceIban == null || sourceIban.isBlank()) {
                    throw new IllegalArgumentException("Deposit must have a source account.");
                }
                if (destinationIban != null) {
                    throw new IllegalArgumentException("Deposit cannot have a destination account.");
                }
            }

            case WITHDRAWAL -> {
                if (sourceIban == null || sourceIban.isBlank()) {
                    throw new IllegalArgumentException("Withdrawal must have a source account.");
                }
                if (destinationIban != null) {
                    throw new IllegalArgumentException("Withdrawal cannot have a destination account.");
                }
            }

            case TRANSFER -> {
                if (sourceIban == null || destinationIban == null || sourceIban.isBlank() || destinationIban.isBlank()) {
                    throw new IllegalArgumentException("Transfer must have both source and destination accounts.");
                }
                if (sourceIban == destinationIban) {
                    throw new IllegalArgumentException("Transfer cannot use the same account as both source and destination.");
                }
            }

            case INTERNATIONALTRANSFER -> {
                if (sourceIban == null || destinationIban == null || sourceIban.isBlank() || destinationIban.isBlank()) {
                    throw new IllegalArgumentException("Transfer must have both source and destination accounts.");
                }
                if (sourceIban == destinationIban) {
                    throw new IllegalArgumentException("Transfer cannot use the same account as both source and destination.");
                }
            }
        }

        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        //this.sourceAccount = sourceAccount;
        //this.destinationAccount = destinationAccount;
        this.sourceIban = sourceIban;
        this.destinationIban = destinationIban;
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

    public String getSourceIban() {
        return sourceIban;
    }

    public String getDestinationIban() {
        return destinationIban;
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
                "• From:        " + sourceIban + "\n" +
                "• To:          " + destinationIban + "\n" +
                "----------------------------------------";
    }
}
