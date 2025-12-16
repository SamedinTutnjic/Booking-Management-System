-- =========================
-- USERS
-- =========================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL
);

-- =========================
-- FILMS
-- =========================
CREATE TABLE IF NOT EXISTS films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(150) NOT NULL,
    originalni_naziv VARCHAR(150),
    trajanje_min INT,
    godina_distribucije INT,
    zanr VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    napomena TEXT
);

-- =========================
-- PARTNERS
-- =========================
CREATE TABLE IF NOT EXISTS partners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(150) NOT NULL,
    adresa VARCHAR(200),
    grad VARCHAR(100),
    email VARCHAR(150),
    telefon VARCHAR(50),
    kontakt_osoba VARCHAR(100),
    napomena TEXT,
    active BOOLEAN NOT NULL
);

-- =========================
-- BOOKINGS  ✅ FIXED
-- =========================
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    film_id INT NOT NULL,
    partner_id INT NOT NULL,
    datum_od DATE NOT NULL,
    datum_do DATE NOT NULL,
    broj_sedmica INT,
    status VARCHAR(20),
    tip_materijala VARCHAR(50),
    napomena TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_film FOREIGN KEY (film_id) REFERENCES films(id),
    CONSTRAINT fk_booking_partner FOREIGN KEY (partner_id) REFERENCES partners(id)
);
