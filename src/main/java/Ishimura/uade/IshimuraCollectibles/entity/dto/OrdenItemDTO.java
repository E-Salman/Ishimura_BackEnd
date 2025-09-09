package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrdenItemDTO {
  private Long coleccionableId;
  private String nombre;
  private Integer cantidad;
  private BigDecimal precioUnitario;
  private BigDecimal subtotal;
}
