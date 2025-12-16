package ba.una.booking.dao;

import ba.una.booking.model.Booking;
import ba.una.booking.model.Film;
import ba.una.booking.model.Partner;
import ba.una.booking.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;


public class BookingDao {

    // =====================================================
    // GET ALL BOOKINGS (BookingController, Dashboard)
    // =====================================================
    public static List<Booking> getAll() {

        List<Booking> list = new ArrayList<>();

        String sql = """
    SELECT
        b.id,
        b.datum_od,
        b.datum_do,
        b.tip_materijala,
        b.status,
        b.created_by,
        b.created_at,
        b.napomena,

        f.id AS film_id,
        f.naziv AS film_naziv,

        p.id AS partner_id,
        p.naziv AS partner_naziv

    FROM bookings b
    JOIN films f ON b.film_id = f.id
    JOIN partners p ON b.partner_id = p.id
    ORDER BY b.datum_od
""";


        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // MAP ROW
    // =====================================================
    private static Booking mapRow(ResultSet rs) throws SQLException {

        Booking b = new Booking();

        b.setId(rs.getInt("id"));
        b.setDatumPocetka(rs.getDate("datum_od").toLocalDate());
        b.setDatumZavrsetka(rs.getDate("datum_do").toLocalDate());
        b.setTipMaterijala(rs.getString("tip_materijala"));
        b.setCreatedByUserId(rs.getInt("created_by"));

        try {
            b.setStatus(Booking.Status.valueOf(rs.getString("status")));
        } catch (Exception e) {
            b.setStatus(Booking.Status.NA_CEKANJU);
        }

        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setNaziv(rs.getString("film_naziv"));
        b.setFilm(film);

        Partner partner = new Partner();
        partner.setId(rs.getInt("partner_id"));
        partner.setNazivKina(rs.getString("partner_naziv"));
        b.setPartner(partner);

        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            b.setCreatedAt(createdAt.toLocalDate());
        }

        return b;
    }

    // =====================================================
    // CALENDAR FILTER (BookingCalendarController)
    // =====================================================
    public static List<Booking> getForMonth(
            int year,
            int month,
            String film,
            String partner,
            String status
    ) {

        List<Booking> list = new ArrayList<>();

        String sql = """
            SELECT 
                b.id,
                b.datum_od,
                b.datum_do,
                b.tip_materijala,
                b.status,
                b.created_by,
                b.created_at,

                f.id AS film_id,
                f.naziv AS film_naziv,

                p.id AS partner_id,
                p.naziv AS partner_naziv

            FROM bookings b
            JOIN films f ON b.film_id = f.id
            JOIN partners p ON b.partner_id = p.id

            WHERE YEAR(b.datum_od) = ?
              AND MONTH(b.datum_od) = ?

              AND (? IS NULL OR ? = '' OR f.naziv = ?)
              AND (? IS NULL OR ? = '' OR p.naziv = ?)
              AND (? IS NULL OR ? = '' OR b.status = ?)

            ORDER BY b.datum_od
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;

            ps.setInt(i++, year);
            ps.setInt(i++, month);

            ps.setString(i++, film);
            ps.setString(i++, film);
            ps.setString(i++, film);

            ps.setString(i++, partner);
            ps.setString(i++, partner);
            ps.setString(i++, partner);

            ps.setString(i++, status);
            ps.setString(i++, status);
            ps.setString(i++, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // INSERT BOOKING
    // =====================================================
    public static void insertBooking(
            int filmId,
            int partnerId,
            LocalDate datumOd,
            LocalDate datumDo,
            String tipMaterijala,
            String status,
            int createdBy,
            String napomena
    ) {

        String sql = """
            INSERT INTO bookings
            (film_id, partner_id, datum_od, datum_do, tip_materijala, status, created_by, created_at, napomena)
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, filmId);
            ps.setInt(2, partnerId);
            ps.setDate(3, Date.valueOf(datumOd));
            ps.setDate(4, Date.valueOf(datumDo));
            ps.setString(5, tipMaterijala);
            ps.setString(6, status);
            ps.setInt(7, createdBy);
            ps.setString(8, napomena);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // DELETE
    // =====================================================
    public static void deleteById(int id) {

        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {   // ← OVO JE BITNO
            e.printStackTrace();
        }
    }


    // =====================================================
    // FILTER HELPERS
    // =====================================================
    public static List<Booking> getByFilmName(String filmName) {

        List<Booking> list = new ArrayList<>();

        String sql = """
        SELECT *
        FROM bookings b
        JOIN films f ON b.film_id = f.id
        JOIN partners p ON b.partner_id = p.id
        WHERE f.naziv = ?
        ORDER BY b.datum_od
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, filmName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapBooking(rs)); // pretpostavljam da ovo već imaš
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static ObservableList<String> getAllPartnerNames() {

        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT naziv FROM partners ORDER BY naziv";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(rs.getString("naziv"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ObservableList<String> getAllStatuses() {
        return FXCollections.observableArrayList(
                "POTVRDJENO",
                "NA_CEKANJU",
                "ODBIJEN"
        );
    }

    private static Booking mapBooking(ResultSet rs) throws SQLException {

        Booking b = new Booking();
        b.setId(rs.getInt("id"));

        // FILM
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setNaziv(rs.getString("film_naziv"));
        b.setFilm(film);

        // PARTNER
        Partner partner = new Partner();
        partner.setId(rs.getInt("partner_id"));
        partner.setNazivKina(rs.getString("partner_naziv"));
        b.setPartner(partner);

        // DATUMI
        b.setDatumPocetka(rs.getDate("datum_od").toLocalDate());
        b.setDatumZavrsetka(rs.getDate("datum_do").toLocalDate());

        // OSTALO
        b.setTipMaterijala(rs.getString("tip_materijala"));
        b.setStatus(Booking.Status.valueOf(rs.getString("status")));
        b.setCreatedByUserId(rs.getInt("created_by"));

        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            b.setCreatedAt(createdAt.toLocalDate());
        }

        b.setNapomena(rs.getString("napomena"));
        return b;
    }

    public static List<String> getAllFilmNames() {

        List<String> list = new ArrayList<>();

        String sql = """
        SELECT DISTINCT f.naziv
        FROM films f
        ORDER BY f.naziv
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("naziv"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static int countThisMonth() {

        String sql = """
        SELECT COUNT(*)
        FROM bookings
        WHERE MONTH(datum_od) = MONTH(CURRENT_DATE)
          AND YEAR(datum_od) = YEAR(CURRENT_DATE)
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int countActivePartners() {

        String sql = "SELECT COUNT(*) FROM partners WHERE active = true";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Map<String, Integer> bookingsPerMonth() {

        Map<String, Integer> map = new LinkedHashMap<>();

        String sql = """
        SELECT MONTH(datum_od) AS m, COUNT(*) AS c
        FROM bookings
        GROUP BY MONTH(datum_od)
        ORDER BY m
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int month = rs.getInt("m");
                int count = rs.getInt("c");
                map.put(Month.of(month).name(), count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Map<String, Integer> topPartners() {

        Map<String, Integer> map = new LinkedHashMap<>();

        String sql = """
        SELECT p.naziv AS partner, COUNT(*) AS cnt
        FROM bookings b
        JOIN partners p ON b.partner_id = p.id
        GROUP BY p.naziv
        ORDER BY cnt DESC
        LIMIT 10
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(
                        rs.getString("partner"),
                        rs.getInt("cnt")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    // =====================================================
// EXPORT – GET BOOKINGS BY FILM ID (STABILNO)
// =====================================================
    public static List<Booking> getByFilmId(int filmId) {

        List<Booking> list = new ArrayList<>();

        String sql = """
        SELECT
            b.id,
            b.datum_od,
            b.datum_do,
            b.tip_materijala,
            b.status,
            b.created_by,
            b.created_at,
            b.napomena,

            f.id AS film_id,
            f.naziv AS film_naziv,

            p.id AS partner_id,
            p.naziv AS partner_naziv

        FROM bookings b
        JOIN films f ON b.film_id = f.id
        JOIN partners p ON b.partner_id = p.id
        WHERE f.id = ?
        ORDER BY b.datum_od
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, filmId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs)); // ⬅ koristi TVOJU POSTOJEĆU mapRow()
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }





}
