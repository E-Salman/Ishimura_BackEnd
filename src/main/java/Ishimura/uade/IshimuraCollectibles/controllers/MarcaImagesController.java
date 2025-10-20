package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.service.MarcaImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/marcasImages")
public class MarcaImagesController {

  @Autowired
  private MarcaImageService marcaImageService;

  // Lista los IDs de imágenes de una marca (vacía si no tiene)
  @GetMapping("/{marcaId}/imagenes")
  public ResponseEntity<List<Long>> list(@PathVariable Long marcaId) {
    return ResponseEntity.ok(marcaImageService.listIdsByMarca(marcaId));
  }

  // Devuelve bytes de la primera imagen de la marca
  @GetMapping("/{marcaId}/imagenes/primera")
  public ResponseEntity<byte[]> firstImage(@PathVariable Long marcaId) throws SQLException, IOException {
    Imagen img = marcaImageService.viewFirstByMarca(marcaId);
    byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
    return ResponseEntity.ok().header("Content-Type", "image/png").body(imageBytes);
  }

  // Devuelve bytes por ID de imagen (independiente de la marca)
  @GetMapping("/imagenes/{imageId}")
  public ResponseEntity<byte[]> byId(@PathVariable Long imageId) throws SQLException, IOException {
    Imagen img = marcaImageService.viewById(imageId);
    byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
    return ResponseEntity.ok().header("Content-Type", "image/png").body(imageBytes);
  }

  // Sube una nueva imagen para la marca
  @PostMapping("/{marcaId}/imagenes")
  public ResponseEntity<Long> upload(@PathVariable Long marcaId, @RequestParam("file") MultipartFile file)
      throws IOException, SerialException, SQLException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Archivo vacio");
    }
    Blob blob = new SerialBlob(file.getBytes());
    Imagen saved = marcaImageService.create(Imagen.builder().image(blob).build(), marcaId);
    return ResponseEntity.ok(saved.getId());
  }
}
