import java.sql.*;
import java.util.Scanner;

public class Buses {

    private final Connection con;
    private final Scanner sc;


//    private int booking_id ,seats_booked ;
//    private String user_id, bus_id, from_city, to_city ;

    public Buses(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;


    }

    public void availableBuses() {
        String query = "SELECT * FROM buses";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("------------------------");
            while (rs.next()) {
                String bus_number = rs.getString("bus_number");
                System.out.println("BUS NUMBER \t\t: " + bus_number);
                System.out.println("FROM CITY \t\t: " + rs.getString("from_city"));
                System.out.println("TO CITY \t\t: " + rs.getString("to_city"));
                int available_Seats = rs.getInt("available_seats");
                System.out.println("AVAILABLE SEATS : " + available_Seats);

                if (available_Seats == 0) {
                    System.out.println("------------------------");
                    System.out.println("Seats are full for bus number " + bus_number + " !!");
                }
                System.out.println("------------------------");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void bookTicket() {

        String query = "INSERT INTO bookings(user_id,bus_id,seats_booked,from_city,to_city)VALUES( ?, ?, ?, ?, ?)";

        System.out.print("Enter username : ");
        String user_id = sc.nextLine();
        System.out.print("Enter bus number : ");
        String bus_id = sc.nextLine();
        System.out.print("Enter seats you have to book : ");
        int seats_booked = sc.nextInt();
        sc.nextLine();
        System.out.print("Book from city : ");
        String from_city = sc.nextLine();
        System.out.print("To city : ");
        String to_city = sc.nextLine();

        try {
            con.setAutoCommit(false);
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, user_id);
            st.setString(2, bus_id);
            st.setInt(3, seats_booked);
            st.setString(4, from_city);
            st.setString(5, to_city);

            int rowsAff = st.executeUpdate();
            if (rowsAff > 0) {
                System.out.println("Ticket booked successfully :) ");
                con.commit();
                con.setAutoCommit(true);

                String query_update = "UPDATE buses SET available_seats = available_Seats - ? WHERE bus_number = ?";
                try {
                    PreparedStatement st2 = con.prepareStatement(query_update);
                    st2.setInt(1, seats_booked);
                    st2.setString(2, bus_id);
                    int rowsAff2 = st2.executeUpdate();
                    if (rowsAff2 > 0) {
                        System.out.println();
                    } else {
                        return;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Booking failed !!");
                con.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Booking failed !!");
        }
    }


    public void checkBookings() {

        System.out.println("Enter username : ");
        String user_id = sc.nextLine();

        if (isBookingExists(user_id)) {
            String query = "SELECT * FROM bookings WHERE user_id = ?";
            try {
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, user_id);
                ResultSet rs = st.executeQuery();
                System.out.println("=================================");
                while (rs.next()) {
                    System.out.println("BOOKING ID \t\t: " + rs.getInt("booking_id"));
                    System.out.println("USER ID \t\t: " + rs.getString("user_id"));
                    System.out.println("BUS NUMBER \t\t: " + rs.getString("bus_id"));
                    System.out.println("SEATS BOOKED \t: " + rs.getInt("seats_booked"));
//                    System.out.println("BOOKING TIME : " + rs.getTimestamp("booking_time"));
                    System.out.println("FROM CITY \t\t: " + rs.getString("from_city"));
                    System.out.println("TO CITY \t\t: " + rs.getString("to_city"));
                    System.out.println("=================================");
                }
            } catch (SQLException e) {
                System.out.println("Incorrect username or password !");
            }
        } else {
            System.out.println("Incorrect username  !!");
        }
    }

    public void exit() throws InterruptedException {
        System.out.println("THANK YOU FOR USING BUS BOOKING SYSTEM :)");
        System.out.print("Exiting System");
        for (int i = 1; i <= 3; i++) {
            Thread.sleep(500);
            System.out.print(".");
        }
    }

    public boolean isBookingExists(String user_id) {
        String query = "SELECT * FROM bookings WHERE user_id = ?";
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, user_id);
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

}
