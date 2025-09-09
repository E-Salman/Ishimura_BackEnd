package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;
import Ishimura.uade.IshimuraCollectibles.repository.ListarNombreColeLineaRepository;

@Service
public class ListarColeLineaServiceImpl implements ListarColeLineaService {

    @Autowired
    private ListarNombreColeLineaRepository repo;

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorLinea(Long lineaId) {
        return repo.listarColeccionablesPorLinea(lineaId);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorMarca(Long marcaId) {
        return repo.listarColeccionablesPorMarca(marcaId);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorDebajoDe(Double precio) {
        return repo.listarColeccionablesPorDebajoDe(precio);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorEncimaDe(Double precio) {
        return repo.listarColeccionablesPorEncimaDe(precio);
    }

    @Override
    public List<LineaResumenDTO> lineasPorMarca(Long marcaId) {
        return repo.listarLineasPorMarca(marcaId);
    }
}
