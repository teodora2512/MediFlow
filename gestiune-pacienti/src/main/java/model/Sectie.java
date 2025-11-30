package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sectii")
public class Sectie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSectie;

    @Column(nullable = false, unique = true)
    private String nume;

    @OneToMany(mappedBy = "sectie")
    private List<Pacient> pacienti;

}
