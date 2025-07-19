
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;


    public User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void register() {

        System.out.print("Enter name : ");
        String name = sc.nextLine();
        System.out.print("Set username : ");
        String username = sc.nextLine();
        if (isUsernameExists(username)) {
            System.out.println("Username already taken! Please choose a different one");
            return;
        }
        System.out.print("Set password : ");
        String password = sc.nextLine();

        try {

            String query = "INSERT INTO users(name,username,password) VALUES (?, ?, ?)";
            con.setAutoCommit(false);
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, name);
            st.setString(2, username);
            st.setString(3, password);
            int rowsAff = st.executeUpdate();

            if (rowsAff > 0) {
                System.out.println("Registration successfully !");
                con.commit();
                con.setAutoCommit(true);
            } else {
                System.out.println("Registration failed !");
                con.rollback();
                con.setAutoCommit(true);
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Technical issue try again !");
        }
    }

    public boolean isUsernameExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void login() {
        System.out.print("Enter username : ");
        String username = sc.nextLine();
        System.out.print("Enter password : ");
        String password = sc.nextLine();
        if (isUsernameExists(username)) {
            String login_query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try {
                PreparedStatement st = con.prepareStatement(login_query);
                st.setString(1, username);
                st.setString(2, password);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    System.out.println("User Logged-in successfully !\n");

                } else {
                    System.out.println("Incorrect password !!");
                }

            } catch (SQLException e) {
                System.out.println("Login Failed !!");
            }


        } else {
            System.out.println("Username not exists !!");
        }


    }
}
