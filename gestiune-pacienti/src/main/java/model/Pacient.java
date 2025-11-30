package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pacienti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pacient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPacient;

    @ManyToOne
    @JoinColumn(name = "id_utilizator")
    private Utilizator utilizator;


    @ManyToOne(optional = false)
    @JoinColumn(name = "id_sectie")
    private Sectie sectie;

    @Column(nullable = false)
    private String nume;

    @Column(nullable = false)
    private String prenume;

    @Column(nullable = false, unique = true)
    private String CNP;


    private String diagnostic;
    private String tratament;

    @Column(nullable = false)
    private LocalDate dataInternarii;

    private LocalDate dataExternarii;





}
