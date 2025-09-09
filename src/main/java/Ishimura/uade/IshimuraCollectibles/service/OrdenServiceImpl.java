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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MostrarColeccionableRepository coleccionableRepository;

    @Override
    public OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Orden orden = new Orden();
        orden.setNumeroOrden(generarNumeroOrden());
        orden.setUsuario(usuario);
        orden.setMetodoPago(dto.getMetodoPago());
        orden.setCreadaEn(LocalDateTime.now());
        orden.setMontoTotal(BigDecimal.ZERO);

        // construir items (precioUnitario/subtotal quedan guardados en OrdenItem)
        for (ItemDTO i : dto.getItems()) {
            var col = coleccionableRepository.findById(i.getColeccionableId())
                    .orElseThrow(() -> new IllegalArgumentException("Coleccionable no encontrado: " + i.getColeccionableId()));

            BigDecimal precioUnit = BigDecimal.valueOf(col.getPrecio());
            BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(i.getCantidad()));

            OrdenItem item = new OrdenItem();
            item.setOrden(orden);
            item.setColeccionable(col);
            item.setCantidad(i.getCantidad());
            item.setPrecioUnitario(precioUnit);
            item.setSubtotal(subtotal);

            orden.getArticulos().add(item); 
        }

        // calcular total
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
        Orden orden = ordenRepository.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        return toDetalleDTO(orden);
    }

    private OrdenDetalleDTO toDetalleDTO(Orden o) {
        List<OrdenItemDTO> items = o.getArticulos().stream().map(it -> {
            OrdenItemDTO dto = new OrdenItemDTO();
            dto.setColeccionableId(it.getColeccionable().getId());
            dto.setNombre(it.getColeccionable().getNombre());
            dto.setCantidad(it.getCantidad());
            dto.setPrecioUnitario(it.getPrecioUnitario());
            dto.setSubtotal(it.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        OrdenDetalleDTO dto = new OrdenDetalleDTO();
        dto.setNumeroOrden(o.getNumeroOrden());
        dto.setMontoTotal(o.getMontoTotal());
        dto.setMetodoPago(o.getMetodoPago());
        dto.setCreadaEn(o.getCreadaEn());
        dto.setItems(items);
        return dto;
    }

    private String generarNumeroOrden() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

