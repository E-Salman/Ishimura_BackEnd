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
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

        @Override
        public ColeccionableDTO mostrarAtributos(@PathVariable Long id) {
                Coleccionable col = mostrarAtributosRepository.findById(id)
                                .orElseThrow(() -> new CollectibleNotFoundException(id));

                List<Long> idImagenes = col.getImagenes().stream().map(Imagen::getId).collect(Collectors.toList());

                return new ColeccionableDTO(
                                col.getNombre(),
                                col.getDescription(),
                                col.getPrecio(),
                                col.getLinea().getId(),
                                idImagenes);
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
}
