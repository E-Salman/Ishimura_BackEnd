package Ishimura.uade.IshimuraCollectibles.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.service.CarritoService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<ItemCarrito>> verCarrito() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<ItemCarrito> carrito = carritoService.obtenerCarrito(usuario.getId());
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{coleccionableId}")
    public ResponseEntity<ItemCarrito> agregar(
            @PathVariable Long coleccionableId,
            @RequestParam(defaultValue = "1") int cantidad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        ItemCarrito item = carritoService.agregarAlCarrito(usuario, coleccionableId, cantidad);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        carritoService.vaciarCarrito(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}


