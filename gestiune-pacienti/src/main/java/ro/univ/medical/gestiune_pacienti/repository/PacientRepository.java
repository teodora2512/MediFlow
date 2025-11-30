package ro.univ.medical.gestiune_pacienti.repository;
import ro.univ.medical.gestiune_pacienti.model.Pacient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacientRepository extends JpaRepository<Pacient, Integer> {

}
