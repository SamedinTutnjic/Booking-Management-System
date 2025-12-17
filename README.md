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
- **PDF / Excel eksport:** iText / Apache PDFBox / Apache POI  
- **Kontrola verzije:** Git & GitHub  

---

## 📊 Funkcionalni moduli

### 📌 Dashboard
<img width="1198" height="779" alt="image" src="https://github.com/user-attachments/assets/b954dd25-a205-4e24-99c2-51cdeaf65640" />

Centralni pregled stanja sistema sa osnovnim statistikama:
- broj filmova, partnera i booking termina  
- brzi uvid u aktivne i nadolazeće projekcije  
- grafički prikaz ključnih podataka iz baze  

---

### 🎟️ Booking
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

---

### 🎞️ Filmovi
<img width="1198" height="779" alt="image" src="https://github.com/user-attachments/assets/d9ab39cf-5646-4c83-8380-29d8a382cf4f" />

Centralizovana baza filmova:
- naziv i originalni naziv  
- žanr, trajanje, godina izlaska  
- period distribucije i status  
- administracija kataloga filmova UNA Filma  

---

### 📅 Booking kalendar
<img width="1199" height="779" alt="image" src="https://github.com/user-attachments/assets/af990f46-50f4-4917-9a1b-7a954a47cfa8" />

Vizuelni kalendarski prikaz svih termina:
- pregled projekcija po danima i mjesecima  
- filtriranje po filmu ili partneru  
- lakše planiranje i izbjegavanje preklapanja termina  

---

### ⚙️ Postavke
<img width="1200" height="779" alt="image" src="https://github.com/user-attachments/assets/ec1c79f6-6a96-4662-9fa2-6fef1211d791" />

Administrativne i korisničke postavke:
- upravljanje korisničkim nalozima i ulogama  
- podešavanje teme (Light / Dark mode)  
- osnovne sistemske konfiguracije  
- sigurnosne i validacijske opcije  

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

## Authors

This project was developed as a team effort by:

- Samedin Tutnjić  
- Kemal Hasanspahić  
- Sanjin Samardžić  
- Aldina Ismić

