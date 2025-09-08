package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany
    private List<Linea> lineas;

    public Marca() {}

    public Marca(String nombre) {
        this.nombre = nombre;
    }
}
