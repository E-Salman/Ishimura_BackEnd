package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @OneToMany
    @JoinColumn(name = "linea_id", insertable = false, updatable = false)
    private List<Coleccionable> lineaColeccionables;

    
}
