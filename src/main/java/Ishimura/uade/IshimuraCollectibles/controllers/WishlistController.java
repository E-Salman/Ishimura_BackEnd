package Ishimura.uade.IshimuraCollectibles.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.service.WishlistService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = "http://localhost:5173")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<ItemWishlist>> verWishlist() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<ItemWishlist> wishlist = wishlistService.obtenerWishlist(usuario.getId());
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{coleccionableId}")
    public ResponseEntity<ItemWishlist> agregar(
            @PathVariable Long coleccionableId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        ItemWishlist item = wishlistService.agregarAWishlist(usuario, coleccionableId);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        wishlistService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        wishlistService.vaciarWishlist(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}

