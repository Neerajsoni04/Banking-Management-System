import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Account_Manager {
    private final Connection con;
    private final Scanner sc;

    public Account_Manager(Connection con,Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void debit(long account_no){
        System.out.print("Enter Amount : ");
        double amount = sc.nextDouble();
        System.out.print("Enter Security PIN : ");
        sc.nextLine();
        String pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_no != 0){
                String query = "select * from account where account_no = ? and pin = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1,account_no);
                preparedStatement.setString(2,pin);
                ResultSet res = preparedStatement.executeQuery();

                if (res.next()){
                    double balance = res.getDouble("balance");
                    if (amount <= balance){
                        String update_query = "update account set balance = balance - ? where account_no = ?";
                        PreparedStatement update = con.prepareStatement(update_query);
                        update.setDouble(1,amount);
                        update.setLong(2,account_no);
                        int rowAffected = update.executeUpdate();

                        if (rowAffected>0){
                            System.out.println("Rs "+ amount + " Debited Successfully!!!");
                            con.commit();
                            con.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction Failed!!!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance!!!");
                    }
                }else{
                    System.out.println("Invalid Security PIN!!!");
                }
            }else{
                System.out.println("Invalid Account Number!!!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void credit(long account_no){
        System.out.print("Enter Amount : ");
        double amount = sc.nextDouble();
        System.out.print("Enter Security PIN : ");
        sc.nextLine();
        String pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_no != 0){
                String query = "select * from account where account_no = ? and pin = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1,account_no);
                preparedStatement.setString(2,pin);
                ResultSet res = preparedStatement.executeQuery();

                if (res.next()){
                    String update_query = "update account set balance = balance + ? where account_no = ?";
                    PreparedStatement update = con.prepareStatement(update_query);
                    update.setDouble(1,amount);
                    update.setLong(2,account_no);
                    int rowAffected = update.executeUpdate();

                    if (rowAffected>0){
                        System.out.println("Rs "+ amount + " Credited Successfully!!!");
                        con.commit();
                        con.setAutoCommit(true);
                    }else{
                        System.out.println("Transaction Failed!!!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security PIN!!!");
                }
            }else{
                System.out.println("Invalid Account Number!!!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer_money(long sender_account){
        System.out.print("Enter the Receiver's account number : ");
        long receiver_account = sc.nextLong();
        System.out.print("Enter the Amount : ");
        double amount = sc.nextDouble();
        System.out.print("Enter the Security PIN : ");
        sc.nextLine();
        String pin = sc.nextLine();
        String query = "select * from Account where account_no = ? and PIN = ?";

        try{
            con.setAutoCommit(false);
            if (sender_account != 0 && receiver_account != 0){
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1,sender_account);
                preparedStatement.setString(2,pin);
                ResultSet res = preparedStatement.executeQuery();

                if (res.next()){
                    double balance = res.getDouble("balance");
                    if (amount<= balance){
                        String sender_query = "update account set balance = balance - ? where account_no = ?";
                        String receiver_query = "update account set balance = balance + ? where account_no = ?";
                        PreparedStatement sender = con.prepareStatement(sender_query);
                        PreparedStatement receiver = con.prepareStatement(receiver_query);
                        sender.setDouble(1,amount);
                        sender.setLong(2,sender_account);
                        receiver.setDouble(1,amount);
                        receiver.setLong(2,receiver_account);

                        int rowAffected1 = sender.executeUpdate();
                        int rowAffected2 = receiver.executeUpdate();

                        if (rowAffected1 > 0 && rowAffected2 > 0){
                            System.out.println("Rs " + amount + " Transferred Successfully!!!");
                            con.commit();
                            con.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction Failed!!!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("InSufficient Balance!!!");
                    }
                }else{
                    System.out.println("Invalid Security PIN!!!");
                }
            }else{
                System.out.println("Invalid Account Number!!!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter the PIN : ");
        String pin = sc.nextLine();
        try{
            String query = "select balance from Account where account_no = ? and pin = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,pin);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()){
                System.out.println("Balance : " + res.getDouble("balance"));
            }else{
                System.out.println("Invalid PIN!!!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
