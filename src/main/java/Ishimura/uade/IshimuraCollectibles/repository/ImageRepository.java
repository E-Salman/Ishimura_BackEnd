package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByColeccionableIdOrderByIdAsc(Long coleccionableId);

    @Query("""
               select i.id
               from Imagen i
               where i.coleccionable.id = :coleId
               order by i.id
            """)
    Optional<Long> findTopIdByColeccionableId(@Param("coleId") Long coleId);
}
