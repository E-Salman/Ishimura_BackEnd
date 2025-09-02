package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
public class Linea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @OneToOne(mappedBy = "coleccionableLinea")
    private Coleccionable lineaColeccionable;

    @OneToOne
    @JoinTable(name = "pertenece", joinColumns = @JoinColumn(name = "linea_id"), inverseJoinColumns = @JoinColumn(name = "marca_id"))
    private Marca lineaMarca;

}
