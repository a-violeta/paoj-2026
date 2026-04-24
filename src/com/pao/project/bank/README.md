# Bank-Application
OOP project in Java for PAOJ class

## Description

### Actions
1. list all accounts ordered by iban
2. list all cards
3. list bank's users
4. list transaction history
4. deposit in account
5. withdraw from account
6. transfer from account to account
7. international transfer
7. deactivate account
8. deactivate card
9. change currency for account
10. change interest rate for account

### Entities
1. `Bank` singleton class
2. `User` represents the person owning accounts, cards and making transactions
3. `Accounts` can be one of 3 types: `SavingsAccount`, `LoanAccount`, `CheckingAccount`
4. `SavingsAccount` has an interest rate
5. `LoanAccount` has a due date
6. `CheckingAccount` has an overdraft limit
7. `Transactions` are done to `Accounts` and can be one of 4 types: `Deposit`, `Withdrawal`, `Transfer`, `Deposit`
9. `Deposit` involves 1 account
10. `Withdrawal` involves 1 account
10. `Transfer` involves 2 accounts
11. `International Transfer` involves 2 accounts of different currencies
12. `Card` is the object owned by a `User` and has a set `Currency`

### Notes about implementation

Methods `toString()`, `equals()` and `hashCode()` cand be found in `Account` and `User` classes

2-level inheriting: `Transaction` -> `Transfer` -> `InternationalTransfer`

Abstract classes: `Account`, `Transaction`

`ImmutableIdentifier` for `cardNumber` in `Card`

Only 2 custom exceptions in `exceptions`: `InactiveAccountException`, `NullAccountException`

Collections used:
- `List` in: `Account`, `User`, `AccountService`, `CardService`, `TransactionService`, `UserService`
- `Map` in: `AccountService`

Sorted collection: `List<Account>` sorted in `Main` menu option `5`, using `Comllections.sort`

4 singleton services classes: `AccountService`, `CardService`, `TransactionService`, `UserService` each implementing add, find, delete and list

`Main` has a menu with 15 options:
1. `Create user`
2. `Create account`
3. `Create card`
4. `List users`
5. `List accounts sorted`
6. `List cards`
7. `List transactions history`
8. `Deposit`
9. `Withdraw`
10. `Transfer`
11. `International transfer`
12. `Deactivate account`
13. `Deactivate card`
14. `Change account currency`
15. `Change interest rate for account`