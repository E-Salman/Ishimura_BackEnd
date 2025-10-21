package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.ItemCarrito;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CarritoItemDTO;
import Ishimura.uade.IshimuraCollectibles.repository.ColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.ItemCarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private ItemCarritoRepository carritoRepo;

    @Autowired
    private ColeccionableRepository coleccionableRepo;

    public List<CarritoItemDTO> obtenerCarrito(Long usuarioId) {
        List<ItemCarrito> items = carritoRepo.findByUsuarioId(usuarioId);

        return items.stream()
                .map(i -> {
                    Coleccionable c = i.getColeccionable();
                    String imagen = (c.getImagenes() != null && !c.getImagenes().isEmpty())
                            ? "http://localhost:4002/imagenes?id=" + c.getImagenes().get(0).getId()
                            : null;

                    return new CarritoItemDTO(
                            i.getId(),
                            c.getId(),
                            c.getNombre(),
                            c.getPrecio(),
                            i.getCantidad(),
                            imagen
                    );
                })
                .collect(Collectors.toList());
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
