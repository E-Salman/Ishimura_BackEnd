package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Orden;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

  // Ya tenés:
  Optional<Orden> findByNumeroOrden(String numeroOrden);
  List<Orden> findAllByUsuario_Id(Long usuarioId);
  boolean existsByNumeroOrden(String numeroOrden);

  // —— MENOS PRIORIDAD: FILTROS POR MONTO ——
  List<Orden> findAllByMontoTotalLessThanEqual(BigDecimal monto);
  List<Orden> findAllByMontoTotalGreaterThanEqual(BigDecimal monto);

  // —— MENOS PRIORIDAD: FILTROS POR FECHA (con LocalDateTime) ——
  List<Orden> findAllByCreadaEnBefore(LocalDateTime fecha);
  List<Orden> findAllByCreadaEn(LocalDateTime fecha);     // misma fecha+hora exacta
  List<Orden> findAllByCreadaEnAfter(LocalDateTime fecha);

  // Si querés “por día” (ignorando hora), agregá estas JPQL:
  @Query("select o from Orden o where date(o.creadaEn) = :dia")
  List<Orden> findAllByDia(@Param("dia") LocalDate dia);

  @Query("select o from Orden o where date(o.creadaEn) < :dia")
  List<Orden> findAllBeforeDia(@Param("dia") LocalDate dia);

  @Query("select o from Orden o where date(o.creadaEn) > :dia")
  List<Orden> findAllAfterDia(@Param("dia") LocalDate dia);

  // —— MENOS PRIORIDAD: POR MÉTODO DE PAGO ——
  List<Orden> findAllByMetodoPago(String metodoPago);

  // —— SOLO ADMIN: ÓRDENES QUE CONTIENEN UN COLECCIONABLE ——
  @Query("select distinct o from Orden o join o.articulos a where a.coleccionable.id = :coleccionableId")
  List<Orden> findAllByColeccionableId(@Param("coleccionableId") Long coleccionableId);
  
  List<Orden> findByMontoTotalLessThanEqualOrderByCreadaEnDesc(BigDecimal max);
  List<Orden> findByMontoTotalGreaterThanEqualOrderByCreadaEnDesc(BigDecimal min);
  
  List<Orden> findByCreadaEnBeforeOrderByCreadaEnDesc(LocalDateTime fecha);
  List<Orden> findByCreadaEnAfterOrderByCreadaEnDesc(LocalDateTime fecha);
  List<Orden> findByCreadaEnBetweenOrderByCreadaEnDesc(LocalDateTime desdeIncl, LocalDateTime hastaExcl);
  
  List<Orden> findByMetodoPagoIgnoreCaseOrderByCreadaEnDesc(String metodo);
  
  List<Orden> findDistinctByArticulos_Coleccionable_IdOrderByCreadaEnDesc(Long coleccionableId);
  
}