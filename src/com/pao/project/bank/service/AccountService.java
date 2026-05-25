package com.pao.project.bank.service;

import com.pao.project.bank.exception.InactiveAccountException;
import com.pao.project.bank.exception.NullAccountException;
import com.pao.project.bank.model.Card;
import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.account.SavingsAccount;
import com.pao.project.bank.model.account.LoanAccount;
import com.pao.project.bank.model.transaction.Transaction;
import com.pao.project.bank.repository.AccountRepository;
import com.pao.project.bank.util.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    //private List<Account> accounts;
    private final AccountRepository accountRepository = new AccountRepository();
    private final Map<String, Account> accountsByIban = new HashMap<>();
    // reference the same objects, but the collections need to be updated separately

    private AccountService() {}

    private static class Holder {
        private static final AccountService INSTANCE = new AccountService();
    }

    public static AccountService getInstance() {
        return AccountService.Holder.INSTANCE;
    }

    public void addAccount(Account account) {
        if(account == null) return;

        Connection conn = null;

        try {
            conn= DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            accountsByIban.put(account.getIban(), account);
            //account.getOwner().getAccounts().add(account);
            accountRepository.save(account, conn);
            conn.commit();
            AuditService.getInstance().logAction("add_account");
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

    public void deleteAccount(Account account) {
        if (account == null) return;

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // delete cards
            for (Card card :List.copyOf(CardService.getInstance().getAllCards())) {
                if (card.getAccountIban().equals(account.getIban())) {
                    CardService.getInstance().deleteCard(card);
                }
            }

            // delete transactions
            for (Transaction t :List.copyOf(TransactionService.getInstance().getAllTransactions())) {
                if (t.getSourceIban().equals(account.getIban()) || t.getDestinationIban().equals(account.getIban())) {
                    TransactionService.getInstance().removeTransactionById(t.getId());
                }
            }

            accountRepository.delete(account.getIban(), conn);

            // delete account from owner
            //account.getOwner().getAccounts().remove(account);

            accountsByIban.remove(account.getIban());

            conn.commit();
            AuditService.getInstance().logAction("delete_account");

            System.out.println("✔ Account " + account.getIban() + " and all associated data have been deleted.");
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

    public Account findAccountByIban(String iban) {
        Connection conn = null;
        try{
            conn=DatabaseConnection.getInstance().getConnection();
            //conn.setAutoCommit(false);

            return accountRepository.findByIban(iban, conn).orElse(null);
            //conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getAllAccounts() {
        Connection conn= null;
        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return accountRepository.findAll(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getOwnerAccounts(String ownerId) {

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            return accountRepository.findByUserId(ownerId, conn);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // takes care of the cards too
    public void deactivateAccount(Account account) {
        if (account == null) {
            throw new NullAccountException("Cannot deactivate a null account.");
        }

        if (!account.isActive()) {
            throw new InactiveAccountException("Account " + account.getIban() + " is already inactive.");
        }

        Connection conn = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // deactivate account
            account.setActive(false);

            accountRepository.update(account, conn);
            System.out.println("✔ Account " + account.getIban() + " has been deactivated.");

            // and the cards too
            CardService.getInstance().deactivateCardsForAccount(account);
            conn.commit();
            AuditService.getInstance().logAction("deactivate_account");
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

    public void changeInterestRate(SavingsAccount account, double newRate) {
        if(account == null) return;
        if(newRate <= 0 || newRate >= 0.3)
            throw new IllegalArgumentException("⚠️ New interest rate not accepted.");

        Connection conn = null;

        try {
            conn= DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            account.setInterestRate(newRate);
            accountRepository.update(account, conn);
            conn.commit();
            AuditService.getInstance().logAction("update_account");
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

    public void changeLoanInterest(LoanAccount account, double newRate) {
        if(account == null) return;
        if(newRate <= 0 || newRate >= 0.3)
            throw new IllegalArgumentException("⚠️ New interest rate not accepted.");

        Connection conn = null;

        try{
            conn =DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            account.setInterestRate(newRate);
            accountRepository.update(account, conn);
            conn.commit();
            AuditService.getInstance().logAction("update_account");
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
}
