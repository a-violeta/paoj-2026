package com.pao.project.bank.service;

import com.pao.project.bank.model.CurrencyConverter;
import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.account.CheckingAccount;
import com.pao.project.bank.model.transaction.*;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();

    private TransactionService() {}

    private static class Holder {
        private static final TransactionService INSTANCE = new TransactionService();
    }

    public static TransactionService getInstance() {
        return TransactionService.Holder.INSTANCE;
    }

    //'add transaction' function is the following 4:

    public void deposit(Account account, double amount) {
        if(account == null){
            throw new IllegalArgumentException("Account missing.");
        }
        if (!account.isActive()) {
            throw new IllegalStateException("Account is inactive.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        // update balance
        account.deposit(amount);

        // create transaction
        Deposit deposit = new Deposit(amount, account);

        transactions.add(deposit);

        // add to history
        account.addTransaction(deposit);
    }

    public void withdraw(Account account, double amount) {
        if(account == null){
            throw new IllegalArgumentException("Account missing.");
        }
        if (!account.isActive()) {
            throw new IllegalStateException("Account is inactive");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // overdraft (pt CheckingAccount)
        if (account instanceof CheckingAccount checking) {
            double limit = checking.getOverdraftLimit();
            if (account.getBalance() - amount < -limit) {
                throw new IllegalStateException("Insufficient funds (overdraft limit exceeded).");
            }
        } else {
            // normal accounts
            if (account.getBalance() < amount) {
                throw new IllegalStateException("Insufficient funds.");
            }
        }

        // update balance
        account.withdraw(amount);

        // create transaction
        Withdrawal withdrawal = new Withdrawal(amount, account);

        transactions.add(withdrawal);

        // add to history
        account.addTransaction(withdrawal);
    }

    public void transfer(Account source, Account destination, double amount) {
        if(source == null || destination == null){
            throw new IllegalArgumentException("One or both accounts is missing.");
        }
        if (!source.isActive() || !destination.isActive()) {
            throw new IllegalStateException("One or both of the accounts is inactive.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (source == destination) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        // check money
        if (source instanceof CheckingAccount checking) {
            double limit = checking.getOverdraftLimit();
            if (source.getBalance() - amount < -limit) {
                throw new IllegalStateException("Insufficient funds (overdraft limit exceeded).");
            }
        } else {
            if (source.getBalance() < amount) {
                throw new IllegalStateException("Insufficient funds.");
            }
        }

        // update balances
        source.withdraw(amount);
        destination.deposit(amount);

        // create transaction
        Transfer transfer = new Transfer(amount, source, destination);

        transactions.add(transfer);

        // add to history
        source.addTransaction(transfer);
        destination.addTransaction(transfer);
    }

    public void internationalTransfer(Account from, Account to, double amount) {

        //set the fee at 2% of the amount and add it to the money

        if(from == null || to == null){
            throw new IllegalArgumentException("One or both accounts are missing.");
        }
        if (!from.isActive() || !to.isActive()) {
            throw new IllegalStateException("One of the accounts is inactive.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (from == to) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        // check money
        double amountInSourceCurrency =CurrencyConverter.convert(amount, to.getCurrency(), from.getCurrency());
        double fee = amountInSourceCurrency * 0.02;
        double totalToWithdraw = amountInSourceCurrency + fee;
        if (from instanceof CheckingAccount checking) {
            double limit = checking.getOverdraftLimit();
            if (from.getBalance() - totalToWithdraw < -limit) {
                throw new IllegalStateException("Insufficient funds (overdraft limit exceeded).");
            }
        } else {
            if (from.getBalance() < totalToWithdraw) {
                throw new IllegalStateException("Insufficient funds.");
            }
        }

        // update balances
        from.withdraw(totalToWithdraw);
        to.deposit(amount);

        // create transaction
        InternationalTransfer internationalTransfer = new InternationalTransfer(amount, from, to);

        transactions.add(internationalTransfer);

        // add to history
        from.addTransaction(internationalTransfer);
        to.addTransaction(internationalTransfer);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions); // copy
    }

    public boolean removeTransactionById(String id) {
        Transaction t = null;

        for (Transaction tr : transactions) {
            if (tr.getId().equals(id)) {
                t = tr;
                break;
            }
        }

        if (t == null) return false;

        //delete from account transactionHistory
        Account from = t.getSourceAccount();
        Account to = t.getDestinationAccount();
        if (from != null) {
            from.getTransactionHistory().remove(t);
        }
        if (to != null) {
            to.getTransactionHistory().remove(t);
        }

        //delete from transactionService list
        transactions.remove(t);

        return true;
    }

    public Transaction findTransactionById(String id) {
        for (Transaction t : transactions) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
