package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MostrarColeccionableNombreServiceImpl implements MostrarColeccionableService {

        @Autowired
        private MostrarColeccionableRepository mostrarAtributosRepository;

        @Override
        public MostrarColeccionableDTO mostrarAtributos(@PathVariable Long id) {
                Coleccionable col = mostrarAtributosRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("No existe coleccionable id=" + id));

                Long lineaId = col.getLinea() != null ? col.getLinea().getId() : null;
                String nombreLinea = col.getLinea() != null ? col.getLinea().getNombre() : null;
                Long marcaId = (col.getLinea() != null && col.getLinea().getMarca() != null) ? col.getLinea().getMarca().getId() : null;
                String nombreMarca = (col.getLinea() != null && col.getLinea().getMarca() != null) ? col.getLinea().getMarca().getNombre() : null;

                List<Long> idImagenes = col.getImagenes().stream().map(Imagen::getId).collect(Collectors.toList());

                return new MostrarColeccionableDTO(
                                col.getId(),
                                col.getNombre(),
                                col.getDescription(),
                                col.getPrecio(),
                                idImagenes,
                                lineaId,
                                nombreLinea,
                                marcaId,
                                nombreMarca);
        }        
}