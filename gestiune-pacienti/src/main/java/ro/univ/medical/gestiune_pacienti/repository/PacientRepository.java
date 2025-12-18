package ro.univ.medical.gestiune_pacienti.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.univ.medical.gestiune_pacienti.entity.Pacient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacientRepository extends JpaRepository<Pacient, String> {

    // Cautare după CNP(pentru modificare / ștergere)
    Optional<Pacient> findByCnp(String cnp);

    @Query("""
SELECT p FROM Pacient p
WHERE (:nume IS NULL OR LOWER(p.nume) LIKE LOWER(CONCAT('%', :nume, '%')))
  AND (:prenume IS NULL OR LOWER(p.prenume) LIKE LOWER(CONCAT('%', :prenume, '%')))
  AND (:diagnostic IS NULL OR LOWER(p.diagnostic) LIKE LOWER(CONCAT('%', :diagnostic, '%')))
  AND (:tratament IS NULL OR LOWER(p.tratament) LIKE LOWER(CONCAT('%', :tratament, '%')))
""")
    List<Pacient> filtrareDinamica(
            @Param("nume") String nume,
            @Param("prenume") String prenume,
            @Param("diagnostic") String diagnostic,
            @Param("tratament") String tratament
    );
}
