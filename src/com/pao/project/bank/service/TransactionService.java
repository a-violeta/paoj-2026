package com.pao.project.bank.service;

import com.pao.project.bank.model.CurrencyConverter;
import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.account.CheckingAccount;
import com.pao.project.bank.model.transaction.*;
import com.pao.project.bank.repository.AccountRepository;
import com.pao.project.bank.repository.TransactionRepository;
import com.pao.project.bank.util.DatabaseConnection;

import javax.xml.crypto.Data;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    // private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final AccountRepository accountRepository =
            new AccountRepository();

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

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // update balance
            account.deposit(amount);

            // update db
            accountRepository.update(account, conn);

            // create transaction
            Deposit deposit = new Deposit(amount, account);

            // save transaction in db
            transactionRepository.save(deposit, conn);

            // add to history
            account.addTransaction(deposit);
            conn.commit();
        } catch (Exception e){
            try{
                if(conn != null){
                    conn.rollback();
                }
            } catch(SQLException ex){
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally{
            try{
                if(conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
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

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // update balance
            account.withdraw(amount);

            // update db
            accountRepository.update(account, conn);

            // create transaction
            Withdrawal withdrawal = new Withdrawal(amount, account);

            // save in db
            transactionRepository.save(withdrawal, conn);

            // add to history
            account.addTransaction(withdrawal);
            conn.commit();
        } catch(Exception e){

            try{
                if(conn!=null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e);
        } finally {
            try{
                if(conn!=null){
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

        Connection conn =null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // update balances
            source.withdraw(amount);
            destination.deposit(amount);

            // update db
            accountRepository.update(source, conn);

            accountRepository.update(destination, conn);

            // create transaction
            Transfer transfer = new Transfer(amount, source, destination);

            transactionRepository.save(transfer, conn);

            // add to history
            source.addTransaction(transfer);
            destination.addTransaction(transfer);
            conn.commit();
        } catch (Exception e){
            try{
                if(conn!=null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // update balances
            from.withdraw(totalToWithdraw);
            to.deposit(amount);

            // update db
            accountRepository.update(from, conn);

            accountRepository.update(to, conn);

            // create transaction
            InternationalTransfer internationalTransfer = new InternationalTransfer(amount, from, to);

            transactionRepository.save(internationalTransfer, conn);

            // add to history
            from.addTransaction(internationalTransfer);
            to.addTransaction(internationalTransfer);
            conn.commit();
        } catch (Exception e){
            try{
                if(conn!=null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Transaction> getAllTransactions() {
        Connection conn = null;

        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return transactionRepository.findAll(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeTransactionById(String id) {
        Transaction t = findTransactionById(id);

        if (t == null) return false;
        //delete from db

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            //delete from account transactionHistory
            Account from = t.getSourceAccount();
            Account to = t.getDestinationAccount();
            if (from != null) {
                from.getTransactionHistory().remove(t);
            }
            if (to != null) {
                to.getTransactionHistory().remove(t);
            }

            transactionRepository.delete(id, conn);
            conn.commit();
            return true;
        } catch (Exception e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e);

        } finally {

            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Transaction findTransactionById(String id) {
        Connection conn = null;

        try {
            conn=DatabaseConnection.getInstance().getConnection();

            return transactionRepository.findById(id, conn).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
