package de.hska.iwi.vslab.inventoryservice;

import java.net.URI;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import de.hska.iwi.vslab.inventoryservice.models.Category;
import de.hska.iwi.vslab.inventoryservice.models.NewProduct;
import de.hska.iwi.vslab.inventoryservice.models.Product;
import de.hska.iwi.vslab.inventoryservice.models.ProductBody;

@RestController
public class InventoryController {

    private static final String CATEGORY_SERVICE_URI = "http://category-service:8080/categories";
    private static final String PRODUCT_SERVICE_URI = "http://product-service:8080/products";
    private static RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

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
        Category category = this.getCategoryById(id).getBody();
        for (Long producdId : category.getProducts()) {
            restTemplate.delete(PRODUCT_SERVICE_URI + "/{id}", producdId);
        }
        restTemplate.delete(CATEGORY_SERVICE_URI + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<NewProduct[]> getProducts(@RequestParam(required = false) String description,
            @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        URI targetUrl = UriComponentsBuilder.fromUriString(PRODUCT_SERVICE_URI) // Build the base link
                .queryParam("description", description) // Add query params
                .queryParam("minPrice", minPrice) // Add query params
                .queryParam("maxPrice", maxPrice) // Add query params
                .build() // Build the URL
                .encode() // Encode any URI items that need to be encoded
                .toUri();

        Product[] products = restTemplate.getForObject(targetUrl, Product[].class);
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

        // update category
        Long[] newProductArray = addProductFromProductArrayForCategory(category, newProduct.getId());
        restTemplate.patchForObject(CATEGORY_SERVICE_URI + "/{id}", newProductArray, Category.class, category.getId());

        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    private Long[] addProductFromProductArrayForCategory(Category category, Long productId) {
        Long[] oldProductsArray = category.getProducts();
        return ArrayUtils.add(oldProductsArray, productId);
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

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteProductById(@PathVariable Long id) {
        try {
            // update category
            NewProduct product = this.getProductById(id).getBody();
            Category category = restTemplate.getForObject(CATEGORY_SERVICE_URI + "/{id}", Category.class,
                    product.getCategory().getId());
            Long[] newProductArray = removeProductFromProductArrayForCategory(category, id);
            restTemplate.patchForObject(CATEGORY_SERVICE_URI + "/{id}", newProductArray, Category.class,
                    category.getId());

            restTemplate.delete(PRODUCT_SERVICE_URI + "/{id}", id);

            return ResponseEntity.status(HttpStatus.OK).body(id);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    private Long[] removeProductFromProductArrayForCategory(Category category, Long productId) {
        Long[] oldProductsArray = category.getProducts();
        return ArrayUtils.removeElement(oldProductsArray, productId);
    }
}