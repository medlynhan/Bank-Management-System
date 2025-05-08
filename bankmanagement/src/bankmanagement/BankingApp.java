package bankmanagement;
import java.sql.*;
import java.util.*;
/*
 * Code Smell 	: Long Method
 * Reason		: Terlalu banyak method pada main
 * Solution		: Move Method
 * 
 * Code Smell 	: Large Class
 * Reason 		: Teralalu banyak tanggung jawab pada satu class
 * Solution 	: Extract Class 
 * 
 * Code Smell 	: Swtich Statements
 * Reason 		: Terlalu banyak logika switch case(case, if-else)
 * Solution		: Extract Method
 * 
 * Code Smell 	: Duplicate Code
 * Reason 		: Terdapat code yang digunakan lebih dari sekali yaitu untuk input choice
 * Solution		: Extract Method
 * 
 * */
public class BankingApp {
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

            String email;
            long account_number;

            while(true){
                System.out.println("*** HI DEAR GET STARTED ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Max/Min");
                System.out.println("4. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }

                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Email or Password!");break;
                        }
                    case 3:
                    	System.out.println("1. MAX");
                    	System.out.println("2. MIN");
                    	int ch=scanner.nextInt();
                    	if(ch==1) {
                    		accountManager.max();break;
                    	}else if(ch==2) {
                    		accountManager.min();break;
                    	}else {
                    		System.out.println("invalid choice for MAN/MIN");break;
                    	}
                       
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
}
