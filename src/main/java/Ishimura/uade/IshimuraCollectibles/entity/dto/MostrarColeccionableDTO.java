package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostrarColeccionableDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    // private int idImagen; // preguntar como asociamos correctanmente la imagen, o
    // si la subimos
    // directamente por insomnia

    private Long lineaId;
    private String lineaNombre;

    private Long marcaId;
    private String marcaNombre;
}
