package de.hska.iwi.vslab.webshopapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import de.hska.iwi.vslab.webshopapi.models.Category;
import de.hska.iwi.vslab.webshopapi.models.NewCategory;
import de.hska.iwi.vslab.webshopapi.models.NewUser;
import de.hska.iwi.vslab.webshopapi.models.Product;
import de.hska.iwi.vslab.webshopapi.models.Role;
import de.hska.iwi.vslab.webshopapi.models.User;

@RestController
public class WebshopApiController {

    private static final String CATEGORY_SERVICE_URI = "http://category-service:8080/categories";
    private static final String PRODUCT_SERVICE_URI = "http://product-service:8080/products";
    private static final String USER_SERVICE_USERS_URI = "http://user-service:8080/users";
    private static final String USER_SERVICE_ROLES_URI = "http://user-service:8080/roles";
    private static final String INVENTORY_CATEGORIES_URI = "http://inventory-service:8080/categories";
    private static final String INVENTORY_PRODUCTS_URI = "http://inventory-service:8080/products";
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * CATEGORIES API
     */

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Category[]> getCategories() {
        Category[] categories = restTemplate.getForObject(INVENTORY_CATEGORIES_URI, Category[].class);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<Category> createNewCategory(@RequestBody NewCategory newCategory) {
        Category category = restTemplate.postForObject(CATEGORY_SERVICE_URI, newCategory, Category.class);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = restTemplate.getForObject(INVENTORY_CATEGORIES_URI + "/{id}", Category.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteCategoryById(@PathVariable Long id) {
        restTemplate.delete(INVENTORY_CATEGORIES_URI + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * PRODUCTS API
     */

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Product[]> getProducts() {
        // TODO: implement search
        Product[] products = restTemplate.getForObject(INVENTORY_PRODUCTS_URI, Product[].class);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Product> createNewProduct(@RequestBody Product newProduct) {
        Product product = restTemplate.postForObject(PRODUCT_SERVICE_URI, newProduct, Product.class);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject(INVENTORY_PRODUCTS_URI + "/{id}", Product.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    /**
     * USERS API
     */

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = restTemplate.getForObject(USER_SERVICE_USERS_URI + "/{username}", User.class, username);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(@RequestBody NewUser newUser) {
        try {
            User user = restTemplate.postForObject(USER_SERVICE_USERS_URI, newUser, User.class);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * ROLES API
     */

    @RequestMapping(value = "/roles/{level}", method = RequestMethod.GET)
    public ResponseEntity<Role> getRoleByLevel(@PathVariable int level) {
        try {
            Role role = restTemplate.getForObject(USER_SERVICE_ROLES_URI + "/{level}", Role.class, level);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }
}