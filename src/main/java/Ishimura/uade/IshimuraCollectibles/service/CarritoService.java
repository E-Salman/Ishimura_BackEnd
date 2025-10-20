package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.repository.*;

@Service
public class CarritoService {

    @Autowired
    private ItemCarritoRepository carritoRepo;

    @Autowired
    private ColeccionableRepository coleccionableRepo;

    public List<ItemCarrito> obtenerCarrito(Long usuarioId) {
        return carritoRepo.findByUsuarioId(usuarioId);
    }

    public ItemCarrito agregarAlCarrito(Usuario usuario, Long coleccionableId, int cantidad) {
        Coleccionable coleccionable = coleccionableRepo.findById(coleccionableId)
                .orElseThrow(() -> new RuntimeException("Coleccionable no encontrado"));

        ItemCarrito item = new ItemCarrito();
        item.setUsuario(usuario);
        item.setColeccionable(coleccionable);
        item.setCantidad(cantidad);

        return carritoRepo.save(item);
    }

    public void eliminarItem(Long id) {
        carritoRepo.deleteById(id);
    }

    public void vaciarCarrito(Long usuarioId) {
        List<ItemCarrito> items = carritoRepo.findByUsuarioId(usuarioId);
        carritoRepo.deleteAll(items);
    }
}

