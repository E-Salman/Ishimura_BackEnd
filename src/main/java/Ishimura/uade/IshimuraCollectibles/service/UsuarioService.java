
package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;

import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UserRepository userRepository;

    public Usuario getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No existe usuario con email: " + email));
    }

    public List<Usuario> getByRol(Rol rol) {
        return userRepository.findAllByRol(rol);
    }

    public Usuario getByOrdenId(Long ordenId) {
        return userRepository.findByOrdenes_Id(ordenId)
                .orElseThrow(() -> new IllegalArgumentException("No existe usuario para la orden: " + ordenId));
    }
}
