package com.pao.project.bank.repository;

import com.pao.project.bank.model.Currency;
import com.pao.project.bank.model.User;
import com.pao.project.bank.model.account.*;
import com.pao.project.bank.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // =========================================================
    // MAPPING (FOARTE IMPORTANT LA TINE)
    // =========================================================
    private Account mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("type");

        Account account;

        switch (type) {
            case "CHECKING" -> {
                CheckingAccount acc = new CheckingAccount();
                acc.setOverdraftLimit(rs.getDouble("overdraft_limit"));
                account = acc;
            }
            case "SAVINGS" -> {
                SavingsAccount acc = new SavingsAccount();
                acc.setInterestRate(rs.getDouble("interest_rate"));
                account = acc;
            }
            case "LOAN" -> {
                LoanAccount acc = new LoanAccount();
                acc.setInterestRate(rs.getDouble("interest_rate"));
                acc.setLoanAmount(rs.getDouble("loan_amount"));
                acc.setRemainingAmount(rs.getDouble("remaining_amount"));
                acc.setDueDate(rs.getDate("due_date").toLocalDate());
                account = acc;
            }
            default -> throw new IllegalStateException("Unknown account type: " + type);
        }

        account.setIban(rs.getString("iban"));
        account.setBalance(rs.getDouble("balance"));
        account.setCurrency(Currency.valueOf(rs.getString("currency")));
        account.setActive(rs.getBoolean("active"));

        // user minimal (doar ID + eventual name)
        User owner = new UserPlaceholder(rs.getString("user_id"));
        account.setOwner(owner);

        return account;
    }

    // =========================================================
    // SAVE
    // =========================================================
    public void save(Account account) throws SQLException {
        String sql = """
            INSERT INTO accounts
            (iban, balance, currency, type,
             overdraft_limit, interest_rate,
             loan_amount, remaining_amount, due_date,
             created_at, active, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)
        """;

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {

            ps.setString(1, account.getIban());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getCurrency().name());

            if (account instanceof CheckingAccount) {
                ps.setString(4, "CHECKING");
                ps.setDouble(5, ((CheckingAccount) account).getOverdraftLimit());
                ps.setNull(6, Types.NUMERIC);
                ps.setNull(7, Types.NUMERIC);
                ps.setNull(8, Types.NUMERIC);
                ps.setNull(9, Types.DATE);

            } else if (account instanceof SavingsAccount) {
                ps.setString(4, "SAVINGS");
                ps.setNull(5, Types.NUMERIC);
                ps.setDouble(6, ((SavingsAccount) account).getInterestRate());
                ps.setNull(7, Types.NUMERIC);
                ps.setNull(8, Types.NUMERIC);
                ps.setNull(9, Types.DATE);

            } else if (account instanceof LoanAccount) {
                LoanAccount acc = (LoanAccount) account;
                ps.setString(4, "LOAN");
                ps.setNull(5, Types.NUMERIC);
                ps.setDouble(6, acc.getInterestRate());
                ps.setDouble(7, acc.getLoanAmount());
                ps.setDouble(8, acc.getRemainingAmount());
                ps.setDate(9, Date.valueOf(acc.getDueDate()));
            }

            ps.setBoolean(10, account.isActive());
            ps.setString(11, account.getOwner().getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // =========================================================
    // FIND BY IBAN
    // =========================================================
    public Optional<Account> findByIban(String iban) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE iban = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // =========================================================
    // FIND ALL
    // =========================================================
    public List<Account> findAll() throws SQLException {
        String sql = "SELECT * FROM accounts";

        List<Account> list = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (IOException e) {
            throw new SQLException(e);
        }

        return list;
    }

    // =========================================================
    // UPDATE
    // =========================================================
    public void update(Account account) throws SQLException {
        String sql = """
            UPDATE accounts
            SET balance = ?, active = ?
            WHERE iban = ?
        """;

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, account.getBalance());
            ps.setBoolean(2, account.isActive());
            ps.setString(3, account.getIban());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // =========================================================
    // DELETE
    // =========================================================
    public void delete(String iban) throws SQLException {
        String sql = "DELETE FROM accounts WHERE iban = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // =========================================================
    // helper minimal user (fără să încarci tot graful)
    // =========================================================
    private static class UserPlaceholder extends User {
        public UserPlaceholder(String id) {
            super("temp", "temp@mail.com", "000");
            try {
                java.lang.reflect.Field f = User.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(this, id);
            } catch (Exception ignored) {}
        }
    }
}