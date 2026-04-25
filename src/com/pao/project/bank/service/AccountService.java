package com.pao.project.bank.service;

import com.pao.project.bank.exception.InactiveAccountException;
import com.pao.project.bank.exception.NullAccountException;
import com.pao.project.bank.model.Card;
import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.account.SavingsAccount;
import com.pao.project.bank.model.account.LoanAccount;
import com.pao.project.bank.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    private List<Account> accounts;
    private final Map<String, Account> accountsByIban = new HashMap<>();
    // reference the same objects, but the collections need to be updated separately

    private AccountService() {
        this.accounts = new ArrayList<>();
    }

    private static class Holder {
        private static final AccountService INSTANCE = new AccountService();
    }

    public static AccountService getInstance() {
        return AccountService.Holder.INSTANCE;
    }

    public void addAccount(Account account) {
        if(account == null) return;

        accounts.add(account);
        accountsByIban.put(account.getIban(), account);
        account.getOwner().getAccounts().add(account);
    }

    public void deleteAccount(Account account) {
        if (account == null) return;

        // delete cards
        for (Card card : new ArrayList<>(CardService.getInstance().getAllCards())) {
            if (card.getAccount().equals(account)) {
                CardService.getInstance().deleteCard(card);
            }
        }

        // delete transactions
        for (Transaction t : new ArrayList<>(account.getTransactionHistory())) {
            TransactionService.getInstance().removeTransactionById(t.getId());
        }

        // delete account from owner
        account.getOwner().getAccounts().remove(account);

        // and from accountService list
        accounts.remove(account);
        accountsByIban.remove(account.getIban());

        System.out.println("✔ Account " + account.getIban() + " and all associated data have been deleted.");
    }

    public Account findAccountByIban(String iban) {
        return accountsByIban.get(iban);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts); // copy
    }

    // takes care of the cards too
    public void deactivateAccount(Account account) {
        if (account == null) {
            throw new NullAccountException("Cannot deactivate a null account.");
        }

        if (!account.isActive()) {
            throw new InactiveAccountException("Account " + account.getIban() + " is already inactive.");
        }

        // deactivate account
        account.setActive(false);
        System.out.println("✔ Account " + account.getIban() + " has been deactivated.");

        // and the cards too
        CardService.getInstance().deactivateCardsForAccount(account);
    }

    public void changeInterestRate(SavingsAccount account, double newRate) {
        if(account == null) return;
        if(newRate <= 0 || newRate >= 0.3)
            throw new IllegalArgumentException("⚠️ New interest rate not accepted.");

        account.setInterestRate(newRate);
    }

    public void changeLoanInterest(LoanAccount account, double newRate) {
        if(account == null) return;
        if(newRate <= 0 || newRate >= 0.3)
            throw new IllegalArgumentException("⚠️ New interest rate not accepted.");

        account.setInterestRate(newRate);
    }
}
