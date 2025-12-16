package ba.una.booking.dao;

import ba.una.booking.model.Partner;
import ba.una.booking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerDao {

    // ===================== GET ALL =====================
    public static List<Partner> getAll() {
        List<Partner> list = new ArrayList<>();

        String sql = """
            SELECT 
                id,
                naziv,
                grad,
                adresa,
                email,
                telefon,
                kontakt_osoba,
                napomena,
                active
            FROM partners
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Partner p = new Partner();

                p.setId(rs.getInt("id"));
                p.setNazivKina(rs.getString("naziv"));
                p.setGrad(rs.getString("grad"));
                p.setAdresa(rs.getString("adresa"));
                p.setEmail(rs.getString("email"));
                p.setTelefon(rs.getString("telefon"));
                p.setKontaktOsoba(rs.getString("kontakt_osoba"));
                p.setNapomena(rs.getString("napomena"));

                p.setStatus(
                        rs.getBoolean("active")
                                ? Partner.Status.AKTIVAN
                                : Partner.Status.NEAKTIVAN
                );

                list.add(p);
            }

        } catch (Exception e) {   // ✅ BITNO
            e.printStackTrace();
        }

        return list;
    }

    // ===================== COUNT =====================
    public static int countAll() {
        String sql = "SELECT COUNT(*) FROM partners";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (Exception e) {   // ✅ BITNO
            e.printStackTrace();
            return 0;
        }
    }

    // ===================== GET ID BY NAME =====================
    public static int getIdByName(String naziv) {
        String sql = """
            SELECT id FROM partners
            WHERE LOWER(TRIM(naziv)) = LOWER(TRIM(?))
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, naziv);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (Exception e) {   // ✅ BITNO
            e.printStackTrace();
        }

        return -1;
    }

    // ===================== INSERT =====================
    public static void insert(Partner p) {
        String sql = """
            INSERT INTO partners
            (naziv, grad, adresa, email, telefon, kontakt_osoba, napomena, active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNazivKina());
            ps.setString(2, p.getGrad());
            ps.setString(3, p.getAdresa());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getTelefon());
            ps.setString(6, p.getKontaktOsoba());
            ps.setString(7, p.getNapomena());
            ps.setBoolean(8, p.getStatus() == Partner.Status.AKTIVAN);

            ps.executeUpdate();

        } catch (Exception e) {   // ✅ BITNO
            e.printStackTrace();
        }
    }

    // ===================== DELETE =====================
    public static void deleteById(int id) {
        String sql = "DELETE FROM partners WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {   // ✅ BITNO
            e.printStackTrace();
        }
    }
}
