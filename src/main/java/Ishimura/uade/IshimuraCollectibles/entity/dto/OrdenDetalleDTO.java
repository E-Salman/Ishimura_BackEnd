package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenDetalleDTO(
    String numeroOrden,
    BigDecimal montoTotal,
    String metodoPago,
    LocalDateTime creadaEn,
    List<OrdenItemDTO> items
) {}
