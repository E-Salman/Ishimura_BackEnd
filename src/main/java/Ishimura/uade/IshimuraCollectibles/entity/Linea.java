package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @OneToMany(mappedBy = "linea")
    @JsonManagedReference("linea-coleccionables")
    private List<Coleccionable> lineaColeccionables;

    @ManyToOne
    @JoinColumn(name = "marca_id", referencedColumnName = "id")
    private Marca marca;
}
