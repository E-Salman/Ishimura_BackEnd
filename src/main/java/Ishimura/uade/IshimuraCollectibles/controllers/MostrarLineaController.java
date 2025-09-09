package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarLineaDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarLineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/lineas")
public class MostrarLineaController {
    @Autowired
    private MostrarLineaService mostrarLineaService;

    @GetMapping
    public List<MostrarLineaDTO> listarLineas() {
        return mostrarLineaService.listarLineas();
    }
}