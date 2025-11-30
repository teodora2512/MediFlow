package ro.univ.medical.gestiune_pacienti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.univ.medical.gestiune_pacienti.model.Utilizator;

public interface UtilizatorRepository extends JpaRepository<Utilizator, Integer> {
    Utilizator findByUsername(String username);
}
