package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.service.ImageService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("imagenes")
public class ImagesController {
    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws SQLException, IOException {
        Imagen image = imageService.viewById(id);
        byte[] imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());

        return ResponseEntity.ok()
                .header("Content-Type", "image/png") // change to "image/jpeg" if it's a JPEG
                .body(imageBytes);
    }
    @PostMapping()
    public String addImagePost(@ModelAttribute AddFileRequest request) throws IOException, SerialException, SQLException {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            return "Archivo vacío";
        }
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        imageService.create(Imagen.builder().image(blob).build());
        return "created";
    }
    
}
