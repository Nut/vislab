package de.hska.iwi.vslab.inventoryservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import de.hska.iwi.vslab.inventoryservice.models.Category;
import de.hska.iwi.vslab.inventoryservice.models.NewProduct;
import de.hska.iwi.vslab.inventoryservice.models.Product;
import de.hska.iwi.vslab.inventoryservice.models.ProductBody;

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
        restTemplate.delete(CATEGORY_SERVICE_URI + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<NewProduct[]> getProducts() {
        // TODO: implement search
        Product[] products = restTemplate.getForObject(PRODUCT_SERVICE_URI, Product[].class);
        NewProduct[] newProducts = new NewProduct[products.length];

        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            Category category = restTemplate.getForObject(CATEGORY_SERVICE_URI + "/{id}", Category.class,
                    product.getCategory());
            NewProduct newProduct = new NewProduct(product.getId(), product.getName(), product.getPrice(), category,
                    product.getDetails());
            newProducts[i] = newProduct;
        }
        return ResponseEntity.status(HttpStatus.OK).body(newProducts);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<NewProduct> createNewProduct(@RequestBody ProductBody inputProduct) {
        Product product = restTemplate.postForObject(PRODUCT_SERVICE_URI, inputProduct, Product.class);
        Category category = restTemplate.getForObject(CATEGORY_SERVICE_URI + "/{id}", Category.class,
                product.getCategory());
        NewProduct newProduct = new NewProduct(product.getId(), product.getName(), product.getPrice(), category,
                product.getDetails());
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<NewProduct> getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject(PRODUCT_SERVICE_URI + "/{id}", Product.class, id);
        Category category = restTemplate.getForObject(CATEGORY_SERVICE_URI + "/{id}", Category.class,
                product.getCategory());
        NewProduct newProduct = new NewProduct(product.getId(), product.getName(), product.getPrice(), category,
                product.getDetails());
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }
}