package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Orden;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

  Optional<Orden> findByNumeroOrden(String numeroOrden);
  List<Orden> findAllByUsuario_Id(Long usuarioId);
  boolean existsByNumeroOrden(String numeroOrden);

  // filtro x monto
  List<Orden> findAllByMontoTotalLessThanEqual(BigDecimal monto);
  List<Orden> findAllByMontoTotalGreaterThanEqual(BigDecimal monto);

  // filtro x fecha
  List<Orden> findAllByCreadaEnBefore(LocalDateTime fecha);
  List<Orden> findAllByCreadaEn(LocalDateTime fecha);
  List<Orden> findAllByCreadaEnAfter(LocalDateTime fecha);

  // filtro x metodo de pago
  List<Orden> findAllByMetodoPago(String metodoPago);

}