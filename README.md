# 🎬 Booking Management System – UNA Film

**Booking Management System – UNA Film** je interna desktop aplikacija razvijena s ciljem digitalizacije i automatizacije administrativnih procesa u distributivnom odjelu kompanije **UNA Film d.o.o.**  
Aplikacija u potpunosti zamjenjuje dosadašnji rad u Excel tabelama i e-mail evidencijama te omogućava centralizovano upravljanje filmovima, kino partnerima i terminima prikazivanja.

> 🔒 Sistem je namijenjen isključivo internom korištenju unutar UNA Filma.

---

## 🧩 Osnovna ideja sistema

Kako partnerska kina sarađuju sa više distributera, korištenje posebne aplikacije za svakog dobavljača bilo bi nepraktično.  
Zbog toga se komunikacija prema partnerima i dalje odvija putem e-maila, dok se ova aplikacija koristi **interno** za:
- evidenciju filmova i partnera  
- planiranje i praćenje booking termina  
- kalendarski pregled prikazivanja  
- generisanje PDF/Excel izvještaja za administraciju i računovodstvo  

---

## 🛠️ Tehnologije

- **Programski jezik:** Java (JDK 17+)  
- **GUI:** JavaFX (FXML + CSS)  
- **Baza podataka:** MySQL (alternativno SQLite za demo)  
- **Arhitektura:** MVC (Model–View–Controller)  
- **Excel eksport:** Apache POI  

---

## 📊 Funkcionalni moduli

### 🔐 Login ekran (UNA Booking)
<img width="1199" height="779" alt="image" src="https://github.com/user-attachments/assets/cdc6d477-557d-405f-ae75-baec9dab110a" />

- Početni ekran aplikacije za autentifikaciju korisnika
- Unos korisničkog imena i šifre
- Siguran pristup sistemu prema dodijeljenim ovlaštenjima
- Jednostavan i moderan UI usklađen s brendom UNA Film
- Nakon uspješne prijave korisnik dobija pristup glavnim modulima aplikacije

### 📌 Dashboard
<img width="1198" height="779" alt="image" src="https://github.com/user-attachments/assets/b954dd25-a205-4e24-99c2-51cdeaf65640" />

Centralni pregled stanja sistema sa osnovnim statistikama:
- broj filmova, partnera i booking termina  
- brzi uvid u aktivne i nadolazeće projekcije  
- grafički prikaz ključnih podataka iz baze  

---

### 🎟️ Booking (glavni pregled / upravljanje)
<img width="1195" height="776" alt="1" src="https://github.com/user-attachments/assets/500859ee-4ae3-4aaf-a378-13ef7ce8bbe4" />

- Centralni prikaz svih booking termina u sistemu (film + partner + period prikazivanja)
- Sažetak statistike na vrhu: ukupno, potvrđeni, odbijeni, na čekanju
- Tabela sa ključnim kolonama: Film, Partner, Datum početka, Datum završetka, Materijal, Status, Kreirao, Kreirano
- Filteri za brže pretraživanje i prikaz (npr. po statusu/partneru)
- Brze akcije: + Dodaj Booking za unos novog termina
- Pregled detalja za svaki unos preko dugmeta Detalji
- Scroll/paginacija omogućava rad i sa većim brojem booking zapisa

### 🎟️ Dodaj booking
<img width="1198" height="778" alt="image" src="https://github.com/user-attachments/assets/0ae366cd-6ae9-4f3f-aa20-e56d8cc259f4" />

Modul za kreiranje i upravljanje booking terminima:
- unos filma, partnera i perioda prikazivanja  
- validacija datuma i podataka prije snimanja  
- evidencija statusa (aktivno, završeno, izmijenjeno)  


---

### 🏢 Partneri
<img width="1198" height="778" alt="image" src="https://github.com/user-attachments/assets/8c774904-ce2e-4a07-b75e-f42ed1f22cd2" />

Baza kino partnera:
- naziv kina, grad, adresa  
- kontakt osoba, email i telefon  
- interna evidencija saradnje i napomena  
- brza pretraga i filtriranje partnera  

## Dodaj partnera
<img width="1199" height="777" alt="dodaj partnera" src="https://github.com/user-attachments/assets/c1c2bb5a-693e-4480-a805-1fb665cf7288" />

Forma za unos novog kino-partnera u bazu. Omogućava unos osnovnih podataka o kinu, kontakt informacije, status saradnje i internu napomenu, uz spremanje partnera direktno u sistem.

---

### 🎞️ Filmovi
<img width="1198" height="779" alt="image" src="https://github.com/user-attachments/assets/d9ab39cf-5646-4c83-8380-29d8a382cf4f" />

Centralizovana baza filmova:
- naziv i originalni naziv  
- žanr, trajanje, godina izlaska  
- period distribucije i status  
- administracija kataloga filmova UNA Filma  

## Dodaj film
<img width="1199" height="777" alt="dodaj film" src="https://github.com/user-attachments/assets/5ce9215c-83fe-4ac3-9361-d72177172b9d" />

Forma za unos novog filma u sistem. Omogućava unos osnovnih informacija o filmu (naziv, godina, trajanje), izbor statusa i žanra, te dodavanje kratkog opisa prije spremanja u bazu.

---

### 📅 Booking kalendar
<img width="1199" height="779" alt="image" src="https://github.com/user-attachments/assets/af990f46-50f4-4917-9a1b-7a954a47cfa8" />

Vizuelni kalendarski prikaz svih termina:
- pregled projekcija po danima i mjesecima  
- filtriranje po filmu ili partneru  
- lakše planiranje i izbjegavanje preklapanja termina  

---

### ⚙️ Postavke ADMIN
<img width="1200" height="779" alt="image" src="https://github.com/user-attachments/assets/ec1c79f6-6a96-4662-9fa2-6fef1211d791" />

Administrativne i korisničke postavke:
- upravljanje korisničkim nalozima i ulogama  
- podešavanje teme (Light / Dark mode)  
- osnovne sistemske konfiguracije  
- sigurnosne i validacijske opcije  

---

## 📌 Dashboard (Referent)
<img width="1198" height="778" alt="Referent" src="https://github.com/user-attachments/assets/e0ead64b-4e47-4fa4-9251-a3d10c163c0c" />

Početni ekran za korisnika sa ograničenim ovlaštenjima. Prikazuje osnovne statistike (aktivni filmovi, partneri, booking aktivnost) te omogućava brze akcije za dodavanje bookinga i praćenje booking kalendara, bez administrativnog pristupa ostalim modulima sistema.

## ⚙️ Postavke REFERENT
<img width="1198" height="778" alt="image" src="https://github.com/user-attachments/assets/80f2f4f4-34d2-4b49-885e-796d4dd38aaa" />

Postavke za referentnu ulogu sa pregledom profila i osnovnim korisničkim opcijama (npr. promjena teme), bez administrativnih ovlaštenja.

---

## 🔐 Sigurnost i pouzdanost

- autentifikacija i autorizacija korisnika  
- hashiranje lozinki  
- validacija svih unosa prije upisa u bazu  
- stabilan rad bez gubitka podataka  
- mogućnost backup-a baze  

---

## 📦 Status projekta

✔️ Projekat razvijen kao **akademski i praktični softverski sistem**  
✔️ Korišten u svrhu demonstracije modernog desktop rješenja  
✔️ Spreman za prezentaciju i dalju nadogradnju


## 🧩 Instalacija i korištenje

Koraci instalacije

- Preuzmite kompletan projekat sa ovog GitHub repozitorija kao ZIP arhivu
  (Code → Download ZIP).
- Raspakujte (extract) preuzetu ZIP arhivu na željenu lokaciju na računaru.
- U raspakovanom folderu pronađite izvršnu datoteku UNA-Booking-1.0.exe.
- Dvoklikom pokrenite izvršnu datoteku kako biste započeli proces instalacije.
- Pratite korake instalacijskog čarobnjaka klikom na dugme Next sve do završetka instalacije.
- Nakon uspješne instalacije, ikona aplikacije će se automatski pojaviti na Desktopu.
  
Korištenje aplikacije
- Pokrenite aplikaciju dvoklikom na ikonu na Desktopu.
- Aplikacija je spremna za korištenje bez dodatnih podešavanja ili konfiguracije.

## 🔐 Testni korisnici

Admin (puna kontrola sistema)
- Korisničko ime: admin
- Šifra: admin

Referent (ograničene ovlasti)
- Korisničko ime: referent
- Šifra: referent


## Authors

Ovaj projekt je razvijen kao timski rad grupe:

- Samedin Tutnjić  
- Kemal Hasanspahić  
- Sanjin Samardžić  
- Aldina Ismić

