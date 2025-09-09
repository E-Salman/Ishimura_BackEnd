package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CrearOrdenDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenResumenDTO;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;
import Ishimura.uade.IshimuraCollectibles.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {

  private final OrdenService ordenService;
  private final UserRepository userRepository; 

  // Crear nueva orden para el usuario autenticado
  @PostMapping
  public ResponseEntity<OrdenDetalleDTO> crear(@RequestBody CrearOrdenDTO dto, Principal principal) {
    Long usuarioId = resolveUserId(principal);
    return ResponseEntity.ok(ordenService.crearOrden(usuarioId, dto));
  }

  // listar (resumen) mis órdenes: nro, monto, fecha
  @GetMapping("/mias")
  public ResponseEntity<List<OrdenResumenDTO>> mias(Principal principal) {
    Long usuarioId = resolveUserId(principal);
    return ResponseEntity.ok(ordenService.listarResumenMias(usuarioId));
  }

  // listar (resumen) de un usuario dado (solo admin)
  @GetMapping("/usuario/{usuarioId}")
  public ResponseEntity<List<OrdenResumenDTO>> deUsuario(@PathVariable Long usuarioId) {
    return ResponseEntity.ok(ordenService.listarResumenDeUsuario(usuarioId));
  }

  // detalle por número de orden
  @GetMapping("/{numeroOrden}")
  public ResponseEntity<OrdenDetalleDTO> detalle(@PathVariable String numeroOrden) {
    return ResponseEntity.ok(ordenService.detallePorNumero(numeroOrden));
  }

  private Long resolveUserId(Principal principal) {
    String email = principal.getName();
    return userRepository.findByEmail(email)
        .map(Usuario::getId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario autenticado no encontrado: " + email));
  }
}

