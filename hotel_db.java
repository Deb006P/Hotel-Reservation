import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class hotel_db {
    private static int reservationId;

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        final String url = "jdbc:mysql://localhost:3306/hotel_db";
        final  String username = "root";
        final String password = "Poiu0987#";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection established");

            while(true) {
                System.out.println();
                System.out.println("WELCOME TO DEV HOTEL MANGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1.Reserve a room");
                System.out.println("2.View  Reservation");
                System.out.println("3.Get Guest room Number");
                System.out.println("4.Update  reservation");
                System.out.println("5.Delete  resertvation");
                System.out.println("0.Exit");
                System.out.println("CHOOSE AN OPTION AVOVE THEM");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(con,sc);
                        break;
                    case 2:
                        viewReservation(con);
                        break;
                    case 3:
                        getRoomNo(con,sc);
                        break;
                    case 4:
                        updateReservation(con,sc);
                        break;
                    case 5:
                        delteReservation(con,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                }
            }
        }catch(SQLException | InterruptedException e){
            System.out.println(e.getMessage());
        }

        }
    private static void reserveRoom(Connection con, Scanner sc){
        try {
            System.out.println("Enter Guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter Guest room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter Contact Number: ");
            int contactNumber = sc.nextInt();
            System.out.println("Enter Guest Address: ");
            String guestAddress = sc.next();
            String query = "INSERT INTO RESERVATION (guest_name,room_number,contact_number,Address)" + " VALUES('" + guestName + "'," + roomNumber + "," + contactNumber + ",'" + guestAddress + "')";
            try (Statement stmt = con.createStatement()){
              int EffectedRows = stmt.executeUpdate(query);
              if(EffectedRows > 0){
                  System.out.println("Reservation has been reserved.");
              }else{
                  System.out.println("Reservation failed!!");
              }
            }


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
   private static void viewReservation(Connection con) throws SQLException{
       String query = "SELECT * FROM RESERVATION";
        try(Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+----------------+--------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Address        |   Reservation Date   ");
            System.out.println("+----------------+-----------------+---------------+----------------------+----------------+--------------------+");

            while(rs.next()){
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                int contactNumber = rs.getInt("contact_number");
                String guestAddress = rs.getString("Address");
                String reservationDate = rs.getString("reservation_date");
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-13s   | %-20s\n",
                        reservationId, guestName, roomNumber, contactNumber,guestAddress, reservationDate);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+--------------+---------------------+");
        }
   }

    private static void getRoomNo(Connection con ,Scanner sc) throws SQLException{
        try {
            System.out.println("Enter Guest Id: ");
            int id = sc.nextInt();
            String query = "SELECT  room_number FROM RESERVATION WHERE reservation_id = " + id;
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    System.out.println("Guest Room No: ");
                    int roomNo = rs.getInt("room_number");
                    System.out.println(roomNo);
                } else {
                    System.out.println("Guest ID does not exist");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Connection con, Scanner sc) throws SQLException {


        System.out.println("to update Enter Guest Id: ");
        int id = sc.nextInt();
        System.out.println("Enter New Guest name: ");
        String NguestName = sc.next();
        sc.nextLine();
        System.out.println("Enter New Guest room number: ");
        int NroomNumber = sc.nextInt();
        System.out.println("Enter New Contact Number: ");
        int NcontactNumber = sc.nextInt();
        System.out.println("Enter New Guest Address: ");
        String NguestAddress = sc.next();
        String query="UPDATE reservation SET guest_name = '" + NguestName + "', " +
                "room_number = " + NroomNumber + ", " +
                "contact_number = " + NcontactNumber + ", " +
                "Address = '" + NguestAddress + "'" +
                "WHERE reservation_id = " + id;
        try(Statement stmt = con.createStatement()){
            int effectedRow =stmt.executeUpdate(query);
            if(effectedRow > 0){
                System.out.println("Reservation has been updated.");
            }
            else{
                System.out.println("Reservation updated has been failed!!");
            }
        }
    }

    private static void delteReservation(Connection con, Scanner sc) throws SQLException{
        if (!reservationExists(con, reservationId)) {
            System.out.println("Reservation not found for the given ID.");
            return;
        }
        System.out.println("to delete Enter Guest Id: ");
        int id = sc.nextInt();

        String query = "DELETE FROM reservation WHERE reservation_id = " + id;
        try(Statement stmt = con.createStatement()){
            int effectedRow =stmt.executeUpdate(query);
            if(effectedRow > 0){
                System.out.println("Reservation has been deleted.");
            }else{
                System.out.println("Reservation deleted has been failed!!");
            }
        }
    }
    private static boolean reservationExists(Connection con, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print("");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using DEV Hotel Reservation System!!!");
    }
    }


