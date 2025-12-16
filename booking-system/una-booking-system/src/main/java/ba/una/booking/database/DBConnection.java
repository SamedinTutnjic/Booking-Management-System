package ba.una.booking.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/una_film_booking?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Konekcija na bazu uspješna! 🎉");
            }
        } catch (Exception e) {
            System.out.println("GREŠKA pri konekciji na bazu!");
            e.printStackTrace();
        }
        return connection;
    }
}
