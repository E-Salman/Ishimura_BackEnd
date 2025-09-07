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

    
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @ElementCollection
    @CollectionTable(
        name = "catalogo_coleccionables",
        joinColumns = @JoinColumn(name = "catalogo_id")
    )
    @MapKeyJoinColumn(name = "coleccionable_id") // la clave = id del coleccionable
    @Column(name = "stock")                      // valor= stock
    private Map<Integer, Integer> coleccionablesConStock = new HashMap<>();

    public Catalogo() {
    }

    public Catalogo(Usuario usuario) {
        this.usuario = usuario;
    }
}
