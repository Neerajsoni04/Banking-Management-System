import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Account {
    private final Connection con;
    private final Scanner sc;

    public Account(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long openAccount(String email){
        if (!account_Exist(email)){
            String open_Account_query = "insert into Account (Account_no,Full_Name,email,balance,pin)" +
                    "values (?,?,?,?,?)";
            System.out.print("Enter the Full Name : ");
            sc.nextLine();
            String name = sc.nextLine();
            System.out.print("Enter the initial balance : ");
            double balance = sc.nextDouble();
            System.out.print("Enter the Security PIN : ");
            sc.nextLine();
            String pin = sc.nextLine();
            long account_no = generateAccount_no();

            try{
                PreparedStatement preparedStatement = con.prepareStatement(open_Account_query);
                preparedStatement.setLong(1,account_no);
                preparedStatement.setString(2,name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,pin);
                int rowAffected = preparedStatement.executeUpdate();

                if (rowAffected>0){
                    return account_no;
                }
                throw new RuntimeException("Account Creation Failed!!!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account Already Exist!!!");
    }

    private long generateAccount_no(){
        String query = "select Account_no from Account order by account_no desc  limit 1";
        try{
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);

            if (res.next()){
                return res.getLong("Account_no") + 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 1000100;
    }

    public long getAccountNo(String email){
        String query = "select * from Account where email = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()){
                return res.getLong("Account_no");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number Doesn't Exist");
    }

    public boolean account_Exist(String email){
        String query = "select * from Account where email= ?;";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()){
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
