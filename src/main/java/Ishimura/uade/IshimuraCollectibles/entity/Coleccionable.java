package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.ArrayList;
import java.util.List;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@NoArgsConstructor
public class Coleccionable {

    public Coleccionable(String nombre, String description, Double precio, Linea linea, List<Imagen> imagenes) {
        this.nombre = nombre;
        this.description = description;
        this.precio = precio;
        this.linea = linea;
        this.imagenes = imagenes;

    }

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

    @OneToMany(mappedBy = "coleccionable", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Imagen> imagenes = new ArrayList<>();
}