package Ishimura.uade.IshimuraCollectibles.exceptions;

public class WishlistItemAlreadyExistsException extends DomainException {
  private final Long usuarioId;
  private final Long coleccionableId;

  public WishlistItemAlreadyExistsException(Long usuarioId, Long coleccionableId) {
    super(ErrorCode.WISHLIST_ITEM_ALREADY_EXISTS,
        "El coleccionable id=" + coleccionableId + " ya está en la wishlist del usuario id=" + usuarioId);
    this.usuarioId = usuarioId;
    this.coleccionableId = coleccionableId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public Long getColeccionableId() {
    return coleccionableId;
  }
}

