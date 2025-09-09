package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdenDetalleDTO {
  private String numeroOrden;
  private BigDecimal montoTotal;
  private String metodoPago;
  private LocalDateTime creadaEn;
  private List<OrdenItemDTO> items;
}
