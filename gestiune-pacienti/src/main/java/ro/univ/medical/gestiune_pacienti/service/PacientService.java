package ro.univ.medical.gestiune_pacienti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.univ.medical.gestiune_pacienti.entity.Pacient;
import ro.univ.medical.gestiune_pacienti.entity.Utilizator;
import ro.univ.medical.gestiune_pacienti.repository.PacientRepository;
import ro.univ.medical.gestiune_pacienti.repository.UtilizatorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PacientService {

    private final PacientRepository pacientRepository;
    private final UtilizatorRepository utilizatorRepository;

    @Autowired
    public PacientService(PacientRepository pacientRepository, UtilizatorRepository utilizatorRepository) {
        this.pacientRepository = pacientRepository;
        this.utilizatorRepository = utilizatorRepository;
    }

    /**
     * Metodă de suport pentru a obține Utilizatorul curent logat.
     */
    private Utilizator getCurrentUtilizator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return utilizatorRepository.findByUsername(username).orElse(null);
    }

    /**
     * Returnează toți pacienții.
     */
    public List<Pacient> findAll() {
        return pacientRepository.findAll();
    }

    /**
     * Gestionează logica de Adăugare/Internare a unui pacient nou.
     * @return Mesajul de feedback.
     */
    public String adaugaPacient(String cnp, String nume, String prenume, String diagnostic, String tratament) {
        if (cnp == null || cnp.trim().isEmpty() || nume == null || nume.trim().isEmpty() || prenume == null || prenume.trim().isEmpty()) {
            return "Adaugarea nu se realizează dacă nu completați CNP, Nume și Prenume!";
        }
        // 2. NOU: Validarea lungimii CNP-ului (trebuie să aibă 13 caractere)
        if (cnp.trim().length() != 13) {
            return "CNP-ul introdus nu este valid. Trebuie să conțină exact 13 cifre.";
        }

        if (!cnp.trim().matches("\\d+")) { return "CNP-ul trebuie să conțină doar cifre."; }
        if (pacientRepository.findByCnp(cnp).isPresent()) {
            return "Pacientul cu CNP-ul " + cnp + " există deja!";
        }

        Utilizator utilizatorCurent = getCurrentUtilizator();
        Pacient pacient = new Pacient(cnp, utilizatorCurent, nume, prenume, diagnostic, tratament, LocalDate.now(), null);
        pacientRepository.save(pacient);
        return "Adăugare realizată cu succes! (Internare: azi)";
    }

    /**
     * Gestionează logica de Modificare/Externare a unui pacient existent.
     * @return Mesajul de feedback.
     */
    public String modificaPacient(String cnp, String nume, String prenume, String diagnostic, String tratament) {
        if (cnp == null || cnp.trim().isEmpty()) {
            return "Introduceți CNP-ul pacientului de modificat!";
        }

        Optional<Pacient> pacientOpt = pacientRepository.findByCnp(cnp);
        if (!pacientOpt.isPresent()) {
            return "Nu se găsește niciun pacient cu CNP-ul " + cnp;
        }

        Pacient pacient = pacientOpt.get();

        // Actualizează câmpurile (dacă nu sunt null sau goale)
        if (nume != null && !nume.trim().isEmpty()) {
            pacient.setNume(nume);
        }
        if (prenume != null && !prenume.trim().isEmpty()) {
            pacient.setPrenume(prenume);
        }
        if (diagnostic != null) {
            pacient.setDiagnostic(diagnostic);
        }
        if (tratament != null) {
            pacient.setTratament(tratament);
        }

        pacientRepository.save(pacient);
        return "Pacientul cu CNP-ul " + cnp + " a fost modificat!";
    }

    /**
     * Gestionează logica de Ștergere a unui pacient.
     * @return Mesajul de feedback.
     */
    public String stergePacient(String cnp) {
        if (cnp == null || cnp.trim().isEmpty()) {
            return "Introduceți CNP-ul pacientului de șters!";
        }
        Optional<Pacient> pacientOpt = pacientRepository.findByCnp(cnp);
        if (pacientOpt.isPresent()) {
            pacientRepository.delete(pacientOpt.get());
            return "Pacientul cu CNP-ul " + cnp + " a fost șters!";
        } else {
            return "Nu se găsește niciun pacient cu CNP-ul " + cnp;
        }
    }

    /**
     * Gestionează logica de căutare și filtrare.
     */
    public List<Pacient> cautaPacienti(String cnp,
                                       String nume,
                                       String prenume,
                                       String diagnostic,
                                       String tratament) {

        // CNP – prioritate absolută
        if (cnp != null && !cnp.isBlank()) {
            return pacientRepository.findByCnp(cnp)
                    .map(List::of)
                    .orElse(List.of());
        }

        //Orice combinație de câmpuri
        if ((nume != null && !nume.isBlank()) ||
                (prenume != null && !prenume.isBlank()) ||
                (diagnostic != null && !diagnostic.isBlank()) ||
                (tratament != null && !tratament.isBlank())) {

            return pacientRepository.filtrareDinamica(
                    nume,
                    prenume,
                    diagnostic,
                    tratament
            );
        }

        return pacientRepository.findAll();
    }
    public String externarePacient(String cnp) {
        if (cnp == null || cnp.trim().isEmpty()) {
            return "Introduceți CNP-ul pacientului!";
        }

        Optional<Pacient> pacientOpt = pacientRepository.findByCnp(cnp);
        if (pacientOpt.isPresent()) {
            Pacient pacient = pacientOpt.get();
            if (pacient.getDataExternarii() != null) {
                return "Pacientul a fost deja externat!";
            }
            pacient.setDataExternarii(LocalDate.now());
            pacientRepository.save(pacient);
            return "Pacientul a fost externat!";
        } else {
            return "Nu se găsește niciun pacient cu CNP-ul " + cnp;
        }
    }
}