package ba.una.booking.service;

import ba.una.booking.dao.UserDao;
import ba.una.booking.model.User;
import ba.una.booking.util.PasswordUtil;

public class AuthService {

    private final UserDao userDao = new UserDao();

    public User login(String username, String password) throws Exception {

        User u = userDao.findByUsername(username);

        if (u == null) return null;
        if (!u.isActive()) return null;

        // ❗ ISPRAVNA PROVJERA
        if (!PasswordUtil.verifyPassword(password, u.getPasswordHash())) {
            return null;
        }

        return u;
    }
}
