package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostrarMarcaServiceImpl implements MostrarMarcaService {

    @Autowired
    private MostrarMarcaRepository mostrarMarcaRepository;
    @Override
    public List<MostrarMarcaDTO> listarMarcas() {
        return mostrarMarcaRepository.findAll().stream()
                .map(m -> new MostrarMarcaDTO(m.getId(), m.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MostrarMarcaDTO crearMarca(MostrarMarcaDTO dto) {
        Marca entity = new Marca();
        entity.setNombre(dto.getNombre().trim()); 
        Marca saved = mostrarMarcaRepository.save(entity);
        return new MostrarMarcaDTO(saved.getId(), saved.getNombre());
    }
}
    

