package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.entity.dto.*;
import Ishimura.uade.IshimuraCollectibles.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

  private final OrdenRepository ordenRepository;
  private final UserRepository userRepository;                          // ya existe
  private final MostrarColeccionableRepository coleccionableRepository; // ya existe (también sirve para findById)

  // =================== Obligatorios ===================

  @Override
  @Transactional
  public OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto) {
    var usuario = userRepository.findById(usuarioId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    var orden = new Orden();
    orden.setNumeroOrden(generarNumeroOrden());
    orden.setUsuario(usuario);
    orden.setMetodoPago(dto.metodoPago());
    orden.setCreadaEn(LocalDateTime.now());
    orden.setMontoTotal(BigDecimal.ZERO);

    // construir items desde los ItemDTO
    for (ItemDTO i : dto.items()) {
      var col = coleccionableRepository.findById(i.coleccionableId())
          .orElseThrow(() -> new IllegalArgumentException("Coleccionable no encontrado: " + i.coleccionableId()));

      var precioUnit = BigDecimal.valueOf(col.getPrecio()); // si luego migran a BigDecimal en Coleccionable, mejor
      var subtotal = precioUnit.multiply(BigDecimal.valueOf(i.cantidad()));

      var item = new OrdenItem();
      item.setOrden(orden);
      item.setColeccionable(col);
      item.setCantidad(i.cantidad());
      item.setPrecioUnitario(precioUnit);
      item.setSubtotal(subtotal);

      orden.getArticulos().add(item); // en tu entity el campo se llama 'articulos'
    }

    // total
    var total = orden.getArticulos().stream()
        .map(OrdenItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    orden.setMontoTotal(total);

    var guardada = ordenRepository.save(orden); // cascade guarda items
    return toDetalleDTO(guardada);
  }

  @Override
  public List<OrdenResumenDTO> listarResumenMias(Long usuarioId) {
    return ordenRepository.findAllByUsuario_Id(usuarioId).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarResumenDeUsuario(Long usuarioId) {
    return listarResumenMias(usuarioId); // misma lógica; control de rol se hace en el controller
  }

  @Override
  public OrdenDetalleDTO detallePorNumero(String numeroOrden) {
    var orden = ordenRepository.findByNumeroOrden(numeroOrden)
        .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
    return toDetalleDTO(orden);
  }

  // =================== Menor prioridad ===================

  @Override
  public List<OrdenResumenDTO> listarPorMontoMenorA(BigDecimal max) {
    return ordenRepository.findByMontoTotalLessThanEqualOrderByCreadaEnDesc(max).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarPorMontoMayorA(BigDecimal min) {
    return ordenRepository.findByMontoTotalGreaterThanEqualOrderByCreadaEnDesc(min).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarAntesDe(LocalDateTime fecha) {
    return ordenRepository.findByCreadaEnBeforeOrderByCreadaEnDesc(fecha).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarEnFecha(LocalDateTime desdeIncl, LocalDateTime hastaExcl) {
    return ordenRepository.findByCreadaEnBetweenOrderByCreadaEnDesc(desdeIncl, hastaExcl).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarDespuesDe(LocalDateTime fecha) {
    return ordenRepository.findByCreadaEnAfterOrderByCreadaEnDesc(fecha).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarPorMetodoPago(String metodo) {
    return ordenRepository.findByMetodoPagoIgnoreCaseOrderByCreadaEnDesc(metodo).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarQueContienenColeccionable(Long coleccionableId) {
    return ordenRepository.findDistinctByArticulos_Coleccionable_IdOrderByCreadaEnDesc(coleccionableId).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  // =================== helpers ===================

  private OrdenDetalleDTO toDetalleDTO(Orden o) {
    var items = o.getArticulos().stream().map(it ->
        new OrdenItemDTO(
            it.getColeccionable().getId(),
            it.getColeccionable().getNombre(),
            it.getCantidad(),
            it.getPrecioUnitario(),
            it.getSubtotal()
        )
    ).toList();

    return new OrdenDetalleDTO(
        o.getNumeroOrden(),
        o.getMontoTotal(),
        o.getMetodoPago(),
        o.getCreadaEn(),
        items
    );
  }

  private String generarNumeroOrden() {
    return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}
