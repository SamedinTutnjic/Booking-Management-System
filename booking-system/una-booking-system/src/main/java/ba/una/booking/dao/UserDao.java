package ba.una.booking.dao;

import ba.una.booking.util.DBUtil;
import ba.una.booking.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public static User findByUsername(String username) {

        String sql = """
            SELECT id, username, password_hash, role, active
            FROM users
            WHERE username = ? AND active = TRUE
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                u.setActive(rs.getBoolean("active"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
