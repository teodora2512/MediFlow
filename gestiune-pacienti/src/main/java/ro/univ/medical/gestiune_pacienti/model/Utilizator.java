package ro.univ.medical.gestiune_pacienti.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "utilizatori")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilizator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUtilizator;

    private String nume;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String parola;

    @Column(nullable = false)
    private String rol;

    @OneToMany(mappedBy = "utilizator")
    private List<Pacient> pacienti;
}
