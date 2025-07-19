import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BookingApp {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/booking_system";
    private static final String username = "root";
    private static final String password = "Kuldeep@1234";


    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Buses buses = new Buses(con, sc);


            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            try {
                System.out.println("WEL-COME TO BUS BOOKING SYSTEM :)");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int choice = 0 ;
                while ( choice <= 4 ) {

                    System.out.println("Enter your choice ");
                    choice =  sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            user.register();
                            break;
                        case 2:
                            user.login();
                            int choice2 = 0;
                            while (choice2 <= 3) {
                                System.out.println("1. AVAILABLE BUSES ");
                                System.out.println("2. BOOK TICKET ");
                                System.out.println("3. CHECK RESERVATIONS");
                                System.out.println("4. EXIT\n");
                                System.out.print("Enter your choice : ");
                                choice2 = sc.nextInt();
                                sc.nextLine();
                                switch (choice2) {
                                    case 1:
                                        buses.availableBuses();
                                        break;

                                    case 2:
                                        buses.bookTicket();
                                        break;

                                    case 3:
                                        buses.checkBookings();
                                        break;

                                    case 4:
                                        buses.exit();
                                        return;
                                }
                            }

                        case 3:
                            buses.exit();
                            return;

                        default:
                            System.out.println("Invalid input !!");
                            break;

                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
