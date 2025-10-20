package Ishimura.uade.IshimuraCollectibles.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.service.CarritoService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET - listar carrito del usuario
    @GetMapping
    public ResponseEntity<List<ItemCarrito>> verCarrito(@RequestParam Long usuarioId) {
        List<ItemCarrito> carrito = carritoService.obtenerCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    // POST - agregar producto al carrito
    @PostMapping("/{coleccionableId}")
    public ResponseEntity<ItemCarrito> agregar(
            @RequestParam Long usuarioId,
            @PathVariable Long coleccionableId,
            @RequestParam(defaultValue = "1") int cantidad) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        ItemCarrito item = carritoService.agregarAlCarrito(usuario, coleccionableId, cantidad);
        return ResponseEntity.ok(item);
    }

    // DELETE - eliminar un item del carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE - vaciar carrito completo
    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciar(@RequestParam Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
