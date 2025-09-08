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
    // private imagen; //

    private Long lineaId;
    private String lineaNombre;

    private Long marcaId;
    private String marcaNombre;
}
