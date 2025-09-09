package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/marcas")
public class MostrarMarcaController {
    @Autowired
    private MostrarMarcaService mostrarMarcaService;

    @GetMapping
    public List<MostrarMarcaDTO> listarMarcas() {
        return mostrarMarcaService.listarMarcas();
    }
}
