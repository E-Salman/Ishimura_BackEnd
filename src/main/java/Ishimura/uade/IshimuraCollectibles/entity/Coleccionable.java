package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.List;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Coleccionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String description;

    @Column
    private Double precio;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Relación con Linea
    @ManyToOne
    @JoinColumn(name = "linea_id", referencedColumnName = "id")
    private Linea linea;

    @OneToMany
    @JoinColumn(name = "imagenes_id", referencedColumnName = "id")
    private List<Imagen> imagenes;
}
