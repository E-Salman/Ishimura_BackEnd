package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    //@OneToMany(mappedBy = "marca")
    //private List<Linea> lineas;

    public Marca() {
    }

    public Marca(String nombre) {
        this.nombre = nombre;
    }
}
