package com.pao.project.bank.service;

import com.pao.project.bank.model.User;
import com.pao.project.bank.model.account.Account;
import com.pao.project.bank.repository.UserRepository;
import com.pao.project.bank.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    //private List<User> users;
    private final UserRepository userRepository = new UserRepository();

    private UserService() {}

    private static class Holder {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return UserService.Holder.INSTANCE;
    }

    public void addUser(User user) {
        if(user == null) return;

        Connection conn = null;

        try{
            conn= DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            userRepository.save(user, conn);

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

    public void deleteUser(User user) {
        if (user == null) return;

        Connection conn = null;

        try {
            conn=DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // delete accounts => and cards and transactions
            for (Account acc : new ArrayList<>(user.getAccounts())) {
                AccountService.getInstance().deleteAccount(acc);
            }

            // delete user
            userRepository.delete(user.getId(), conn);

            conn.commit();

            System.out.println("✔ User " + user.getName() + " and all associated data have been deleted.");
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

    public User findUserById(String id){
        if (id == null || id.isBlank()) return null;

        Connection conn = null;

        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return userRepository.findById(id, conn).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        Connection conn=null;

        try{
            conn=DatabaseConnection.getInstance().getConnection();

            return userRepository.findAll(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
