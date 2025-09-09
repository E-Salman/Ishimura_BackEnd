package Ishimura.uade.IshimuraCollectibles.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.repository.CatalogoRepository;

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
        return repo.findById(coleccionableId).orElseThrow();
    }

    public void cambiarStock(Long coleccionableId, int nuevoStock) {
        Catalogo item = repo.findById(coleccionableId).orElseThrow();
        item.setStock(nuevoStock);
        repo.save(item);
    }

    public void incrementarStock(Long coleccionableId, int cantidadIncrementar) {
        Catalogo item = repo.findById(coleccionableId).orElseThrow();
        int stockActual = item.getStock();
        repo.updateStock(coleccionableId, cantidadIncrementar + stockActual);
        repo.save(item);
    }

    public void decrementarStock(Long coleccionableId, int cantidadIncrementar) {
        Catalogo item = repo.findById(coleccionableId).orElseThrow();
        int stockActual = item.getStock();
        if (stockActual > 0) {
            repo.updateStock(coleccionableId, stockActual - cantidadIncrementar);
            repo.save(item);
        } else {
            throw new IllegalArgumentException("No se puede decrementar el stock por debajo de 0");
        }
    }
}
