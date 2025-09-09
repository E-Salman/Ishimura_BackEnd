package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Linea;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostrarLineaServiceImpl implements MostrarLineaService {
    @Autowired
    private MostrarLineaRepository mostrarLineaRepository;

    @Override
    public List<MostrarLineaDTO> listarLineas() {
        List<Linea> lineas = mostrarLineaRepository.findAll();
        return lineas.stream()
                .map(l -> new MostrarLineaDTO(l.getId(), l.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public MostrarLineaDTO crearLinea(CLineaDTO cLineaDTO) {
        Linea linea = new Linea();
        linea.setNombre(cLineaDTO.getNombre());
        if (cLineaDTO.getIdMarca() != null) {
            Marca marca = new Marca();
            marca.setId(cLineaDTO.getIdMarca());
            linea.setMarca(marca);
        }
        Linea saved = mostrarLineaRepository.save(linea);
        return new MostrarLineaDTO(saved.getId(), saved.getNombre());
    }
}
