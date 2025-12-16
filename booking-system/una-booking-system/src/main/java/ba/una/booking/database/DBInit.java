package ba.una.booking.database;

import ba.una.booking.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBInit {

    public static void init() {

        try (Connection conn = DBUtil.getConnection()) {

            if (databaseExists(conn)) {
                System.out.println("✔ Baza već inicijalizovana – preskačem init");
                return;
            }

            System.out.println("⚡ Inicijalizujem bazu...");

            DBUtil.runSqlScript(conn, "/schema.sql");
            DBUtil.runSqlScript(conn, "/data.sql");

            // TEST
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {

                rs.next();
                System.out.println("👤 Broj korisnika: " + rs.getInt(1));
            }

            System.out.println("✔ Baza uspješno inicijalizovana");

        } catch (Exception e) {
            System.err.println("❌ Kritična greška u DBInit");
            e.printStackTrace();
        }
    }

    private static boolean databaseExists(Connection conn) {
        try (ResultSet rs =
                     conn.getMetaData().getTables(null, null, "users", null)) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }
}
