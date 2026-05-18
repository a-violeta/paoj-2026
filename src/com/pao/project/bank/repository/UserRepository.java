package com.pao.project.bank.repository;

import com.pao.project.bank.model.User;
import com.pao.project.bank.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // -------------------------
    // MAPPING DB -> OBJECT
    // -------------------------
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );

        // ❗ suprascriem id-ul generat în Java cu cel din DB
        // DAR ATENȚIE: la tine id este final → deci NU ai voie să-l setezi
        // => vezi explicația mai jos
        return user;
    }

    // -------------------------
    // CREATE
    // -------------------------
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name, email, phone) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());

            ps.executeUpdate();

        } catch (IOException e) {
            throw new SQLException("DB connection error", e);
        }
    }

    // -------------------------
    // READ BY ID
    // -------------------------
    public Optional<User> findById(String id) throws SQLException {
        String sql = "SELECT id, name, email, phone FROM users WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // ⚠️ vezi explicația de mai jos (important)
                    return Optional.of(
                            new User(
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    rs.getString("phone")
                            )
                    );
                }
                return Optional.empty();
            }

        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, name, email, phone FROM users ORDER BY name";

        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(
                        new User(
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("phone")
                        )
                );
            }

        } catch (IOException e) {
            throw new SQLException(e);
        }

        return users;
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getId());

            ps.executeUpdate();

        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}