package Ishimura.uade.IshimuraCollectibles.entity;

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

    @Column
    private int idImagen;

    // Relación con Category
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Relación con Linea
    @ManyToOne
    @JoinColumn(name = "linea_id", referencedColumnName = "id")
    private Linea linea;

}
