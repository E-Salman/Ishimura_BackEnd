package Ishimura.uade.IshimuraCollectibles.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Usuario (1:1)
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    // Diccionario Coleccionable -> Stock
    @ElementCollection
    @CollectionTable(
        name = "catalogo_coleccionables",
        joinColumns = @JoinColumn(name = "catalogo_id")
    )
    @MapKeyJoinColumn(name = "coleccionable_id") // la clave del map = FK a coleccionables
    @Column(name = "stock")                      // el valor del map = stock
    private Map<Coleccionable, Integer> coleccionablesConStock = new HashMap<>();

    public Catalogo() {
    }

    public Catalogo(Usuario usuario) {
        this.usuario = usuario;
    }
}
