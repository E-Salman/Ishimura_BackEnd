package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @OneToOne(mappedBy = "lineaMarca")
    private Linea marcaLinea;

    // @ManyToOne
    // @JoinTable(name = "pertenece",
    // joinColumns = @JoinColumn(name = "marca_id"),
    // inverseJoinColumns = @JoinColumn(name = "linea_id"))
    // private Linea marcaLinea;

}