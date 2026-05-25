package com.pao.project.bank.repository;

import com.pao.project.bank.model.*;
import com.pao.project.bank.model.account.Account;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CardRepository {

    // MAPPING
    private Card mapRow(ResultSet rs) throws SQLException {

        String number = rs.getString("card_number");
        String cvv = rs.getString("cvv");
        LocalDate exp = rs.getDate("expiration_date").toLocalDate();
        boolean active = rs.getBoolean("active");

        CardType type = CardType.valueOf(rs.getString("type"));

        User owner = new UserPlaceholder(rs.getString("user_id"));
        Account account = new AccountPlaceholder(rs.getString("iban"));

        return new Card(
                new ImmutableIdentifier(number),
                cvv,
                exp,
                owner,
                account,
                active,
                type
        );
    }

    // SAVE
    public void save(Card card, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO cards
            (card_number, cvv, expiration_date, active, type, user_id, iban)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, card.getCardNumber().toString());
            ps.setString(2, card.getCvv());
            ps.setDate(3, Date.valueOf(card.getExpirationDate()));
            ps.setBoolean(4, card.isActive());
            ps.setString(5, card.getType().name());
            ps.setString(6, card.getOwner().getId());
            ps.setString(7, card.getAccount().getIban());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // FIND BY NUMBER
    public Optional<Card> findByNumber(String number, Connection conn) throws SQLException {
        String sql = "SELECT * FROM cards WHERE card_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    // FIND ALL
    public List<Card> findAll(Connection conn) throws SQLException {
        List<Card> list = new ArrayList<>();
        String sql = "SELECT * FROM cards";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // UPDATE (only active + type)
    public void update(Card card, Connection conn) throws SQLException {
        String sql = """
            UPDATE cards
            SET active = ?, type = ?
            WHERE card_number = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, card.isActive());
            ps.setString(2, card.getType().name());
            ps.setString(3, card.getCardNumber().toString());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    public void delete(String cardNumber, Connection conn) throws SQLException {
        String sql = "DELETE FROM cards WHERE card_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cardNumber);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // PLACEHOLDERS
    private static class UserPlaceholder extends User {
        public UserPlaceholder(String id) {
            super("temp", "temp@mail.com", "000");
            try {
                var f = User.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(this, id);
            } catch (Exception ignored) {}
        }
    }

    private static class AccountPlaceholder extends Account {
        public AccountPlaceholder(String iban) {
            super();
            this.setIban(iban);
        }
    }
}