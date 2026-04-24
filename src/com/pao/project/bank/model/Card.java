package com.pao.project.bank.model;

import com.pao.project.bank.model.account.Account;
import java.time.LocalDate;
import java.util.Random;

public class Card {

    private final ImmutableIdentifier cardNumber;      // 16 digits
    private String cvv;             // 3 digits
    private LocalDate expirationDate;
    private User owner;
    private Account account;
    private boolean active;
    private CardType type;

    public Card(User owner, Account account, CardType type) {
        if (account == null || owner == null || type == null) {
            throw new IllegalArgumentException("Account, owner and type cannot be null.");
        }

        if (!account.isActive()) {
            throw new IllegalStateException("Cannot create a card for an inactive account.");
        }

        this.cardNumber = new ImmutableIdentifier(generateCardNumber());
        this.cvv = generateCvv();
        this.expirationDate = LocalDate.now().plusYears(5);
        this.owner = owner;
        this.account = account;
        this.active = true;
        this.type = type;
    }

    private String generateCardNumber() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append(r.nextInt(10)); // append 16 random digits
        }

        return sb.toString();
    }

    private String generateCvv() {
        Random r = new Random();
        int cvv = r.nextInt(900) + 100; // something between 100 – 999
        return String.valueOf(cvv);
    }

    //--------------------
    //get, set, toString
    //--------------------

    public ImmutableIdentifier getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public User getOwner() {
        return owner;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isActive() {
        return active;
    }

    public CardType getType() {
        return type;
    }

    public void setActive(boolean newActive){
        this.active = newActive;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "🪪 Card (" + type + ")\n" +
                "• Number:      " + cardNumber + "\n" +
                "• Owner:       " + owner.getName() + "\n" +
                "• Account:     " + account.getIban() + "\n" +
                "• Active:      " + (active ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }
}
