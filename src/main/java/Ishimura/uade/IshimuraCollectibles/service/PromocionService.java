package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Promocion;
import Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType;
import Ishimura.uade.IshimuraCollectibles.entity.PromotionType;
import Ishimura.uade.IshimuraCollectibles.exceptions.PromotionConflictException;
import Ishimura.uade.IshimuraCollectibles.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromocionService {
  private final PromocionRepository repo;

  public PromocionService(PromocionRepository repo) {
    this.repo = repo;
  }

  public Promocion crear(Promocion p) {
    validarCampos(p);
    validarConflicto(p, null);
    return repo.save(p);
  }

  public Promocion actualizar(Long id, Promocion body) {
    Promocion p = repo.findById(id).orElseThrow();
    // aplicar cambios (parciales)
    if (body.getTipo() != null)
      p.setTipo(body.getTipo());
    if (body.getValor() != null)
      p.setValor(body.getValor());
    if (body.getScopeType() != null)
      p.setScopeType(body.getScopeType());
    if (body.getScopeId() != null)
      p.setScopeId(body.getScopeId());
    if (body.getInicio() != null)
      p.setInicio(body.getInicio());
    if (body.getFin() != null)
      p.setFin(body.getFin());
    if (body.getPrioridad() != 0)
      p.setPrioridad(body.getPrioridad());
    p.setActiva(body.isActiva());
    p.setStackable(body.isStackable());

    validarCampos(p);
    validarConflicto(p, id);
    return repo.save(p);
  }

  private void validarConflicto(Promocion p, Long ignoreId) {
    if (!p.isActiva())
      return; // solo nos importan las activas
    if (p.getScopeType() != PromotionScopeType.ITEM || p.getScopeId() == null)
      return; // regla: solo ITEM

    List<Promocion> activas = repo.findByScopeTypeAndScopeIdAndActiva(PromotionScopeType.ITEM, p.getScopeId(), true);
    LocalDateTime s1 = p.getInicio();
    LocalDateTime e1 = p.getFin();
    for (Promocion other : activas) {
      if (ignoreId != null && other.getId().equals(ignoreId))
        continue;
      if (overlaps(s1, e1, other.getInicio(), other.getFin())) {
        throw new PromotionConflictException(p.getScopeId());
      }
    }
  }

  private boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd,
      LocalDateTime bStart, LocalDateTime bEnd) {
    LocalDateTime startA = aStart == null ? LocalDateTime.MIN : aStart;
    LocalDateTime endA = aEnd == null ? LocalDateTime.MAX : aEnd;
    LocalDateTime startB = bStart == null ? LocalDateTime.MIN : bStart;
    LocalDateTime endB = bEnd == null ? LocalDateTime.MAX : bEnd;
    return !endA.isBefore(startB) && !endB.isBefore(startA);
  }

  private void validarCampos(Promocion p) {
    if (p.getTipo() == null) {
      throw new IllegalArgumentException("tipo es requerido (PERCENT o FIXED)");
    }
    if (p.getValor() == null) {
      throw new IllegalArgumentException("valor es requerido");
    }
    // Rango de valor según tipo
    switch (p.getTipo()) {
      case PERCENT -> {
        var v = p.getValor();
        if (v.signum() <= 0 || v.doubleValue() > 100.0) {
          throw new IllegalArgumentException("valor para PERCENT debe ser > 0 y <= 100");
        }
      }
      case FIXED -> {
        if (p.getValor().signum() <= 0) {
          throw new IllegalArgumentException("valor para FIXED debe ser > 0");
        }
      }
    }

    // Ventana temporal consistente
    if (p.getInicio() != null && p.getFin() != null && p.getFin().isBefore(p.getInicio())) {
      throw new IllegalArgumentException("fin no puede ser anterior a inicio");
    }

    // Alcance consistente
    if (p.getScopeType() == null) {
      throw new IllegalArgumentException("scopeType es requerido (ALL, ITEM, ...)");
    }
    if (p.getScopeType() == PromotionScopeType.ITEM && p.getScopeId() == null) {
      throw new IllegalArgumentException("scopeId es requerido cuando scopeType=ITEM");
    }
  }
}
