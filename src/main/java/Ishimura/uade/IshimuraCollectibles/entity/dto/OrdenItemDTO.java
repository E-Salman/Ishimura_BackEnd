package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.math.BigDecimal;

public record OrdenItemDTO(
    Long coleccionableId,
    String nombre,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {}
