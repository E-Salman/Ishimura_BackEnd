package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdenResumenDTO {
  private String numeroOrden;
  private BigDecimal montoTotal;
  private LocalDateTime creadaEn;
}
