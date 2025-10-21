package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Orden;
import Ishimura.uade.IshimuraCollectibles.entity.OrdenItem;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CrearOrdenDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ItemDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenItemDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenResumenDTO;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.OrdenRepository;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.exceptions.UserNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.OrderNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdenServiceImpl implements OrdenService {

  private final OrdenRepository ordenRepository;
  private final UserRepository userRepository; // ya existe
  private final MostrarColeccionableRepository coleccionableRepository; // ya existe (también sirve para findById)
  @Autowired
  private PricingService pricingService;

  public OrdenServiceImpl(
      OrdenRepository ordenRepository,
      UserRepository userRepository,
      MostrarColeccionableRepository coleccionableRepository) {
    this.ordenRepository = ordenRepository;
    this.userRepository = userRepository;
    this.coleccionableRepository = coleccionableRepository;
  }

  @Override
  public OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto) {
    Usuario usuario = userRepository.findById(usuarioId)
        .orElseThrow(() -> new UserNotFoundException(usuarioId));

    Orden orden = new Orden();
    orden.setNumeroOrden(generarNumeroOrden());
    orden.setUsuario(usuario);
    orden.setMetodoPago(dto.getMetodoPago());
    orden.setCreadaEn(LocalDateTime.now());
    orden.setMontoTotal(BigDecimal.ZERO);

    for (ItemDTO i : dto.getItems()) {
      var col = coleccionableRepository.findById(i.getColeccionableId())
          .orElseThrow(() -> new CollectibleNotFoundException(i.getColeccionableId()));

      var quote = pricingService.quoteForColeccionable(col, i.getCantidad(), null);
      BigDecimal precioUnit = quote.getPrecioEfectivo();
      BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(i.getCantidad()));

      OrdenItem item = new OrdenItem();
      item.setOrden(orden);
      item.setColeccionable(col);
      item.setCantidad(i.getCantidad());
      item.setPrecioUnitario(precioUnit);
      item.setSubtotal(subtotal);

      orden.getArticulos().add(item);
    }

    BigDecimal total = orden.getArticulos().stream()
        .map(OrdenItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    orden.setMontoTotal(total);

    Orden guardada = ordenRepository.save(orden);
    return toDetalleDTO(guardada);
  }

  @Override
  public List<OrdenResumenDTO> listarResumenMias(Long usuarioId) {
    return ordenRepository.findAllByUsuario_Id(usuarioId).stream()
        .map(o -> {
          OrdenResumenDTO out = new OrdenResumenDTO();
          out.setNumeroOrden(o.getNumeroOrden());
          out.setMontoTotal(o.getMontoTotal());
          out.setCreadaEn(o.getCreadaEn());
          return out;
        })
        .collect(Collectors.toList());
  }

  @Override
  public List<OrdenResumenDTO> listarResumenDeUsuario(Long usuarioId) {
    // misma logica- el control de rol ADMIN lo hace el Controller/Security
    return listarResumenMias(usuarioId);
  }

  @Override
  public OrdenDetalleDTO detallePorNumero(String numeroOrden) {
    var orden = ordenRepository.findByNumeroOrden(numeroOrden)
        .orElseThrow(() -> new OrderNotFoundException(numeroOrden));
    return toDetalleDTO(orden);
  }

  @Override
  public List<OrdenDetalleDTO> listarMisOrdenesDetalle(Long usuarioId) {
    return ordenRepository.findAllByUsuarioIdDetailed(usuarioId)
        .stream().map(this::toOrdenDetalleDTOUser).toList();
  }

  @Override
  public List<OrdenDetalleDTO> listarTodasOrdenesDetalle() {
    return ordenRepository.findAllDetailed()
        .stream().map(this::toOrdenDetalleDTOAdmin).toList();
  }

  @Override
  public List<OrdenDetalleDTO> listarOrdenesDetallePorUsuario(Long usuarioId) {
    return ordenRepository.findAllByUsuarioIdDetailedAdmin(usuarioId)
        .stream().map(this::toOrdenDetalleDTOAdmin).toList();
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
    var items = o.getArticulos().stream().map(it -> new OrdenItemDTO(
        it.getColeccionable().getId(),
        it.getColeccionable().getNombre(),
        it.getCantidad(),
        it.getPrecioUnitario(),
        it.getSubtotal())).toList();

    return new OrdenDetalleDTO(
        o.getNumeroOrden(),
        o.getMontoTotal(),
        o.getMetodoPago(),
        o.getCreadaEn(),
        items);
  }

  private String generarNumeroOrden() {
    return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  private OrdenDetalleDTO toOrdenDetalleDTOUser(Orden o) {
    var items = o.getArticulos().stream().map(this::toItemDTO).toList();
    var totalCalc = items.stream().map(OrdenItemDTO::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    var total = (o.getMontoTotal() != null) ? o.getMontoTotal() : totalCalc;

    return new OrdenDetalleDTO(
        o.getNumeroOrden(),
        total,
        o.getMetodoPago(),
        o.getCreadaEn(),
        items);
  }

  private OrdenDetalleDTO toOrdenDetalleDTOAdmin(Orden o) {
    // mismo cuerpo que USER; si quisieras agregar info de usuario,
    // se podría extender el DTO, pero me pediste usar el tuyo actual.
    return toOrdenDetalleDTOUser(o);
  }

  private OrdenItemDTO toItemDTO(OrdenItem it) {
    var c = it.getColeccionable();
    var subtotal = it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad()));
    return new OrdenItemDTO(
        c.getId(),
        c.getNombre(),
        it.getCantidad(),
        it.getPrecioUnitario(),
        subtotal);
  }

  private OrdenResumenDTO toOrdenResumenDTO(Orden o) {
    var total = (o.getMontoTotal() != null)
        ? o.getMontoTotal()
        : o.getArticulos().stream()
            .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new OrdenResumenDTO(
        o.getNumeroOrden(),
        total,
        o.getCreadaEn());
  }
}
