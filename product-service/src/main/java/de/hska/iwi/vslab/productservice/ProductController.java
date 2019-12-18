package de.hska.iwi.vslab.productservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public Iterable<Product> getAllProducts(@RequestParam(defaultValue = "", required = false) String description,
            @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        minPrice = minPrice == null ? Double.MIN_VALUE : minPrice;
        maxPrice = maxPrice == null ? Double.MAX_VALUE : maxPrice;

        if (description.isEmpty()) {
            return this.productRepository.findByPriceBetween(mapToDefaultSeachCriteria(minPrice, Double.MIN_VALUE),
                    mapToDefaultSeachCriteria(maxPrice, Double.MAX_VALUE));
        }

        return this.productRepository.findByDetailsContainingAndPriceBetween(description,
                mapToDefaultSeachCriteria(minPrice, Double.MIN_VALUE),
                mapToDefaultSeachCriteria(maxPrice, Double.MAX_VALUE));
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Product> createNewProduct(@RequestBody Product product) {
        try {
            Product newProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!"));
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProductById(@PathVariable Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private double mapToDefaultSeachCriteria(final double searchCriteria, final double defaultValue) {
        return searchCriteria < 0.00001 ? defaultValue : searchCriteria;
    }
}