package de.hska.iwi.vslab.webshopapi;

import java.net.URI;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import de.hska.iwi.vslab.webshopapi.models.Category;
import de.hska.iwi.vslab.webshopapi.models.NewCategory;
import de.hska.iwi.vslab.webshopapi.models.NewProduct;
import de.hska.iwi.vslab.webshopapi.models.NewUser;
import de.hska.iwi.vslab.webshopapi.models.Product;
import de.hska.iwi.vslab.webshopapi.models.Role;
import de.hska.iwi.vslab.webshopapi.models.User;

@RestController
public class WebshopApiController {

    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private EurekaClient discoveryClient;

    /**
     * CATEGORIES API
     */

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Category[]> getCategories() {
        Category[] categories = restTemplate.getForObject(getInventoryURL("categories"), Category[].class);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<Category> createNewCategory(@RequestBody NewCategory newCategory) {
        Category category = restTemplate.postForObject(getInventoryURL("categories"), newCategory, Category.class);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = restTemplate.getForObject(getInventoryURL("categories") + "/{id}", Category.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteCategoryById(@PathVariable Long id) {
        restTemplate.delete(getInventoryURL("categories") + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * PRODUCTS API
     */

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Product[]> getProducts(@RequestParam(required = false) String description,
            @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice) {
        URI targetUrl = UriComponentsBuilder.fromUriString(getInventoryURL("products")) // Build the base link
                .queryParam("description", description) // Add query params
                .queryParam("minPrice", minPrice) // Add query params
                .queryParam("maxPrice", maxPrice) // Add query params
                .build() // Build the URL
                .encode() // Encode any URI items that need to be encoded
                .toUri();
        Product[] products = restTemplate.getForObject(targetUrl, Product[].class);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Product> createNewProduct(@RequestBody NewProduct newProduct) {
        Product product = restTemplate.postForObject(getInventoryURL("products"), newProduct, Product.class);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject(getInventoryURL("products") + "/{id}", Product.class, id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Long> deleteProductById(@PathVariable Long id) {
        restTemplate.delete(getInventoryURL("products") + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * USERS API
     */

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = restTemplate.getForObject(getUserURL("users") + "/{username}", User.class, username);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(@RequestBody NewUser newUser) {
        try {
            User user = restTemplate.postForObject(getUserURL("users"), newUser, User.class);
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
            Role role = restTemplate.getForObject(getUserURL("roles") + "/{level}", Role.class, level);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * HELPER FUNCTIONS
     */

    private String getUserURL(String uri) {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka("USER-SERVICE", false);
        return instance.getHomePageUrl() + uri;
    }

    private String getInventoryURL(String uri) {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka("INVENTORY-SERVICE", false);
        return instance.getHomePageUrl() + uri;
    }
}