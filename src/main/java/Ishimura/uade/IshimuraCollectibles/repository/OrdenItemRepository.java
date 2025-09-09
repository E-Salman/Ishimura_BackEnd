package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.OrdenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
  List<OrdenItem> findAllByOrden_Id(Long ordenId);
  List<OrdenItem> findAllByOrden_NumeroOrden(String numeroOrden);
}
