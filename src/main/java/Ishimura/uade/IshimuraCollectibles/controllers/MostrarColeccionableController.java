package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarColeccionableService;
import Ishimura.uade.IshimuraCollectibles.service.MostrarColeccionableServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/coleccionable")
public class MostrarColeccionableController {

    @Autowired
    private MostrarColeccionableService mostrarAtributosService;

    @GetMapping("/{coleccionableID}")
    public MostrarColeccionableDTO mostrarAtributosID(@PathVariable Long coleccionableID) {
        return mostrarAtributosService.mostrarAtributos(coleccionableID);
    }

    // Cree el post pero entiendo no lo necesita ya que no debo intentar de crear
    // nada, solo pido listado de atributos
    // @PostMapping("path")
    // public String postMethodName(@RequestBody String entity) {
    // TODO: process POST request

    // return entity;
    // }

}
