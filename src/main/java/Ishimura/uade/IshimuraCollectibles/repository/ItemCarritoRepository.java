package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ishimura.uade.IshimuraCollectibles.entity.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(Long usuarioId);
}