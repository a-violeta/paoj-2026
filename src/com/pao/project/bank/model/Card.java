package com.pao.project.bank.model;

import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.service.UserService;

import java.time.LocalDate;
import java.util.Random;

public class Card {

    private final ImmutableIdentifier cardNumber;      // 16 digits
    private final String cvv;             // 3 digits
    private final LocalDate expirationDate;
    //private final User owner;
    //private final Account account;
    // should have also:
    private final String userId;
    private final String accountIban;

    private boolean active;
    private CardType type;

    public Card(
            String cardNumber,
            String cvv,
            LocalDate expirationDate,
            String userId,
            String accountIban,
            boolean active,
            CardType type
    ) {
        this.cardNumber = new ImmutableIdentifier(cardNumber);
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.userId = userId;
        this.accountIban = accountIban;
        this.active = active;
        this.type = type;
    }

    public Card(String ownerId, String accountIban, CardType type) {
        if (accountIban == null || ownerId == null || type == null || ownerId.isBlank() || accountIban.isBlank()) {
            throw new IllegalArgumentException("Account, owner and type cannot be null.");
        }

        this.cardNumber = new ImmutableIdentifier(generateCardNumber());
        this.cvv = generateCvv();
        this.expirationDate = LocalDate.now().plusYears(5);
        //this.owner = owner;
        //this.account = account;
        this.active = true;
        this.type = type;
        this.accountIban = accountIban;
        this.userId = ownerId;
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

    public boolean isActive() {
        return active;
    }

    public CardType getType() {
        return type;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public String getUserId() {
        return userId;
    }

    public void setActive(boolean newActive){
        this.active = newActive;
    }

    @Override
    public String toString() {
        return  "----------------------------------------\n" +
                "🪪 Card (" + type + ")\n" +
                "• Number:      " + cardNumber + "\n" +
                "• Owner ID:       " + userId + "\n" +
                "• Account:     " + accountIban + "\n" +
                "• Active:      " + (active ? "🔓 YES" : "🔒 NO") + "\n" +
                "----------------------------------------";
    }
}
