package de.hska.iwi.vslab.categoryservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public Category createNewCategory(@RequestBody NewCategoryDTO newCategory) {
        Category newCat = new Category();
        Long[] products = {};
        newCat.setName(newCategory.getCategoryName());
        newCat.setProducts(products);
        categoryRepository.save(newCat);
        return newCat;
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!"));
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Null> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Category> update2CategoryById(@RequestBody Long[] productIds, @PathVariable Long id) {
        return categoryRepository.findById(id).map(category -> {
            category.setProducts(productIds);
            return ResponseEntity.status(HttpStatus.OK).body(categoryRepository.save(category));
        }).orElseGet(() -> {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }
}