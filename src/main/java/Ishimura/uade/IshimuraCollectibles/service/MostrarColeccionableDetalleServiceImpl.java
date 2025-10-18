package Ishimura.uade.IshimuraCollectibles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableNombreDTO;
import Ishimura.uade.IshimuraCollectibles.repository.ListarNombreColeLineaRepository;

@Service
public class MostrarColeccionableDetalleServiceImpl implements MostrarColeccionableNombreService {

    @Autowired
    private ListarNombreColeLineaRepository repo;

    @Override
    public ColeccionableNombreDTO detallePorNombre(String nombre) {
        Coleccionable c = repo.findFirstByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException("nombre=" + nombre));
        return new ColeccionableNombreDTO(c.getId(), c.getDescription(), c.getImagenes());
    }
}
