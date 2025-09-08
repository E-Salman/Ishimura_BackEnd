package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;

import org.springframework.stereotype.Service;

@Service
public interface ImageService {
    public Imagen create(Imagen image);

    public Imagen viewById(long id);
}
