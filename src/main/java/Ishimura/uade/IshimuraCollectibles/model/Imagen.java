package Ishimura.uade.IshimuraCollectibles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Blob;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imagenes")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Blob image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coleccionable_id") //imagenes_id en realidad es el id de la tabla de coleccionables
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Coleccionable coleccionable;
}
