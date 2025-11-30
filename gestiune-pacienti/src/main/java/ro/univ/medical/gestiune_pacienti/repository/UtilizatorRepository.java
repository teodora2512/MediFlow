package ro.univ.medical.gestiune_pacienti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.univ.medical.gestiune_pacienti.model.Utilizator;

import java.util.Optional;

public interface UtilizatorRepository extends JpaRepository<Utilizator, Integer> {
    Optional<Utilizator> findByUsername(String username);
}
