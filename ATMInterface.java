import java.math.BigDecimal;
import java.util.*;

class ATMInterface {
    private Scanner in = new Scanner(System.in);
    private HashMap<String, Account> accounts = new HashMap<String, Account>();

    ATMInterface() {
        Account user1 = new Account("user1", "1");
        Account user2 = new Account("user2", "2");

        accounts.put("1", user1);
        accounts.put("2", user2);
        System.out.println("Accounts for demo use:\nuid\tpass\n1\t1\n2\t2\n");

        while (true) {
            System.out.println("Login:");
            System.out.print("Enter uid:");
            String uid = in.nextLine();
            System.out.print("Enter pin:");
            String pin = in.nextLine();

            if (validate(uid, pin)) {
                new Transaction(uid);
            } else {
                System.out.println("Wrong uid/pin!");
            }
        }
    }

    public boolean validate(String uid, String pin) {
        if (accounts.containsKey(uid)) {
            return pin.equals(accounts.get(uid).pin);
        } else {
            return false;
        }
    }

    class Account {
        private String name;
        private String pin;
        private BigDecimal balance;
        private List<String> transactions;

        Account(String name, String pin) {
            this.name = name;
            this.pin = pin;
            this.balance = new BigDecimal("1000.00");
            this.transactions = new LinkedList<>();
        }

        private void history() {
            System.out.println("Transactions:");
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }

        private void deposit(BigDecimal amount) {

            this.balance = this.balance.add(amount);

        }

        private boolean withdraw(BigDecimal amount) {

            if (amount.compareTo(this.balance) > 0) {
                return false;
            }

            this.balance = this.balance.subtract(amount);
            return true;
        }

        private void balance() {
            System.out.println("Balance: " + this.balance);
        }

    }

    private class Transaction {

        private int transactionId;
        private String mainId;
        private String otherId;
        private int choice;
        private BigDecimal amount;
        private String description;
        private Account main;
        private Account other;

        Transaction(String uid) {
            System.out.println("Transaction initiated!");
            this.mainId = uid;
            main = accounts.get(uid);
            description = "" + (int) (java.lang.Math.random() * 10000);
            System.out.print(
                    "Menu:\n1.Deposit\n2.Withdraw\n3.Transfer\n4.Balance Enquiry\n5.Transaction History\n6.Exit\nEnter your choice:");

            choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1:
                    deposit(main, description);
                    break;
                case 2:
                    withdraw(main, description);
                    break;
                case 3:
                    transfer();
                    break;
                case 4:
                    main.balance();
                    break;
                case 5:
                    main.history();
                    break;
                case 6:
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice!");
            }
        }

        private void deposit(Account main, String description) {
            System.out.print("Enter amount:");
            while (!in.hasNextBigDecimal()) {
                System.out.println("Invalid input! Please enter a valid amount:");
                in.next();
            }
            amount = in.nextBigDecimal();
            in.nextLine();
            description += "-\tDeposited Rs." + amount;
            main.transactions.add(description);
            main.deposit(amount);

        }

        private void transfer() {

            System.out.print("Enter recepient Uid:");
            otherId = in.next();
            in.nextLine();
            other = accounts.get(otherId);
            System.out.print("Enter amount:");
            while (!in.hasNextBigDecimal()) {
                in.next();
                System.out.println("Enter valid amount!");
            }
            amount = in.nextBigDecimal();
            in.nextLine();
            boolean withdrawStatus = main.withdraw(amount);
            if (withdrawStatus == false) {
                description += ("-\tTransfer Failed due to insufficient funds!");
                main.transactions.add(description);
                main.deposit(amount);
            } else if (other == null) {
                description += "-\t Transfer failed due to invalid recepient account id!";
                main.transactions.add(description);
                main.deposit(amount);
            } else {
                other.deposit(amount);
                main.transactions.add(description + "-\tTransferred Rs." + amount + " to " + otherId);
                other.transactions.add(description + "- Received Rs." + amount + " from " + mainId);
            }

        }

        private void withdraw(Account main, String description) {

            System.out.print("Enter amount:");
            while (!in.hasNextBigDecimal()) {
                System.out.println("Invalid input! Please enter a valid amount:");
                in.next();
            }
            amount = in.nextBigDecimal();
            in.nextLine();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Invalid amount! Please enter a positive value.");
                return;
            }
            if (main.withdraw(amount) == false)
                System.out.println("Insufficient balance!");
            else {
                description += "-\tWithdrawn Rs." + amount;
                main.transactions.add(description);
            }
        }

    }

    public static void main(String[] args) {
        new ATMInterface();
    }
}
