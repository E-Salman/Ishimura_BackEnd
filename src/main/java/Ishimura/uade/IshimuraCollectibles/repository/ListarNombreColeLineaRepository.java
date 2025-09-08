package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ListarNombreColeLineaRepository extends JpaRepository<Coleccionable, Long> {

    Optional<Coleccionable> findFirstByNombreIgnoreCase(String nombre);

    @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableResumenDTO(c.id, c.nombre) from Coleccionable c where c.linea.id = :lineaId")
    List<ListarColeLineaDTO> listarColeccionablesPorLinea(@Param("lineaId") Long lineaId);

    @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableResumenDTO(c.id, c.nombre) from Coleccionable c where c.linea.marca.id = :marcaId")
    List<ListarColeLineaDTO> listarColeccionablesPorMarca(@Param("marcaId") Long marcaId);

    @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableResumenDTO(c.id, c.nombre) from Coleccionable c where c.precio <= :precio")
    List<ListarColeLineaDTO> listarColeccionablesPorDebajoDe(@Param("precio") Double precio);

    @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableResumenDTO(c.id, c.nombre) from Coleccionable c where c.precio >= :precio")
    List<ListarColeLineaDTO> listarColeccionablesPorEncimaDe(@Param("precio") Double precio);

    @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO(l.id, l.nombre) from Linea l where l.marca.id = :marcaId")
    List<LineaResumenDTO> listarLineasPorMarca(@Param("marcaId") Long marcaId);
}
