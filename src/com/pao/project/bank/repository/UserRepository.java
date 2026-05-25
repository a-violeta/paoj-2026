package com.pao.project.bank.repository;

import com.pao.project.bank.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    // MAPPING DB -> OBJECT
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );

        // overwrite the generated id from Java with the one from DB
        // id is final -> no setter available
        return user;
    }

    // CREATE
    public void save(User user, Connection conn) throws SQLException {
        String sql = "INSERT INTO users (id, name, email, phone) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // FIND BY ID
    public Optional<User> findById(String id, Connection conn) throws SQLException {
        String sql = "SELECT id, name, email, phone FROM users WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    // FIND ALL
    public List<User> findAll(Connection conn) throws SQLException {
        String sql = "SELECT id, name, email, phone FROM users ORDER BY name";

        List<User> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // UPDATE
    public void update(User user, Connection conn) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, phone = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // DELETE
    public void delete(String id, Connection conn) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}