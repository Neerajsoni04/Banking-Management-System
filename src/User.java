import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private final Connection con;
    private final Scanner sc;

    public User(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void registration(){
        System.out.print("Enter the Name : ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter the Email : ");
        String email = sc.nextLine();
        System.out.print("Enter th1e Password : ");
        String password = sc.nextLine();

        if (user_exist(email)){
            System.out.println("User Already exist for this Email Address!!!");
            return;
        }else{
            String query = "insert into user(Full_Name,email,password) values (?,?,?)";
            try{
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,password);
                int rowAffected = preparedStatement.executeUpdate();

                if (rowAffected>0){
                    System.out.println("User Registered Successfully!!!");
                }else{
                    System.out.println("User Registration failed!!!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String login(){
        System.out.print("Enter the email : ");
        sc.nextLine();
        String email = sc.nextLine();
        System.out.print("Enter the password : ");
        String password = sc.nextLine();
        String query = "select * from user where email = ? and password = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()){
                return email;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean user_exist(String email) {
        String query = "select * from user where email = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet res = preparedStatement.executeQuery();

            // if result get, that means user exist
            return res.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
