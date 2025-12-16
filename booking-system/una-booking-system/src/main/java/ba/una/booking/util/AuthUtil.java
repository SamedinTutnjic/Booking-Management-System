package ba.una.booking.util;

import ba.una.booking.session.UserSession;

public class AuthUtil {

    public static boolean isAdmin() {
        return UserSession.isAdmin();
    }

    public static boolean isReferent() {
        return UserSession.isReferent();
    }

    // Ako samo admin smije ući u neku akciju (npr. Add Film / Add Partner)
    public static void requireAdmin() {
        if (!UserSession.isAdmin()) {
            throw new SecurityException("Pristup zabranjen! Samo admin ima ovlasti.");
        }
    }
}
