package com.pao.laboratory12.service;

import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private static LibraryService instance;

    private LibraryService() {}

    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // =====================================================
    // TRANZACȚIE 1: borrowBook (commit / rollback)
    // =====================================================
    public long borrowBook(long readerId, long bookId) throws SQLException, IOException {

        Connection conn = getConnection();
        conn.setAutoCommit(false);

        try {
            // 1. verificăm disponibilitatea cărții
            String checkSql = "SELECT available FROM book WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setLong(1, bookId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Cartea nu există.");
                    }
                    if (rs.getInt("available") == 0) {
                        throw new SQLException("Cartea nu este disponibilă.");
                    }
                }
            }

            // 2. insert loan
            String insertSql =
                    "INSERT INTO loan(book_id, reader_id, loan_date) VALUES (?, ?, ?)";

            long loanId;

            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, bookId);
                ps.setLong(2, readerId);
                ps.setString(3, LocalDate.now().toString());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    loanId = keys.getLong(1);
                }
            }

            // 3. update book
            String updateSql = "UPDATE book SET available = 0 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setLong(1, bookId);
                ps.executeUpdate();
            }

            conn.commit();
            return loanId;

        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("borrowBook failed -> rollback: " + e.getMessage(), e);

        } finally {
            conn.setAutoCommit(true);
        }
    }

    // =====================================================
    // TRANZACȚIE 2: returnBook
    // =====================================================
    public void returnBook(long loanId) throws SQLException, IOException {

        Connection conn = getConnection();
        conn.setAutoCommit(false);

        try {
            long bookId;

            // 1. aflăm book_id
            try (PreparedStatement ps =
                         conn.prepareStatement("SELECT book_id FROM loan WHERE id = ?")) {
                ps.setLong(1, loanId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Loan inexistent.");
                    }
                    bookId = rs.getLong("book_id");
                }
            }

            // 2. set return date
            try (PreparedStatement ps =
                         conn.prepareStatement("UPDATE loan SET return_date = ? WHERE id = ?")) {
                ps.setString(1, LocalDate.now().toString());
                ps.setLong(2, loanId);
                ps.executeUpdate();
            }

            // 3. book available = 1
            try (PreparedStatement ps =
                         conn.prepareStatement("UPDATE book SET available = 1 WHERE id = ?")) {
                ps.setLong(1, bookId);
                ps.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw new SQLException("returnBook failed -> rollback", e);

        } finally {
            conn.setAutoCommit(true);
        }
    }

    // =====================================================
    // JOIN 1: imprumuturi active
    // =====================================================
    public List<String> getActiveLoansWithDetails() throws SQLException, IOException {

        String sql = """
                SELECT l.id AS loan_id,
                       b.title AS book_title,
                       r.name AS reader_name,
                       l.loan_date
                FROM loan l
                JOIN book b ON l.book_id = b.id
                JOIN reader r ON l.reader_id = r.id
                WHERE l.return_date IS NULL
                """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(
                        "Loan " + rs.getLong("loan_id") +
                                " | " + rs.getString("book_title") +
                                " -> " + rs.getString("reader_name") +
                                " | " + rs.getString("loan_date")
                );
            }
        }

        return result;
    }

    // =====================================================
    // JOIN 2: top cărți împrumutate
    // =====================================================
    public List<String> getTopBorrowedBooksWithAuthor() throws SQLException, IOException {

        String sql = """
                SELECT b.title AS book_title,
                       a.name AS author_name,
                       COUNT(l.id) AS cnt
                FROM book b
                JOIN author a ON b.author_id = a.id
                LEFT JOIN loan l ON l.book_id = b.id
                GROUP BY b.id, b.title, a.name
                ORDER BY cnt DESC
                """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(
                        rs.getString("book_title") +
                                " - " + rs.getString("author_name") +
                                " (" + rs.getLong("cnt") + ")"
                );
            }
        }

        return result;
    }

    // =====================================================
    // JOIN 3: împrumuturi per reader
    // =====================================================
    public List<String> getLoansCountPerReader() throws SQLException, IOException {

        String sql = """
                SELECT r.name,
                       COUNT(l.id) AS cnt
                FROM reader r
                LEFT JOIN loan l ON l.reader_id = r.id
                GROUP BY r.id, r.name
                """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(rs.getString("name") + " -> " + rs.getLong("cnt"));
            }
        }

        return result;
    }
}