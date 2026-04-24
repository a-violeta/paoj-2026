package com.pao.project.bank.service;

import com.pao.project.bank.model.User;
import com.pao.project.bank.model.account.Account;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users;

    private UserService() {
        this.users = new ArrayList<>();
    }

    private static class Holder {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return UserService.Holder.INSTANCE;
    }

    public void addUser(User user) {
        if(user == null) return;
        users.add(user);
    }

    public void deleteUser(User user) {
        if (user == null) return;

        // delete accounts => and cards and transactions
        for (Account acc : new ArrayList<>(user.getAccounts())) {
            AccountService.getInstance().deleteAccount(acc);
        }

        // delete user
        users.remove(user);

        System.out.println("✔ User " + user.getName() + " and all associated data have been deleted.");
    }

    public User findUserById(String id){
        if(id.isBlank() || id.isEmpty()) return null;

        for (User u: users){
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

}
