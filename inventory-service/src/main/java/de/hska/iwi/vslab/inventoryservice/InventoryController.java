package de.hska.iwi.vslab.inventoryservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import de.hska.iwi.vslab.inventoryservice.models.Category;
import de.hska.iwi.vslab.inventoryservice.models.Product;

@RestController
public class InventoryController {

    private static final String CATEGORY_SERVICE_URI = "http://category-service:8080/categories";
    private static final String PRODUCT_SERVICE_URI = "http://product-service:8080/products";
    private static RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Category[]> getCategories() {
        Category[] categories = restTemplate.getForObject(CATEGORY_SERVICE_URI, Category[].class);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = restTemplate.getForObject(CATEGORY_SERVICE_URI + "/{id}", Category.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteCategoryById(@PathVariable Long id) {
        // TODO implement delete functionality
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Product[]> getProducts() {
        // TODO: implement search
        Product[] products = restTemplate.getForObject(PRODUCT_SERVICE_URI, Product[].class);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject(PRODUCT_SERVICE_URI + "/{id}", Product.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}