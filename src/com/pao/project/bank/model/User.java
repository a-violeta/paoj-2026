package com.pao.project.bank.model;

import com.pao.project.bank.model.account.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final String id;
    private String name;
    private String email;
    private String phone;
    private List<Account> accounts;
    private List<Card> cards;

    public User(String name, String email, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public void addAccount(Account account) {
        if( account == null) return;
        accounts.add(account);
    }

    public void addCard(Card card) {
        if(card == null) return;
        cards.add(card);
    }

    //-------------------
    //get, set, toString, equals, hashCode
    //-------------------

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "👤 User\n" +
                "• Name:        " + name + "\n" +
                "• Accounts:    " + accounts.size() + "\n" +
                "• Cards:       " + cards.size() + "\n" +
                "----------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
