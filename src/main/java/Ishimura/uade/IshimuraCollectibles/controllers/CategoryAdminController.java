package Ishimura.uade.IshimuraCollectibles.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.Category;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;
import Ishimura.uade.IshimuraCollectibles.service.CategoryService;

@RestController
@RequestMapping("categoriesAdmin") // localhost:4002/categoriesAdmin

public class CategoryAdminController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId())).body(result);
    }
}
