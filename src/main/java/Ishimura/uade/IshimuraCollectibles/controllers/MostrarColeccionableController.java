package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.Category;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.service.ColeccionableService;
import Ishimura.uade.IshimuraCollectibles.service.ImageService;
import Ishimura.uade.IshimuraCollectibles.service.MostrarColeccionableService;
import Ishimura.uade.IshimuraCollectibles.service.MostrarColeccionableNombreServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coleccionable")
public class MostrarColeccionableController {

    @Autowired
    private ColeccionableService coleccionableService;

    @Autowired
    private MostrarColeccionableService mostrarAtributosService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/{coleccionableID}")
    public MostrarColeccionableDTO mostrarAtributosID(@PathVariable Long coleccionableID) {
        return mostrarAtributosService.mostrarAtributos(coleccionableID);
    }

    @GetMapping("/{coleccionableID}/imagenes/{imagenID}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable Long coleccionableID, @PathVariable int imagenID) throws SQLException, IOException {
        ColeccionableDTO coleccionable = coleccionableService.mostrarAtributos(coleccionableID);
        Imagen imagen = imageService.viewById(coleccionable.getImagenes().get(imagenID));
        byte[] imageBytes = imagen.getImage().getBytes(1, (int) imagen.getImage().length());
        return ResponseEntity.ok()
                .header("Content-Type", "image/png") // change to "image/jpeg" if it's a JPEG
                .body(imageBytes);
    }

    @PostMapping
    public ResponseEntity<Object> crearColeccionable(@RequestBody ColeccionableDTO coleccionableDTO) {
        Coleccionable nuevoColeccionable = coleccionableService.crearColeccionable(coleccionableDTO);
        return ResponseEntity.created(URI.create("/coleccionable/" + nuevoColeccionable.getId())).body(nuevoColeccionable);
    }
}
