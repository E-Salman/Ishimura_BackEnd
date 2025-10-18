package Ishimura.uade.IshimuraCollectibles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CategoryDuplicateException extends DomainException {
    private final String description;

    public CategoryDuplicateException(String description) {
        super(ErrorCode.CATEGORY_ALREADY_EXISTS, "La categoría '" + description + "' ya existe");
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}