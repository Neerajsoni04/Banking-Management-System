import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter. +
public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/Banking_db";
    private static final String username = "root";
    private static final String password = "@Dilip18";


    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try{
            Scanner sc = new Scanner(System.in);
            Connection con = DriverManager.getConnection(url,username,password);
            Account account = new Account(con,sc);
            User user = new User(con,sc);
            Account_Manager account_manager = new Account_Manager(con,sc);

            String email;
            long account_no;
            while (true){
                System.out.println("**** WELCOME TO BANKING SYSTEM ****");

                System.out.println("1. Registration");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        user.registration();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null){
                            System.out.println("User Logged In!!");
                            if (!account.account_Exist(email)){ // It means New User
                                System.out.println("1. Open a new Account");
                                System.out.println("2. Exit.");
                                System.out.print("Enter Your Choice : ");
                                if (sc.nextInt() == 1){
                                    account_no = account.openAccount(email);
                                    System.out.println("Account Created Successfully!! ");
                                }else{
                                    break;
                                }
                            }

                            account_no = account.getAccountNo(email);
                            int choice2 = 0;
                            while(choice2  != 5){
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter Your Choice : ");
                                choice2 = sc.nextInt();
                                switch (choice2){
                                    case 1:
                                        account_manager.debit(account_no);
                                        break;
                                    case 2:
                                        account_manager.credit(account_no);
                                        break;
                                    case 3:
                                        account_manager.transfer_money(account_no);
                                        break;
                                    case 4:
                                        account_manager.getBalance(account_no);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid Choice!! Try Again...");
                                }
                            }
                        }else {
                            System.out.println("Incorrect Email or Password!!");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR VISITING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Invalid Choice!! Try again");
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}