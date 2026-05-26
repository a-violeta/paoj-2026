package com.pao.project.bank.repository;

import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.model.transaction.*;

import java.sql.*;
import java.util.*;

public class TransactionRepository {

    // SAVE
    public void save(Transaction tx, Connection conn) throws SQLException {

        String sql = """
            INSERT INTO transactions
            (id, timestamp, amount, type, fee, source_iban, destination_iban)
            VALUES (?, NOW(), ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tx.getId());
            ps.setDouble(2, tx.getAmount());

            if (tx instanceof Deposit) {
                ps.setString(3, "DEPOSIT");
                ps.setNull(4, Types.NUMERIC);
                ps.setString(5, tx.getSourceIban());
                ps.setNull(6, Types.VARCHAR);

            } else if (tx instanceof Withdrawal) {
                ps.setString(3, "WITHDRAWAL");
                ps.setNull(4, Types.NUMERIC);
                ps.setString(5, tx.getSourceIban());
                ps.setNull(6, Types.VARCHAR);

            } else if (tx instanceof InternationalTransfer it) {
                ps.setString(3, "INTERNATIONAL_TRANSFER");
                ps.setDouble(4, it.getFee());
                ps.setString(5, tx.getSourceIban());
                ps.setString(6, tx.getDestinationIban());

            } else if (tx instanceof Transfer) {
                ps.setString(3, "TRANSFER");
                ps.setNull(4, Types.NUMERIC);
                ps.setString(5, tx.getSourceIban());
                ps.setString(6, tx.getDestinationIban());
            }

            ps.executeUpdate();
        }
    }

    // FIND BY ID
    public Optional<Transaction> findById(String id, Connection conn) throws SQLException {

        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    // FIND ALL
    public List<Transaction> findAll(Connection conn) throws SQLException {

        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // DELETE
    public void delete(String id, Connection conn) throws SQLException {

        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    // MAPPING (SIMPLE)
    private Transaction mapRow(ResultSet rs) throws SQLException {

        String type = rs.getString("type");
        double amount = rs.getDouble("amount");

        String sourceIban = rs.getString("source_iban");
        String destIban = rs.getString("destination_iban");

        return switch (type) {

            case "DEPOSIT" ->
                    new Deposit(amount, sourceIban);

            case "WITHDRAWAL" ->
                    new Withdrawal(amount, sourceIban);

            case "TRANSFER" ->
                    new Transfer(amount, sourceIban, destIban);

            case "INTERNATIONAL_TRANSFER" -> {
                double fee = rs.getDouble("fee");
                    yield new InternationalTransfer(amount, sourceIban, destIban);
            }

            default -> throw new IllegalStateException("Unknown type");
        };
    }

    public List<String> getTransactionsWithUsers(String iban, Connection conn) throws SQLException {

        String sql = """
            SELECT 
                t.id,
                t.amount,
                t.type,
                t.source_iban,
                t.destination_iban,
                u.name AS user_name
            FROM transactions t
            JOIN users u 
                ON u.id = (
                    SELECT a.user_id 
                    FROM accounts a 
                    WHERE a.iban = t.source_iban
                    LIMIT 1
                )
            WHERE t.source_iban = ? OR t.destination_iban = ?
            ORDER BY t.timestamp DESC
        """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, iban);
            ps.setString(2, iban);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    result.add(
                            "----------------------------------------\n" +
                                    "💸 TRANSACTION ID: " + rs.getString("id") + "\n" +
                                    "👤 User:           " + rs.getString("user_name") + "\n" +
                                    "📌 Type:           " + rs.getString("type") + "\n" +
                                    "💰 Amount:         " + rs.getDouble("amount") + "\n" +
                                    "➡ From:            " + rs.getString("source_iban") + "\n" +
                                    "⬅ To:              " + rs.getString("destination_iban") + "\n" +
                                    "----------------------------------------"
                    );
                }
            }
        }

        return result;
    }
}