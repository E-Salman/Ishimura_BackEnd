package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
    name = "orden_articulo",
    uniqueConstraints = @UniqueConstraint(columnNames = {"orden_id", "coleccionable_id"})
)
public class Orden_Articulo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // FK a la cabecera (Orden)
  @ManyToOne(optional = false)
  @JoinColumn(name = "orden_id", nullable = false)
  private Orden orden;

  // FK al producto
  @ManyToOne(optional = false)
  @JoinColumn(name = "coleccionable_id", nullable = false)
  private Coleccionable coleccionable;

  // cantidad de figuras con este id
  @Column(nullable = false)
  private Integer cantidad;
}
