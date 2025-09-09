package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenResumenDTO(
    String numeroOrden,
    BigDecimal montoTotal,
    LocalDateTime creadaEn
) {}
