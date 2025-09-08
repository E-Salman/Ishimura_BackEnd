package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "catalogo")
public class Catalogo {

    @Id
    @Column(name = "coleccionable_id")
    private Long coleccionableId; // PK y FK al mismo tiempo

    @OneToOne
    @MapsId
    @JoinColumn(name = "coleccionable_id", nullable = false)
    private Coleccionable coleccionable;

    @Column(nullable = false)
    private Integer stock;
}

