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
            UserSessionService userService = new UserSessionService(scanner, user, accounts, accountManager);

            while(true){
                printMenu();
                int choice1 = scanner.nextInt();
                
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        userService.handleLogin();
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
