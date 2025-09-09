package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.CrearOrdenDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenResumenDTO;

import java.util.List;

public interface OrdenService {

  // nueva orden de compra
  OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto);

  // listar nro compra, monto y fecha
  List<OrdenResumenDTO> listarResumenMias(Long usuarioId);

  // listar nro compra, monto y fecha (para ADMIN)
  List<OrdenResumenDTO> listarResumenDeUsuario(Long usuarioId);

  // devolver nro, monto, fecha, coleccionables y método de pago
  OrdenDetalleDTO detallePorNumero(String numeroOrden);
}
