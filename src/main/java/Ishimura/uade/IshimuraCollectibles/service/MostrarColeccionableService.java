package Ishimura.uade.IshimuraCollectibles.service;

import java.util.Optional;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;

public interface MostrarColeccionableService {
    MostrarColeccionableDTO mostrarAtributos(Long coleccionableId);
}