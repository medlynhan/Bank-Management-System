package solutions;
import java.sql.*;
import java.util.*;

public class BankingApp {

    public static void printMenu(){
        System.out.println("*** HI DEAR GET STARTED ***");
        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Max/Min");
        System.out.println("4. Exit");
        enterChoice();
    }
    public static void enterChoice(){
        System.out.println("Enter your choice: ");
    }

	public static void headerOfFrontPage() {
		System.out.println("|^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^|");
		System.out.println("|                                                         |");
		System.out.println("|                                                         |");
		System.out.println("|          █▓▒▒░░░𝓦 𝓔 𝓛 𝓒 𝓞 𝓜 𝓔   𝓣 𝓞 ░░░▒▒▓█             |");
		System.out.println("|█▓▒▒░░░B A N K   M A N A G E M E N T   S Y S T E M░░░▒▒▓█|");
		System.out.println("|                                                         |");
		System.out.println("|                                                         |");
		System.out.println("|=========================================================|");
	}
    
	private static final  String connectUrl="jdbc:sqlserver://dEVICEnAME\\SQLEXPRESS;DatabaseName=BankManagementSystem;IntegratedSecurity=true;encrypt=true;trustServerCertificate=true";
    public static void main(String[] args) {
    	headerOfFrontPage();
        try{
            Connection connection = DriverManager.getConnection(connectUrl);
            Scanner scanner =  new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            while(true){
                printMenu();
                int choice1 = scanner.nextInt();
                
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        handleLogin(scanner, user, accounts, accountManager);
                        break;
                    case 3:
                        handleMaxMin(scanner, accountManager);
                        break;
                    case 4:
                    	 System.out.println("THANK YOU FOR USING AJK BANKING SYSTEM!!!");
                         System.out.println("Exiting System!");
                         return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void handleLogin(Scanner scanner, User user, Accounts accounts, AccountManager accountManager) {
        String email = user.login();
        if (email != null) {
            System.out.println("\nUser Logged In!");

            long account_number = checkAccountExist(scanner, accounts, email);
            if (account_number == 0) {
                System.out.println("There is no account.");
                return;
            }
            AccountMenu menu = new AccountMenu(scanner, accountManager, account_number);
            menu.show();
        } else {
            System.out.println("Invalid Email or Password!");
        }
    }

    private static long checkAccountExist(Scanner scanner, Accounts accounts, String email) {
        if (!accounts.account_exist(email)) {
            System.out.println("\n1. Open a new Bank Account");
            System.out.println("2. Exit");
            enterChoice();
            int subChoice = scanner.nextInt();
            if(subChoice == 1){
                long account_number = accounts.open_account(email);
                System.out.println("Account Created Successfully");
                System.out.println("Your Account Number is: " + account_number);
                return account_number;
            } else {
                return 0; 
            }
        }    
        return accounts.getAccount_number(email);
    }

    private static void handleMaxMin(Scanner scanner, AccountManager accountManager) {
        System.out.println("1. MAX");
        System.out.println("2. MIN");
        enterChoice();
        int choice =scanner.nextInt();
        if(choice ==1) {
        	accountManager.getMaxAccountBalance();
        }else if(choice ==2) {
        	accountManager.getMinAccountBalance();
        }else {
            System.out.println("invalid choice for MAN/MIN");
        }
    }
}
