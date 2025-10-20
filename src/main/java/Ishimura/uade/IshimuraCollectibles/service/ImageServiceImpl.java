package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.ImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.ImageNotFoundException;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MostrarColeccionableRepository coleccionableRepository;

    @Override
    public Imagen create(Imagen image, Long idCol) {
        Coleccionable coleccionable = coleccionableRepository.findById(idCol).orElseThrow(() -> new EntityNotFoundException("Coleccionable " + idCol + " no existe"));
        image.setColeccionable(coleccionable);
        image.setMarca(null);
        return imageRepository.save(image);
    }

    @Override
    public Imagen viewById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }
}
