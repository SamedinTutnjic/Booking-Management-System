package ba.una.booking.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUtil {

    private static final String URL =
            "jdbc:h2:file:" + System.getProperty("user.home") + "/UNA-Booking/data/una_booking_db;" +
                    "MODE=MySQL;" +
                    "DATABASE_TO_UPPER=false;" +
                    "CACHE_SIZE=131072;" +     // OK
                    "LOCK_MODE=0;" +           // brže
                    "DB_CLOSE_DELAY=-1";       // drži DB otvorenu



    private static final String USER = "sa";
    private static final String PASS = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void runSqlScript(Connection conn, String resourcePath) throws Exception {

        InputStream is = DBUtil.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("SQL fajl nije pronađen: " + resourcePath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sql = new StringBuilder();
        String line;

        try (Statement stmt = conn.createStatement()) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // preskoči komentare i prazne linije
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                sql.append(line).append(" ");

                if (line.endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
        }
    }
}
