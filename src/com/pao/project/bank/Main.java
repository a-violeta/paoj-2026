package com.pao.project.bank;

import com.pao.project.bank.exception.InactiveAccountException;
import com.pao.project.bank.exception.NullAccountException;
import com.pao.project.bank.model.*;
import com.pao.project.bank.model.account.*;
import com.pao.project.bank.model.transaction.Transaction;
import com.pao.project.bank.service.AccountService;
import com.pao.project.bank.service.UserService;
import com.pao.project.bank.service.CardService;
import com.pao.project.bank.service.TransactionService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Bank bank = Bank.getInstance();
        UserService userService = UserService.getInstance();
        TransactionService transactionService = TransactionService.getInstance();
        AccountService accountService = AccountService.getInstance();
        CardService cardService = CardService.getInstance();

        System.out.println("\nWelcome to " + bank.getName());
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
                    if (userService.getAllUsers().isEmpty()){
                        System.out.println("⚠️ No users yet!");
                        break;
                    }

                    System.out.println("Choose user index:");
                    List<User> users = userService.getAllUsers();
                    for (int i = 0; i < users.size(); i++) {
                        System.out.println(i+1 + ": " + users.get(i).getName());
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

                    User owner = users.get(userIndex-1);

                    switch (type) {
                        case 1 -> {
                            SavingsAccount sa = new SavingsAccount(owner, Currency.RON);
                            owner.addAccount(sa);
                            accountService.addAccount(sa);
                            System.out.println("Savings account created.");
                        }
                        case 2 -> {
                            CheckingAccount ca = new CheckingAccount(owner, Currency.RON);
                            owner.addAccount(ca);
                            accountService.addAccount(ca);
                            System.out.println("Checking account created.");
                        }
                        case 3 -> {
                            System.out.print("Loan amount: ");
                            double amount = scanner.nextDouble();
                            // not 0,00
                            if (amount <= 0) {
                                System.out.println("⚠️ Loan amount must be greater than 0!");
                                break;
                            }
                            LoanAccount la = new LoanAccount(owner, Currency.RON, amount, 0.05, LocalDate.now().plusYears(1));
                            owner.addAccount(la);
                            accountService.addAccount(la);
                            System.out.println("Loan account created.");
                        }
                        default -> {
                            System.out.println("⚠️ Invalid type. Defaulting to SavingsAccount.");

                            SavingsAccount sa = new SavingsAccount(owner, Currency.RON);
                            owner.addAccount(sa);
                            accountService.addAccount(sa);
                            System.out.println("Savings account created.");
                        }
                    }
                }

                case 3 -> {
                    if(userService.getAllUsers().isEmpty()){
                        System.out.println("⚠️ No users yet!");
                        break;
                    }

                    System.out.println("Choose user index:");
                    List<User> users = userService.getAllUsers();
                    for (int i = 0; i < users.size(); i++) {
                        System.out.println(i+1 + ": " + users.get(i).getName());
                    }
                    int userIndex = scanner.nextInt();

                    if (userIndex < 1 || userIndex > users.size()) {
                        System.out.println("⚠️ Invalid user index!");
                        break;
                    }

                    User owner = users.get(userIndex-1);

                    // check for accounts
                    if (owner.getAccounts().isEmpty()) {
                        System.out.println("⚠️ This user has no accounts. Create an account first.");
                        break;
                    }

                    // choose account
                    System.out.println("Choose account for the card:");
                    List<Account> accounts = owner.getAccounts();
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
                    }
                    int accIndex = scanner.nextInt();
                    if (accIndex < 1 || accIndex > accounts.size()) {
                        System.out.println("⚠️ Invalid account index!");
                        break;
                    }

                    Account chosenAccount = accounts.get(accIndex-1);

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

                    Card card = new Card(owner, chosenAccount, type);
                    owner.addCard(card);
                    cardService.addCard(card);

                    System.out.println("Card created.");
                }

                case 4 -> {
                    System.out.println("=== USERS ===");
                    if (userService.getAllUsers().isEmpty()) {
                        System.out.println("⚠️ No users found. Create a user first.");
                        break;
                    }
                    for (User u : userService.getAllUsers()) {
                        System.out.println(u);
                    }
                }

                case 5 -> {
                    // + sort
                    System.out.println("=== ACCOUNTS ===");

                    List<Account> accounts = accountService.getAllAccounts();

                    if (accounts.isEmpty()){
                        System.out.println("⚠️ No accounts found. Create an account first.");
                        break;
                    }

                    Collections.sort(accounts);

                    for (Account a : accounts) {
                        System.out.println(a);
                    }
                }

                case 6 -> {
                    System.out.println("=== CARDS ===");
                    if (cardService.getAllCards().isEmpty()){
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
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
                    }
                    int accIndex = scanner.nextInt();
                    if (accIndex < 1 || accIndex > accounts.size()) {
                        System.out.println("⚠️ Invalid account index!");
                        break;
                    }

                    Account account = accounts.get(accIndex - 1);

                    List<Transaction> history = account.getTransactionHistory();

                    if (history.isEmpty()) {
                        System.out.println("⚠️ No transactions for this account.");
                        break;
                    }

                    for (Transaction t : history) {
                        System.out.println(t); // uses toString()
                    }
                }

                case 8 -> {
                    List<Account> accounts = accountService.getAllAccounts();
                    if(accounts.isEmpty()){
                        System.out.println("⚠️ No accounts found. Create an account first.");
                        break;
                    }

                    System.out.println("Choose account index:");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
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

                    transactionService.deposit(accounts.get(accIndex-1), amount);
                    System.out.println("Deposit successful.");
                }

                case 9 -> {
                    if(accountService.getAllAccounts().isEmpty()){
                        System.out.println("⚠️ No accounts found. Create an account first.");
                        break;
                    }

                    System.out.println("Choose account index:");
                    List<Account> accounts = accountService.getAllAccounts();
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
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

                    transactionService.withdraw(accounts.get(accIndex-1), amount);
                    System.out.println("Withdrawal successful.");
                }

                case 10 -> {
                    if(accountService.getAllAccounts().isEmpty()){
                        System.out.println("⚠️ No accounts found. Create 2 accounts first.");
                        break;
                    }

                    System.out.println("Choose source account:");
                    List<Account> accounts = accountService.getAllAccounts();
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
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

                    transactionService.transfer(accounts.get(src-1), accounts.get(dst-1), amount);
                    System.out.println("Transfer successful.");
                }

                case 11 -> {
                    if(accountService.getAllAccounts().isEmpty()){
                        System.out.println("⚠️ No accounts found. Create 2 accounts first.");
                        break;
                    }

                    System.out.println("Choose source account:");
                    List<Account> accounts = accountService.getAllAccounts();
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
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

                    transactionService.internationalTransfer(accounts.get(src-1), accounts.get(dst-1), amount);
                    System.out.println("International transfer successful.");
                }

                case 12 -> {
                    List<Account> accounts = accountService.getAllAccounts();
                    if(accounts.isEmpty()){
                        System.out.println("⚠️ No accounts found. Create an account first.");
                        break;
                    }

                    System.out.println("Choose account to deactivate:");
                    for (int i = 0; i < accounts.size(); i++) {
                        System.out.println(i+1 + ": " + accounts.get(i).getIban());
                    }
                    int accIndex = scanner.nextInt();
                    if (accIndex < 1 || accIndex > accounts.size()) {
                        System.out.println("⚠️ Invalid account index!");
                        break;
                    }

                    try {
                        accountService.deactivateAccount(accounts.get(accIndex-1));
                    } catch (NullAccountException | InactiveAccountException e) {
                        System.out.println("⚠️ " + e.getMessage());
                    }

                    System.out.println("Account deactivated.");
                }

                case 13 -> {
                    List<Card> cards = cardService.getAllCards();
                    if(cards.isEmpty()){
                        System.out.println("⚠️ No cards found. Create a card first.");
                        break;
                    }

                    System.out.println("Choose card to deactivate:");
                    for (int i = 0; i < cards.size(); i++) {
                        System.out.println(i+1 + ": " + cards.get(i).getCardNumber());
                    }
                    int cardIndex = scanner.nextInt();
                    if (cardIndex < 1 || cardIndex > cards.size()) {
                        System.out.println("⚠️ Invalid account index!");
                        break;
                    }

                    cardService.deactivateCard(cards.get(cardIndex-1));
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

                    account.changeCurrency(newCurrency);

                    System.out.println("✔ Currency successfully changed!");
                    System.out.println("New balance: " + account.getBalance() + " " + newCurrency);
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
    }
}
