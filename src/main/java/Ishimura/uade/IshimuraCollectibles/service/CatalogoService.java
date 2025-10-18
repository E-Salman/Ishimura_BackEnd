package Ishimura.uade.IshimuraCollectibles.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.repository.CatalogoRepository;
import Ishimura.uade.IshimuraCollectibles.exceptions.CatalogItemNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.InvalidDomainStateException;

@Service
public class CatalogoService {

    private final CatalogoRepository repo;

    @Autowired
    public CatalogoService(CatalogoRepository repo) {
        this.repo = repo;
    }

    public List<Catalogo> getAll() {
        return repo.findAll();
    }
    
    public Catalogo stockProducto(Long coleccionableId) {
        return repo.findById(coleccionableId)
                   .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
    }

    public void cambiarStock(Long coleccionableId, int nuevoStock) {
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        item.setStock(nuevoStock);
        repo.save(item);
    }

    public void incrementarStock(Long coleccionableId, int cantidadIncrementar) {
        if (cantidadIncrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor que 0");
        }
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        int stockActual = item.getStock();
        repo.updateStock(coleccionableId, cantidadIncrementar + stockActual);
        repo.save(item);
    }

    public void decrementarStock(Long coleccionableId, int cantidadIncrementar) {
        if (cantidadIncrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a decrementar debe ser mayor que 0");
        }
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        int stockActual = item.getStock();
        int nuevo = stockActual - cantidadIncrementar;
        if (nuevo < 0) {
            throw new InvalidDomainStateException("No se puede decrementar el stock por debajo de 0");
        }
        repo.updateStock(coleccionableId, nuevo);
        repo.save(item);
    }
}
