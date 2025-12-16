package ba.una.booking.model;

public class User {

    public enum Uloga {
        ADMIN,
        USER
    }

    private int id;
    private String username;
    private String passwordHash;
    private String role;      // vrijednost iz baze (string)
    private boolean active;

    private Uloga uloga;      // enum verzija role

    // ✅ PRAZAN KONSTRUKTOR (OBAVEZAN za DAO, H2, JavaFX)
    public User() {
    }

    // ✅ KONSTRUKTOR SA SVIM POLJIMA (koristan za testove, insert, seed)
    public User(int id, String username, String passwordHash, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        syncUloga();
    }

    // 🔄 pomoćna metoda za sync string -> enum
    private void syncUloga() {
        if (role == null) {
            this.uloga = Uloga.USER;
            return;
        }

        switch (role.toLowerCase()) {
            case "admin":
                this.uloga = Uloga.ADMIN;
                break;
            case "user":
            case "referent":
                this.uloga = Uloga.USER;
                break;
            default:
                this.uloga = Uloga.USER;
        }
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public Uloga getUloga() {
        return uloga;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
        syncUloga();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
        this.role = uloga.name().toLowerCase();
    }
}
