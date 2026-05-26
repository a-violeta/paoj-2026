package com.pao.project.bank.service;

import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.Card;
import com.pao.project.bank.repository.AccountRepository;
import com.pao.project.bank.repository.CardRepository;
import com.pao.project.bank.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardService {

    // private List<Card> cards;
    private final CardRepository cardRepository = new CardRepository();

    private CardService() {}

    private static class Holder {
        private static final CardService INSTANCE = new CardService();
    }

    public static CardService getInstance() {
        return CardService.Holder.INSTANCE;
    }

    public void addCard(Card card) {
        if(card == null) return;

        Account acc = AccountService.getInstance().findAccountByIban(card.getAccountIban());

        if(!acc.isActive()){
            throw new IllegalStateException("Cannot create a card for an inactive account.");
        }

        Connection conn = null;

        try {
            conn= DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // pt logica din memorie
            //card.getOwner().addCard(card);

            cardRepository.save(card, conn);
            conn.commit();
            AuditService.getInstance().logAction("add_card");
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

    public void deleteCard(Card card) {
        if(card == null) return;

        Connection conn = null;
        try {
            conn=DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            cardRepository.delete(card.getCardNumber().toString(), conn);
            //card.getOwner().getCards().remove(card);

            conn.commit();
            AuditService.getInstance().logAction("delete_card");
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

    public Card findCardByNumber(String number) {
        Connection conn = null;
        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return cardRepository.findByNumber(number, conn).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Card> getAllCards() {
        Connection conn = null;

        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return cardRepository.findAll(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deactivateCard(Card card) {
        if(card == null) return;

        Connection conn = null;

        try {
            conn=DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            card.setActive(false);
            cardRepository.update(card, conn);

            conn.commit();
            AuditService.getInstance().logAction("deactivate_card");
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

    public void deactivateCardsForAccount(Account account, Connection conn) {
        for (Card c : getAllCards()) {
            if (c.getAccountIban().equals(account.getIban()) && c.isActive()) {

                try {
                    c.setActive(false);
                    cardRepository.update(c, conn);

                    System.out.println("   → Card " + c.getCardNumber() + " has been deactivated automatically.");

                    AuditService.getInstance().logAction("deactivate_card");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
