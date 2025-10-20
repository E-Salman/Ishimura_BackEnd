package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.MarcaImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.exceptions.ImageNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.MarcaNotFoundException;

@Service
public class MarcaImageServiceImpl implements MarcaImageService {
    @Autowired
    private MarcaImageRepository marcaImageRepository;

    @Autowired
    private MostrarMarcaRepository marcaRepository;

    @Override
    public Imagen create(Imagen image, Long idMarca) {
        Marca marca = marcaRepository.findById(idMarca)
            .orElseThrow(() -> new MarcaNotFoundException(idMarca));

        // asegurar exclusividad: setear marca y limpiar coleccionable
        image.setMarca(marca);
        image.setColeccionable(null);
        return marcaImageRepository.save(image);
    }

    @Override
    public Imagen viewById(long id) {
        return marcaImageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Override
    public java.util.List<Long> listIdsByMarca(Long idMarca) {
        // Asegura existencia de la marca para diferenciar 404 de "sin imágenes"
        if (!marcaRepository.existsById(idMarca)) {
            throw new MarcaNotFoundException(idMarca);
        }
        return marcaImageRepository.findByMarcaIdOrderByIdAsc(idMarca)
                .stream()
                .map(Imagen::getId)
                .map(Long::valueOf)
                .toList();
    }

    @Override
    public Imagen viewFirstByMarca(Long idMarca) {
        Long imgId = marcaImageRepository.findTopIdByMarcaId(idMarca)
                .orElseThrow(() -> new ImageNotFoundException(idMarca));
        return viewById(imgId);
    }
}
