package ba.una.booking.dao;

import ba.una.booking.model.Film;
import ba.una.booking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmDaoImpl implements FilmDao {

    // =========================
    // FIND ALL / FILTERED
    // =========================
    @Override
    public List<Film> findAll() throws Exception {
        return findFiltered("", "ALL", "ALL");
    }

    @Override
    public List<Film> findFiltered(String search, String genre, String status) throws Exception {

        List<Film> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT id, naziv, originalni_naziv, zanr,
                   trajanje_min, godina_distribucije, status
            FROM films
            WHERE 1=1
        """);

        if (search != null && !search.isBlank())
            sql.append(" AND LOWER(naziv) LIKE LOWER(?)");

        if (genre != null && !"ALL".equalsIgnoreCase(genre))
            sql.append(" AND zanr = ?");

        if (status != null && !"ALL".equalsIgnoreCase(status))
            sql.append(" AND status = ?");

        sql.append(" ORDER BY naziv");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (search != null && !search.isBlank())
                ps.setString(i++, "%" + search + "%");

            if (genre != null && !"ALL".equalsIgnoreCase(genre))
                ps.setString(i++, genre);

            if (status != null && !"ALL".equalsIgnoreCase(status))
                ps.setString(i++, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    // =========================
    // FIND BY ID
    // =========================
    @Override
    public Optional<Film> findById(int id) throws Exception {

        String sql = "SELECT * FROM films WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return Optional.of(mapRow(rs));
        }

        return Optional.empty();
    }

    // =========================
    // SAVE / DELETE
    // =========================
    @Override
    public void save(Film film) throws Exception {
        if (film.getId() == 0) insert(film);
        else update(film);
    }

    @Override
    public void delete(int id) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM films WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // =========================
    // BOOKINGS CHECK
    // =========================
    @Override
    public boolean hasBookings(int filmId) throws Exception {

        String sql = "SELECT COUNT(*) FROM bookings WHERE film_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, filmId);
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // =========================
    // DASHBOARD COUNT
    // =========================
    @Override
    public int countActive() throws Exception {

        String sql = "SELECT COUNT(*) FROM films WHERE status='AKTIVAN'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // =========================
    // 🔥 STATIC HELPER – GET ID BY NAME
    // =========================
    public static int getIdByName(String naziv) {

        String sql = """
            SELECT id FROM films
            WHERE LOWER(TRIM(naziv)) = LOWER(TRIM(?))
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, naziv);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =========================
    // INSERT / UPDATE HELPERS
    // =========================
    private void insert(Film f) throws Exception {

        String sql = """
            INSERT INTO films
            (naziv, originalni_naziv, zanr,
             trajanje_min, godina_distribucije,
             status, napomena)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fill(ps, f);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                f.setId(rs.getInt(1));
        }
    }

    private void update(Film f) throws Exception {

        String sql = """
            UPDATE films SET
            naziv=?, originalni_naziv=?, zanr=?,
            trajanje_min=?, godina_distribucije=?,
            status=?, napomena=?
            WHERE id=?
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            fill(ps, f);
            ps.setInt(8, f.getId());
            ps.executeUpdate();
        }
    }

    private void fill(PreparedStatement ps, Film f) throws SQLException {

        ps.setString(1, f.getNaziv());
        ps.setString(2, f.getOriginalniNaziv());
        ps.setString(3, f.getZanr());
        ps.setInt(4, f.getTrajanjeMin());

        if (f.getGodinaDistribucije() == 0)
            ps.setNull(5, Types.INTEGER);
        else
            ps.setInt(5, f.getGodinaDistribucije());

        ps.setString(6, f.getStatus().name());
        ps.setString(7, f.getKratakOpis());
    }

    private Film mapRow(ResultSet rs) throws SQLException {

        Film f = new Film();
        f.setId(rs.getInt("id"));
        f.setNaziv(rs.getString("naziv"));
        f.setOriginalniNaziv(rs.getString("originalni_naziv"));
        f.setZanr(rs.getString("zanr"));
        f.setTrajanjeMin(rs.getInt("trajanje_min"));
        f.setGodinaDistribucije(rs.getInt("godina_distribucije"));
        f.setStatus(Film.Status.valueOf(rs.getString("status")));
        return f;
    }
}
