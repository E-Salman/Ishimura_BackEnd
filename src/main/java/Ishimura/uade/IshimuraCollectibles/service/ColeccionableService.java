package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;

public interface ColeccionableService {
    ColeccionableDTO mostrarAtributos(Long coleccionableId);

    Coleccionable crearColeccionable(ColeccionableDTO coleccionableDTO);
}