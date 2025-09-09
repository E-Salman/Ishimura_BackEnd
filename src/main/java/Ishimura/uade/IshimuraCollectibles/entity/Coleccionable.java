package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.ArrayList;
import java.util.List;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "linea_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Linea linea;

    @OneToMany(mappedBy = "coleccionable", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Imagen> imagenes = new ArrayList<>();
}
