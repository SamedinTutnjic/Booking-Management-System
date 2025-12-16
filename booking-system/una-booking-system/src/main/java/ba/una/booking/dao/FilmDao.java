package ba.una.booking.dao;

import ba.una.booking.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FilmDao {

    List<Film> findAll() throws Exception;

    Optional<Film> findById(int id) throws Exception;

    void save(Film film) throws Exception;

    void delete(int id) throws Exception;

    List<Film> findFiltered(String search, String genre, String status) throws Exception;

    int countActive() throws Exception;

    boolean hasBookings(int filmId) throws Exception;

}
