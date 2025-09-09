package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;
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
        List<Marca> marcas = mostrarMarcaRepository.findAll();
        return marcas.stream()
                .map(m -> new MostrarMarcaDTO(m.getId(), m.getNombre()))
                .collect(Collectors.toList());
    }
}
