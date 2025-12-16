package ba.una.booking.session;

import ba.una.booking.model.User;

public class UserSession {

    private static User loggedUser;

    // =========================
    // DARK MODE STATE
    // =========================
    private static boolean darkMode = false;

    // =========================
    // USER
    // =========================

    // Poziva se nakon uspješne prijave
    public static void setUser(User user) {
        loggedUser = user;
    }

    // Dohvati usera
    public static User getUser() {
        return loggedUser;
    }

    // Dohvati rolu
    public static String getRole() {
        return loggedUser != null ? loggedUser.getRole() : null;
    }

    // Provjera uloge
    public static boolean isAdmin() {
        return loggedUser != null && "ADMIN".equalsIgnoreCase(loggedUser.getRole());
    }

    public static boolean isReferent() {
        return loggedUser != null && "REFERENT".equalsIgnoreCase(loggedUser.getRole());
    }

    // =========================
    // DARK MODE API
    // =========================

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean value) {
        darkMode = value;
    }

    public static void toggleDarkMode() {
        darkMode = !darkMode;
    }

    // =========================
    // LOGOUT
    // =========================

    public static void clear() {
        loggedUser = null;
        darkMode = false; // reset theme on logout
    }
}
