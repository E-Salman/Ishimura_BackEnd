package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.util.List;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionableDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long linea;
    private List<Long> imagenes; //Deberia guardar el ID de las imagenes, o las imagenes en si?
}
