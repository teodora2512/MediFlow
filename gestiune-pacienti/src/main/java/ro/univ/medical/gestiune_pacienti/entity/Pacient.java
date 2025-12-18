package ro.univ.medical.gestiune_pacienti.entity;

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
    private String cnp;

    @ManyToOne
    @JoinColumn(name = "id_utilizator") //(cel care a adaugat pacientul)
    private Utilizator utilizator;

    @Column(nullable = false)
    private String nume;

    @Column(nullable = false)
    private String prenume;


    private String diagnostic;

    private String tratament;

    @Column(nullable = false)
    private LocalDate dataInternarii;

    private LocalDate dataExternarii;


}
