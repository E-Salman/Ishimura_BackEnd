package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.entity.Category;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.Linea;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.CatalogoRepository;
import Ishimura.uade.IshimuraCollectibles.repository.ImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.ImageNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.LineaNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleDuplicateException;
import Ishimura.uade.IshimuraCollectibles.repository.ItemCarritoRepository;
import Ishimura.uade.IshimuraCollectibles.repository.ItemWishlistRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionableServiceImpl implements ColeccionableService {

        @Autowired
        private CatalogoRepository catalogoRepository;

        @Autowired
        private MostrarLineaRepository lineaRepository;

        @Autowired
        private MostrarColeccionableRepository mostrarAtributosRepository;

        @Autowired
        private ImageRepository imageRepository;

        @Autowired
        private ItemCarritoRepository itemCarritoRepository;

        @Autowired
        private ItemWishlistRepository itemWishlistRepository;

        @Override
        public MostrarColeccionableDTO mostrarAtributosID(@PathVariable Long id) {
                Coleccionable col = mostrarAtributosRepository.findById(id)
                                .orElseThrow(() -> new CollectibleNotFoundException(id));

                List<Long> idImagenes = col.getImagenes().stream().map(Imagen::getId).collect(Collectors.toList());
                Long lineaId = col.getLinea() != null ? col.getLinea().getId() : null;
                String nombreLinea = col.getLinea() != null ? col.getLinea().getNombre() : null;
                Long marcaId = (col.getLinea() != null && col.getLinea().getMarca() != null) ? col.getLinea().getMarca().getId() : null;
                String nombreMarca = (col.getLinea() != null && col.getLinea().getMarca() != null) ? col.getLinea().getMarca().getNombre() : null;

                return new MostrarColeccionableDTO(
                                col.getId(),
                                col.getNombre(),
                                col.getDescription(),
                                col.getPrecio(),
                                idImagenes,
                                lineaId,
                                nombreLinea,
                                marcaId,
                                nombreMarca);
        }

        @Override
        public Coleccionable crearColeccionable(ColeccionableDTO coleccionableDTO) {
                // Validación de duplicados: mismo nombre en la misma línea
                boolean existe = mostrarAtributosRepository
                                .existsByNombreIgnoreCaseAndLinea_Id(coleccionableDTO.getNombre(), coleccionableDTO.getLinea());
                if (existe) {
                        throw new Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleDuplicateException(
                                        coleccionableDTO.getNombre(), coleccionableDTO.getLinea());
                }
                Coleccionable coleccionable = new Coleccionable();
                coleccionable.setNombre(coleccionableDTO.getNombre());
                coleccionable.setDescription(coleccionableDTO.getDescripcion());
                coleccionable.setPrecio(coleccionableDTO.getPrecio());
                List<Imagen> imagenes = new LinkedList<>();
                Imagen tempImg;
                for (Long id : coleccionableDTO.getImagenes()) {
                        tempImg = imageRepository.findById(id)
                                   .orElseThrow(() -> new ImageNotFoundException(id));
                        imagenes.add(tempImg);
                }
                coleccionable.setImagenes(imagenes);
                Linea tempLinea = lineaRepository.findById(coleccionableDTO.getLinea())
                                .orElseThrow(() -> new LineaNotFoundException(coleccionableDTO.getLinea()));
                coleccionable.setLinea(tempLinea);
                Coleccionable saved = mostrarAtributosRepository.save(coleccionable);
                Catalogo catalogo = new Catalogo();
                catalogo.setColeccionable(saved);
                catalogo.setStock(0);
                catalogoRepository.save(catalogo);      

                return saved;
        }

        @Override
        @Transactional
        public Coleccionable actualizarColeccionable(Long id, ColeccionableDTO dto) {
                Coleccionable existente = mostrarAtributosRepository.findById(id)
                                .orElseThrow(() -> new CollectibleNotFoundException(id));

                String nuevoNombre = dto.getNombre() != null ? dto.getNombre() : existente.getNombre();
                Long nuevaLineaId = dto.getLinea() != null ? dto.getLinea() : existente.getLinea().getId();
                boolean dup = mostrarAtributosRepository
                                .existsByNombreIgnoreCaseAndLinea_IdAndIdNot(nuevoNombre, nuevaLineaId, id);
                if (dup) {
                        throw new CollectibleDuplicateException(nuevoNombre, nuevaLineaId);
                }

                if (dto.getNombre() != null) existente.setNombre(dto.getNombre());
                if (dto.getDescripcion() != null) existente.setDescription(dto.getDescripcion());
                if (dto.getPrecio() != null) existente.setPrecio(dto.getPrecio());

                if (dto.getLinea() != null) {
                        Linea nuevaLinea = lineaRepository.findById(dto.getLinea())
                                        .orElseThrow(() -> new LineaNotFoundException(dto.getLinea()));
                        existente.setLinea(nuevaLinea);
                }

                // Reemplazar imágenes asociadas si se envía la lista
                if (dto.getImagenes() != null) {
                        List<Imagen> nuevasImagenes = new LinkedList<>();
                        for (Long imgId : dto.getImagenes()) {
                                Imagen img = imageRepository.findById(imgId)
                                                .orElseThrow(() -> new ImageNotFoundException(imgId));
                                nuevasImagenes.add(img);
                        }
                        existente.setImagenes(nuevasImagenes);
                }

                return mostrarAtributosRepository.save(existente);
        }

        @Override
        @Transactional
        public void borrarColeccionable(Long id) {
                Coleccionable existente = mostrarAtributosRepository.findById(id)
                                .orElseThrow(() -> new CollectibleNotFoundException(id));

                // Borrar dependientes para evitar FK
                var imgs = imageRepository.findByColeccionableIdOrderByIdAsc(id);
                if (!imgs.isEmpty()) {
                        imageRepository.deleteAll(imgs);
                }
                itemCarritoRepository.deleteByColeccionableId(id);
                itemWishlistRepository.deleteByColeccionableId(id);
                catalogoRepository.deleteById(id);

                // Borrar coleccionable
                mostrarAtributosRepository.delete(existente);
        }
}
