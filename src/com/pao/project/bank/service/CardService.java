package com.pao.project.bank.service;

import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardService {

    private List<Card> cards;

    private CardService() {
        this.cards = new ArrayList<>();
    }

    private static class Holder {
        private static final CardService INSTANCE = new CardService();
    }

    public static CardService getInstance() {
        return CardService.Holder.INSTANCE;
    }

    public void addCard(Card card) {
        if(card == null) return;

        cards.add(card);
        card.getOwner().addCard(card);
    }

    public void deleteCard(Card card) {
        if(card == null) return;

        cards.remove(card);
        card.getOwner().getCards().remove(card);
    }

    public Card findCardByNumber(String number) {
        for (Card c : cards) {
            if (c.getCardNumber().toString().equals(number)) {
                return c;
            }
        }
        return null;
    }

    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }

    public void deactivateCard(Card card) {
        if(card == null) return;
        card.setActive(false);
    }

    public void deactivateCardsForAccount(Account account) {
        for (Card c : cards) {
            if (c.getAccount().equals(account) && c.isActive()) {
                c.setActive(false);
                System.out.println("   → Card " + c.getCardNumber() + " has been deactivated automatically.");
            }
        }
    }

}
