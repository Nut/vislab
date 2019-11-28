package de.hska.iwi.vslab.categoryservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value="/categories", method=RequestMethod.GET)
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @RequestMapping(value="/categories", method=RequestMethod.POST)
    public Category createNewCategory(@RequestBody NewCategoryDTO newCategory) {
        Category newCat = new Category();
        int[] products = {};
        newCat.setName(newCategory.getCategoryName());
        newCat.setProducts(products);
        categoryRepository.save(newCat);
        return newCat;
    }

    @RequestMapping(value="/categories/{id}", method=RequestMethod.GET)
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!"));
    }

    @RequestMapping(value="/categories/{id}", method=RequestMethod.DELETE)
    public String deleteCategoryById(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "deleted category with id " + id;
    }
}