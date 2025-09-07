package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
    name = "orden_articulo",
    uniqueConstraints = @UniqueConstraint(columnNames = {"orden_compra_id", "coleccionable_id"})
)
public class Orden_Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK a la cabecera
    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private Orden_Compra ordenCompra;

    // FK al producto
    @ManyToOne(optional = false)
    @JoinColumn(name = "coleccionable_id", nullable = false)
    private Coleccionable coleccionable;

    // cantidad de figuras con este id
    @Column(nullable = false)
    private Integer cantidad;
}

