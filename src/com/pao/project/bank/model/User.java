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
    // lists become useless after db integration, but i keep them; just don t use them
    //private List<Account> accounts;
    //private List<Card> cards;

    public User(String id, String name, String email, String phone) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (email == null || email.isBlank()) throw new IllegalArgumentException();
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException();

        // can it be acceptable to make a user without these checks taking into account DB integration?

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        //this.accounts = new ArrayList<>();
        //this.cards = new ArrayList<>();
    }

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
        //this.accounts = new ArrayList<>();
        //this.cards = new ArrayList<>();
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

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "👤 User\n" +
                "• Name:        " + name + "\n" +
                "• Email:       " + email + "\n" +
                "• Phone:       " + phone + "\n" +
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
