import java.util.*;
import java.time.*;//time.LocalDate,LocalTime for current date and time
//import java.time.LocalDateTime for both
class User {
    String username;
    String password;
    String aadhar; //identify length easily

    User(String u, String p, String a) {//Reduce repeated code
      if (a.length() != 12) {
          throw new IllegalArgumentException("Invalid Aadhar Number");
        } else {
           username = u;
           password = p;
           aadhar = a;
        }
   }
}

   class Transaction {
       String type;
       double amount;
       String note;
       LocalDateTime date;

       public Transaction(String type, double amount,String note) {//Constructor
           this.type = type;
           this.amount = amount;
           this.note=note;
           this.date = LocalDateTime.now();
       }

       public String info() {//method

           return date + " | " + type + " | " + amount+" | "+ note;
       }
   }
class Account {
    String accNo;
    double balance;
    List<Transaction> transactions = new ArrayList<>();//stores full transaction

    public Account(String accNo) {//constructor
        this.accNo = accNo;
        this.balance = 500; // minimum balance
    }

    public void deposit(double amt) {
        if (amt <= 0) {
            System.out.println("Invalid amount");
            return;
        }
        balance += amt;
        transactions.add(new Transaction("DEPOSIT", amt,"Self"));
    }

    public void withdraw(double amt) {
        if (amt <= 0) {
            System.out.println("Invalid amount");
            return;
        }
        if (balance - amt < 500) {
            System.out.println("Minimum balance must be 500");
            return;
        }
        balance -= amt;
        transactions.add(new Transaction("WITHDRAW", amt,"Self"));
    }

    public void transfer(Account toAcc, double amt) {
        if (balance - amt < 500) {
            System.out.println("Minimum balance must be 500");
            return;
        }

        this.balance -= amt;//deduct money from sender
        toAcc.balance += amt;//add money to receiver(gets money)

        transactions.add(new Transaction("TRANSFER_OUT", amt, "To " + toAcc.accNo));
        toAcc.transactions.add(new Transaction("TRANSFER_IN", amt, "From " + this.accNo));

        System.out.println("Transfer successful");
    }

    public void showBalance() {

        System.out.println("Balance: " + balance);
    }

    public void miniStatement() {
        if (transactions.size() == 0) {
            System.out.println("No transactions yet");
            return;
        }

        System.out.println("Last 10 Transactions:");

        int start = Math.max(0, transactions.size() - 10); //if 15 size-  15-10=5
        //(0,5)=5 so it starts with 5th index
        for (int i = start; i < transactions.size(); i++) {
            System.out.println(transactions.get(i).info());
        }
    }

    public void showAllTransactions() {//transaction history
        if (transactions.size() == 0) {
            System.out.println("No transactions yet");
            return;
        }

        System.out.println("All Transactions:");

        for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i).info());
        }
    }


}

    class SavingsAccount extends Account {//subcls inherits from parent cls
        double interest = 0.04;

        SavingsAccount(String a) {//constructor-setup
            super(a);//call parent cls constructor
        }

        void addInterest() {//method-behaviour
            double interestrate = balance * interest;
            balance = balance + interestrate;
        }
    }

public class BankSystem {

    static Scanner sc = new Scanner(System.in);

    static Map<String, User> users = new HashMap<>();
    static Map<String, Account> accounts = new HashMap<>();
    //final-value cannot be changed
    static final String ADMIN_USER = "admin";
    static final String ADMIN_PASS = "admin123";

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n1. Signup\n2. Login\n3. Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    signup();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }
    static void signup() {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Aadhar (12 digits): ");
        String aadhar = sc.nextLine();

        try {
            User user = new User(username, password, aadhar);//call user constructor
            users.put(username, user);

            String accNo = "ACC" + (accounts.size() + 1);
            Account acc = new SavingsAccount(accNo);
            accounts.put(username, acc);

            System.out.println("Signup successful. Account No: " + accNo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        // Admin login
        if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
            System.out.println("Admin login successful");
            adminMenu();
        }
        // User login
        else if (users.containsKey(username) && users.get(username).password.equals(password)) {
            System.out.println("Login successful");
            userMenu(username);
        } else {
            System.out.println("Invalid credentials");
        }
    }
    static void userMenu(String username) {

        Account acc = accounts.get(username);

        while (true) {
            System.out.println("\n1.Deposit 2.Withdraw 3.Transfer 4.Balance 5.Mini Statement 6.All Transactions 7.Exit");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Amount: ");
                    acc.deposit(sc.nextDouble());
                    break;

                case 2:
                    System.out.print("Amount: ");
                    acc.withdraw(sc.nextDouble());
                    break;

                case 3:
                    sc.nextLine();//clear buffer then wait for real input
                    System.out.print("Transfer to username: ");
                    String toUser = sc.nextLine();

                    if (!accounts.containsKey(toUser)) {
                        System.out.println("User not found");
                        break;
                    }

                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    acc.transfer(accounts.get(toUser), amt);
                    break;

                case 4:
                    acc.showBalance();
                    break;

                case 5:
                    acc.miniStatement();
                    break;

                case 6:
                    acc.showAllTransactions();
                    break;

                case 7:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    static void adminMenu() {

        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. View Users\n2. View Accounts\n3. Exit");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.println("Users:");
                    for (String u : users.keySet()) {
                        System.out.println(u);
                    }
                    break;

                case 2:
                    System.out.println("Accounts:");
                    for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                        //entry contains key+value
                        System.out.println("User: " + entry.getKey() +
                                " | AccNo: " + entry.getValue().accNo +
                                " | Balance: " + entry.getValue().balance);
                    }
                    break;

                case 3:
                    return;
            }
        }
    }
}
