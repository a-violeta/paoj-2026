package com.pao.project.bank.service;

import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.Card;
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

        Connection conn = null;

        try {
            conn= DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // pt logica din memorie
            card.getOwner().addCard(card);

            cardRepository.save(card, conn);
            conn.commit();
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
            card.getOwner().getCards().remove(card);

            conn.commit();
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

        try{
            return cardRepository.findByNumber(number).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Card> getAllCards() {
        try{
            return cardRepository.findAll();
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

    public void deactivateCardsForAccount(Account account) {
        for (Card c : getAllCards()) {
            if (c.getAccount().equals(account) && c.isActive()) {

                Connection conn =null;

                try {
                    conn=DatabaseConnection.getInstance().getConnection();
                    conn.setAutoCommit(false);

                    cardRepository.update(c, conn);
                    c.setActive(false);
                    System.out.println("   → Card " + c.getCardNumber() + " has been deactivated automatically.");

                    conn.commit();
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
    }

}
