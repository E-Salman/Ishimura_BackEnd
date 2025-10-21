package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.repository.*;

@Service
public class WishlistService {

    @Autowired
    private ItemWishlistRepository wishlistRepo;

    @Autowired
    private ColeccionableRepository coleccionableRepo;

    public List<ItemWishlist> obtenerWishlist(Long usuarioId) {
        return wishlistRepo.findByUsuarioId(usuarioId);
    }

    public ItemWishlist agregarAWishlist(Usuario usuario, Long coleccionableId) {
        boolean existe = wishlistRepo.existsByUsuarioIdAndColeccionableId(usuario.getId(), coleccionableId);
        if (existe) throw new RuntimeException("El coleccionable ya está en la wishlist");

        Coleccionable coleccionable = coleccionableRepo.findById(coleccionableId)
                .orElseThrow(() -> new RuntimeException("Coleccionable no encontrado"));

        ItemWishlist item = new ItemWishlist();
        item.setUsuario(usuario);
        item.setColeccionable(coleccionable);

        return wishlistRepo.save(item);
    }

    public void eliminarItem(Long id) {
        wishlistRepo.deleteById(id);
    }

    public void vaciarWishlist(Long usuarioId) {
        List<ItemWishlist> items = wishlistRepo.findByUsuarioId(usuarioId);
        wishlistRepo.deleteAll(items);
    }
}