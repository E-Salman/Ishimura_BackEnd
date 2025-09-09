package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.util.List;

public record CrearOrdenDTO(
    String metodoPago,
    List<ItemDTO> items
) {}

