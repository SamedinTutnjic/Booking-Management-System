-- =========================
-- USERS
-- =========================
INSERT INTO users (username, password_hash, role, active)
SELECT 'admin', 'admin', 'ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password_hash, role, active)
SELECT 'referent', 'referent', 'REFERENT', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'referent');



-- =========================
-- FILMS
-- =========================
INSERT INTO films
(naziv, originalni_naziv, trajanje_min, godina_distribucije, zanr, status)
VALUES
('Inception', 'Inception', 146, 2010, 'Sci-Fi', 'AKTIVAN'),
('The Godfather', 'The Godfather', 175, 1972, 'Crime', 'AKTIVAN'),
('Interstellar', 'Interstellar', 169, 2014, 'Sci-Fi', 'AKTIVAN'),
('The Matrix', 'The Matrix', 136, 1999, 'Sci-Fi', 'AKTIVAN'),
('Bambi', NULL, 142, 1982, 'Akcija', 'AKTIVAN');

-- =========================
-- PARTNERS  ✅ OVO TI FALI
-- =========================
INSERT INTO partners
(naziv, adresa, grad, email, telefon, kontakt_osoba, napomena, active)
VALUES
(
 'Cinema City',
 'Alta Shopping Center, Franca Lehara 2',
 'Sarajevo',
 'marko.kovacevic@cinemacity.ba',
 '061123456',
 'Marko Kovacevic',
 NULL,
 TRUE
),
(
 'Multiplex Ekran',
 'Bulevar Kralja Tvrtka 1',
 'Zenica',
 'amina.hadzic@multiplexekran.ba',
 '062987654',
 'Amina Hadzic',
 NULL,
 TRUE
),
(
 'CineStar Mostar',
 'Mall of Mostar, Kralja Tomislava bb',
 'Mostar',
 'ivan.maric@cinestar.ba',
 '063442110',
 'Ivan Maric',
 NULL,
 TRUE
),
(
 'Kino Breza',
 'Kamenice 102',
 'Breza',
 'kemal.hasanspahic.zzz@sze.ba',
 '061058889',
 'Kemal Hasanspahic',
 NULL,
 FALSE
);

-- =========================
-- BOOKINGS
-- =========================
INSERT INTO bookings
(film_id, partner_id, datum_od, datum_do, broj_sedmica, status, tip_materijala, napomena, created_by)
VALUES
(1, 1, '2025-01-10', '2025-01-24', 2, 'POTVRDJENO', 'DCP', 'Premijerna projekcija', 1),
(2, 2, '2025-02-01', '2025-02-15', 2, 'NA_CEKANJU', 'BluRay', NULL, 1);
