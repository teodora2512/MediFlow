# 🏥 Sistem de Gestiune a Pacienților

Aplicație web pentru managementul pacienților într-un mediu medical, cu sistem de autentificare și autorizare bazat pe roluri.

## 📋 Funcționalități

### 🔐 Autentificare și Securitate
- **Sistem de login/logout** cu formular dedicat
- **Autorizare bazată pe roluri** (USER și EDITOR)
- **Spring Security** cu BCrypt password encoding
- **Protecție rută** pentru operațiile sensibile

### 👥 Gestiune Pacienți
- **CRUD complet**: Adăugare, Vizualizare, Modificare, Ștergere
- **Căutare avansată** cu filtrare dinamică (nume, prenume, diagnostic, tratament)
- **Internare/Externare** pacienți cu management date
- **Validare CNP** (13 cifre, unic în sistem)
- **Asociere automată** cu utilizatorul care a efectuat operația

### 🎨 Interfață Utilizator
- **Design modern** cu Bootstrap 5
- **Tabel interactiv** cu toate detaliile pacienților
- **Formular inteligent** care se precompletează la căutare
- **Feedback vizual** pentru fiecare operație
- **Responsive** și user-friendly

### 👤 Roluri și Permisiuni
- **EDITOR**: Acces complet (CRUD, externare)
- **USER**: Doar vizualizare și căutare

## 🛠 Tehnologii Utilizate

- **Backend**: Java 17, Spring Boot 3.x
- **Security**: Spring Security 6.x
- **Database**: MySQL 8.x, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Security Test

## 🗄️ Structura Bazei de Date

### Tabele Principale
1. **utilizatori** - utilizatori sistemului (username, parolă, rol)
2. **pacienti** - pacienți (CNP, nume, prenume, diagnostic, tratament, date internare/externare)
