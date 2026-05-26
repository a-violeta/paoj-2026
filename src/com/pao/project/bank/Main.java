package com.pao.project.bank;

import com.pao.project.bank.exception.IllegalCurrencyException;
import com.pao.project.bank.exception.InactiveAccountException;
import com.pao.project.bank.exception.NullAccountException;
import com.pao.project.bank.model.*;
import com.pao.project.bank.model.account.*;
import com.pao.project.bank.model.transaction.Transaction;
import com.pao.project.bank.service.AccountService;
import com.pao.project.bank.service.UserService;
import com.pao.project.bank.service.CardService;
import com.pao.project.bank.service.TransactionService;
import com.pao.project.bank.util.DatabaseConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) {

//        try {
//            Connection connection =
//                    DriverManager.getConnection(
//                            "jdbc:postgresql://localhost:5433/bank_db",
//                            "postgres",
//                            "postgres"
//                    );

//            System.out.println("Connected!");

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            Connection connection =
//                    DatabaseConnection.getInstance().getConnection();

//            System.out.println("Connected!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {

            Scanner scanner = new Scanner(System.in);
            Bank bank = Bank.getInstance();
            UserService userService = UserService.getInstance();
            TransactionService transactionService = TransactionService.getInstance();
            AccountService accountService = AccountService.getInstance();
            CardService cardService = CardService.getInstance();

            System.out.println("\nWelcome to: 💲 " + bank.getName() + " 💲\n\nSWIFT Code: " + bank.getSwiftCode());
            System.out.println("\n🏦 💳 💰 🔐 🌍  📈  💼  🧾  🔄  🪙");

            while (true) {
                System.out.println("\n===== BANK MENU =====");
                System.out.println("1. Create user");
                System.out.println("2. Create account");
                System.out.println("3. Create card");
                System.out.println("4. List users");
                System.out.println("5. List accounts sorted");
                System.out.println("6. List cards");
                System.out.println("7. List transactions history");
                System.out.println("8. Deposit");
                System.out.println("9. Withdraw");
                System.out.println("10. Transfer");
                System.out.println("11. International transfer");
                System.out.println("12. Deactivate account");
                System.out.println("13. Deactivate card");
                System.out.println("14. Change account currency");
                System.out.println("15. Change interest rate for account");
                System.out.println("0. Exit\n");
                System.out.print("Choose option: ");

                int option = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (option) {

                    case 1 -> {
                        try {
                            System.out.print("Enter user name: ");
                            String name = scanner.nextLine();

                            System.out.print("Enter email: ");
                            String email = scanner.nextLine();

                            System.out.print("Enter phone: ");
                            String phone = scanner.nextLine();

                            User user = new User(name, email, phone);
                            userService.addUser(user);

                            System.out.println("User created successfully!");

                        } catch (IllegalArgumentException e) {
                            System.out.println("⚠️ " + e.getMessage());
                        }
                    }

                    case 2 -> {
                        if (userService.getAllUsers().isEmpty()) {
                            System.out.println("⚠️ No users yet!");
                            break;
                        }

                        System.out.println("Choose user index:");
                        List<User> users = userService.getAllUsers();
                        for (int i = 0; i < users.size(); i++) {
                            System.out.println(i + 1 + ": " + users.get(i).getName());
                        }
                        int userIndex = scanner.nextInt();
                        // check index out of bounds
                        if (userIndex < 1 || userIndex > users.size()) {
                            System.out.println("⚠️ Invalid user index!");
                            break;
                        }

                        System.out.println("Choose account type:");
                        System.out.println("1. Savings");
                        System.out.println("2. Checking");
                        System.out.println("3. Loan");
                        int type = scanner.nextInt();

                        User owner = users.get(userIndex - 1);

                        switch (type) {
                            case 1 -> {
                                SavingsAccount sa = new SavingsAccount(owner.getId(), Currency.RON);
                                //owner.addAccount(sa);
                                accountService.addAccount(sa);
                                System.out.println("Savings account created.");
                            }
                            case 2 -> {
                                CheckingAccount ca = new CheckingAccount(owner.getId(), Currency.EUR);
                                //owner.addAccount(ca);
                                accountService.addAccount(ca);
                                System.out.println("Checking account created.");
                            }
                            case 3 -> {
                                System.out.print("Loan amount (between 0 and 1000000): ");
                                double amount = scanner.nextDouble();
                                // not 0,00
                                if (amount <= 0) {
                                    System.out.println("⚠️ Loan amount must be greater than 0!");
                                    break;
                                }
                                LoanAccount la = new LoanAccount(owner.getId(), Currency.RON, amount, 0.05, LocalDate.now().plusYears(1));
                                //owner.addAccount(la);
                                accountService.addAccount(la);
                                System.out.println("Loan account created.");
                            }
                            default -> {
                                System.out.println("⚠️ Invalid type. Defaulting to SavingsAccount.");

                                SavingsAccount sa = new SavingsAccount(owner.getId(), Currency.RON);
                                //owner.addAccount(sa);
                                accountService.addAccount(sa);
                                System.out.println("Savings account created.");
                            }
                        }
                    }

                    case 3 -> {
                        if (userService.getAllUsers().isEmpty()) {
                            System.out.println("⚠️ No users yet!");
                            break;
                        }

                        System.out.println("Choose user index:");
                        List<User> users = userService.getAllUsers();
                        for (int i = 0; i < users.size(); i++) {
                            System.out.println(i + 1 + ": " + users.get(i).getName());
                        }
                        int userIndex = scanner.nextInt();
                        if (userIndex < 1 || userIndex > users.size()) {
                            System.out.println("⚠️ Invalid user index!");
                            break;
                        }

                        User owner = users.get(userIndex - 1);

                        // check for accounts
                        if (accountService.getOwnerAccounts(owner.getId()).isEmpty()) {
                            System.out.println("⚠️ This user has no accounts. Create an account first.");
                            break;
                        }

                        System.out.println("Choose account for the card:");
                        List<Account> accounts = accountService.getOwnerAccounts(owner.getId());
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        Account chosenAccount = accounts.get(accIndex - 1);

                        // card type
                        System.out.println("Choose card type:"); // any type for any account
                        System.out.println("1. DEBIT");
                        System.out.println("2. CREDIT");
                        System.out.println("3. VIRTUAL");
                        int typeOption = scanner.nextInt();

                        CardType type = switch (typeOption) {
                            case 1 -> CardType.DEBIT;
                            case 2 -> CardType.CREDIT;
                            case 3 -> CardType.VIRTUAL;
                            default -> {
                                System.out.println("⚠️ Invalid type. Defaulting to DEBIT.");
                                yield CardType.DEBIT;
                            }
                        };

                        try {
                            Card card = new Card(owner.getId(), chosenAccount.getIban(), type);
                            //owner.addCard(card);
                            cardService.addCard(card);
                            System.out.println("Card created.");
                        } catch (IllegalStateException e) {
                            System.out.println("⚠️ Cannot create card: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 4 -> {
                        System.out.println("=== USERS ===");

                        if (userService.getAllUsers().isEmpty()) {
                            System.out.println("⚠️ No users found. Create a user first.");
                            break;
                        }

                        System.out.println("Choose simple or detailed list:");
                        System.out.println("1. simple");
                        System.out.println("2. detailed");
                        int type = scanner.nextInt();
                        if (type != 1 && type != 2){
                            System.out.println("⚠️ Invalid type!");
                            break;
                        }
                        switch (type){
                            case 1 -> {
                                for (User u : userService.getAllUsers()) {
                                    System.out.println(u);
                                }
                            }
                            case 2 -> {
                                try {
                                    for (String s : userService.getUsersWithStats()) {
                                        System.out.println(s);
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                    case 5 -> {
                        // + so6
                        // rt
                        System.out.println("=== ACCOUNTS ===");

                        List<Account> accounts = accountService.getAllAccounts();
                        if (accounts.isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create an account first.");
                            break;
                        }

                        Collections.sort(accounts);

                        System.out.println("Choose simple or detailed list:");
                        System.out.println("1. simple");
                        System.out.println("2. detailed");
                        int type = scanner.nextInt();
                        if (type != 1 && type != 2){
                            System.out.println("⚠️ Invalid type!");
                            break;
                        }
                        switch (type){
                            case 1 -> {
                                for (Account a : accounts) {
                                    System.out.println(a);
                                }
                            }
                            case 2 -> {
                                try {
                                    for (String s : accountService.getAccountsWithCards()) {
                                        System.out.println(s);
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                    case 6 -> {
                        System.out.println("=== CARDS ===");
                        if (cardService.getAllCards().isEmpty()) {
                            System.out.println("⚠️ No cards found. Create a card first.");
                            break;
                        }

                        for (Card c : cardService.getAllCards()) {
                            System.out.println(c);
                        }
                    }

                    case 7 -> {
                        List<Account> accounts = accountService.getAllAccounts();
                        System.out.println("=== TRANSACTION HISTORY ===");
                        System.out.println("Choose account index:");
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        Account account = accounts.get(accIndex - 1);
                        List<Transaction> history = transactionService.getAccountTransactions(account.getIban());

                        if (history.isEmpty()) {
                            System.out.println("⚠️ No transactions for this account.");
                            break;
                        }

                        System.out.println("Choose simple or detailed list:");
                        System.out.println("1. simple");
                        System.out.println("2. detailed");
                        int type = scanner.nextInt();
                        if (type != 1 && type != 2){
                            System.out.println("⚠️ Invalid type!");
                            break;
                        }
                        switch (type){
                            case 1 -> {
                                for (Transaction t : history) {
                                    System.out.println(t); // uses toString()
                                }
                            }
                            case 2 -> {
                                try {
                                    for (String t : transactionService.getTransactionsWithUsers(account.getIban())) {
                                        System.out.println(t);
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                    case 8 -> {
                        List<Account> accounts = accountService.getAllAccounts();
                        if (accounts.isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create an account first.");
                            break;
                        }

                        System.out.println("Choose account index:");
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.print("Amount: ");
                        double amount = scanner.nextDouble();
                        // not 0,00
                        if (amount <= 0) {
                            System.out.println("⚠️ Deposit amount must be greater than 0!");
                            break;
                        }

                        try {
                            transactionService.deposit(accounts.get(accIndex - 1), amount);
                            System.out.println("Deposit successful.");
                        } catch (IllegalStateException e) {
                            System.out.println("⚠️ Cannot create transaction: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 9 -> {
                        if (accountService.getAllAccounts().isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create an account first.");
                            break;
                        }

                        System.out.println("Choose account index:");
                        List<Account> accounts = accountService.getAllAccounts();
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.print("Amount: ");
                        double amount = scanner.nextDouble();
                        if (amount <= 0) {
                            System.out.println("⚠️ Withdrawal amount must be greater than 0!");
                            break;
                        }

                        try {
                            transactionService.withdraw(accounts.get(accIndex - 1), amount);
                            System.out.println("Withdrawal successful.");
                        } catch (IllegalStateException e) {
                            System.out.println("⚠️ Cannot create transaction: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 10 -> {
                        if (accountService.getAllAccounts().isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create 2 accounts first.");
                            break;
                        }

                        System.out.println("Choose source account:");
                        List<Account> accounts = accountService.getAllAccounts();
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int src = scanner.nextInt();
                        if (src < 1 || src > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.println("Choose destination account:");
                        int dst = scanner.nextInt();
                        if (dst < 1 || dst > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.print("Amount: ");
                        double amount = scanner.nextDouble();
                        if (amount <= 0) {
                            System.out.println("⚠️ Transfer amount must be greater than 0!");
                            break;
                        }

                        try {
                            transactionService.transfer(accounts.get(src - 1), accounts.get(dst - 1), amount);
                            System.out.println("Transfer successful.");
                        } catch (IllegalStateException | IllegalArgumentException e) {
                            System.out.println("⚠️ Cannot create transaction: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 11 -> {
                        if (accountService.getAllAccounts().isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create 2 accounts first.");
                            break;
                        }

                        System.out.println("Choose source account:");
                        List<Account> accounts = accountService.getAllAccounts();
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int src = scanner.nextInt();
                        if (src < 1 || src > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.println("Choose destination account:");
                        int dst = scanner.nextInt();
                        if (dst < 1 || dst > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        System.out.print("Amount (in destination currency): ");
                        double amount = scanner.nextDouble();
                        if (amount <= 0) {
                            System.out.println("⚠️ International transfer amount must be greater than 0!");
                            break;
                        }

                        try {
                            transactionService.internationalTransfer(accounts.get(src - 1), accounts.get(dst - 1), amount);
                            System.out.println("International transfer successful.");
                        } catch (IllegalStateException | IllegalArgumentException e) {
                            System.out.println("⚠️ Cannot create transaction: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 12 -> {
                        List<Account> accounts = accountService.getAllAccounts();
                        if (accounts.isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create an account first.");
                            break;
                        }

                        System.out.println("Choose account to deactivate:");
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        try {
                            accountService.deactivateAccount(accounts.get(accIndex - 1));
                            System.out.println("Account deactivated.");
                        } catch (NullAccountException | InactiveAccountException e) {
                            System.out.println("⚠️ " + e.getMessage());
                        }
                    }

                    case 13 -> {
                        List<Card> cards = cardService.getAllCards();
                        if (cards.isEmpty()) {
                            System.out.println("⚠️ No cards found. Create a card first.");
                            break;
                        }

                        System.out.println("Choose card to deactivate:");
                        for (int i = 0; i < cards.size(); i++) {
                            System.out.println(i + 1 + ": " + cards.get(i).getCardNumber());
                        }
                        int cardIndex = scanner.nextInt();
                        if (cardIndex < 1 || cardIndex > cards.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }

                        cardService.deactivateCard(cards.get(cardIndex - 1));
                        System.out.println("Card deactivated.");
                    }

                    case 14 -> {
                        List<Account> accounts = accountService.getAllAccounts();
                        System.out.println("Select account:");
                        for (int i = 0; i < accounts.size(); i++) {
                            Account acc = accounts.get(i);
                            System.out.println((i + 1) + ". " + acc.getIban() + " (" + acc.getCurrency() + ")");
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }
                        Account account = accounts.get(accIndex - 1);

                        System.out.println("\nChoose new currency:");
                        System.out.println("1. RON");
                        System.out.println("2. EUR");
                        System.out.println("3. USD");
                        int currencyChoice = scanner.nextInt();
                        Currency newCurrency = null;

                        switch (currencyChoice) {
                            case 1 -> newCurrency = Currency.RON;
                            case 2 -> newCurrency = Currency.EUR;
                            case 3 -> newCurrency = Currency.USD;
                            default -> {
                                System.out.println("⚠️ Invalid currency option!");
                                break;
                            }
                        }
                        if (newCurrency == null) break;

                        try {
                            accountService.changeCurrency(account, newCurrency);
                            System.out.println("✔ Currency successfully changed!");
                            System.out.println("New balance: " + account.getBalance() + " " + newCurrency);
                        } catch (InactiveAccountException | IllegalCurrencyException e) {
                            System.out.println("⚠️ Cannot change currency: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("⚠️ Unexpected error: " + e.getMessage());
                        }
                    }

                    case 15 -> {
                        //only loan and savings have interest rates
                        if (accountService.getAllAccounts().isEmpty()) {
                            System.out.println("⚠️ No accounts found. Create an account first.");
                            break;
                        }

                        System.out.println("Choose account to change interest rate:");
                        List<Account> accounts = accountService.getAllAccounts();
                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println(i + 1 + ": " + accounts.get(i).getIban());
                        }
                        int accIndex = scanner.nextInt();
                        if (accIndex < 1 || accIndex > accounts.size()) {
                            System.out.println("⚠️ Invalid account index!");
                            break;
                        }
                        Account acc = accounts.get(accIndex - 1);
                        if (acc instanceof CheckingAccount) {
                            System.out.println("⚠️ This account type does NOT support interest rate changes.");
                            break;
                        }

                        System.out.print("Enter new interest rate (e.g. 0,05 for 5%): ");
                        double newInterestRate = scanner.nextDouble();
                        if (newInterestRate <= 0 || newInterestRate >= 0.3) {
                            System.out.println("⚠️ Invalid interest rate!");
                            break;
                        }

                        if (acc instanceof SavingsAccount sa) {
                            accountService.changeInterestRate(sa, newInterestRate);
                            System.out.println("✔ Savings account interest rate updated.");
                        } else if (acc instanceof LoanAccount la) {
                            accountService.changeLoanInterest(la, newInterestRate);
                            System.out.println("✔ Loan account interest rate updated.");
                        } else {
                            System.out.println("⚠️ This account type does not support interest rate changes.");
                        }
                    }

                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid option.");
                }
            }

        } finally {
            try {
                DatabaseConnection.getInstance().closeConnection();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
