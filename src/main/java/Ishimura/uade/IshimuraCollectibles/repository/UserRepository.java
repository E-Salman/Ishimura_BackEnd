package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String mail);
    
    List<Usuario> findAllByRol(Rol rol);

    
    Optional<Usuario> findByOrdenes_Id(Long ordenId);
}
