package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @ManyToOne
    @JoinColumn(name = "linea_id", referencedColumnName = "id")
    @JsonBackReference("linea-coleccionables")
    private Linea linea;
}
