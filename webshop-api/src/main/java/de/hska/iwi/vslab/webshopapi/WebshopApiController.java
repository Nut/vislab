package de.hska.iwi.vslab.webshopapi;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private Map<Long, Category> categoryCache = new LinkedHashMap<>();
    private Map<Long, Product> productCache = new LinkedHashMap<>();
    private Map<String, User> userCache = new LinkedHashMap<>();
    private Map<Integer, Role> roleCache = new LinkedHashMap<>();

    @Autowired
    private EurekaClient discoveryClient;

    /**
     * CATEGORIES API
     */

    @HystrixCommand(fallbackMethod = "getCategoriesFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<Category[]> getCategories() {
        Category[] categories = restTemplate.getForObject(getInventoryURL("categories"), Category[].class);
        for (Category category : categories) {
            this.categoryCache.put(category.getId(), category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> createNewCategory(@RequestBody NewCategory newCategory) {
        Category category = restTemplate.postForObject(getInventoryURL("categories"), newCategory, Category.class);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @HystrixCommand(fallbackMethod = "getCategoryFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = restTemplate.getForObject(getInventoryURL("categories") + "/{id}", Category.class, id);
        this.categoryCache.put(category.getId(), category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> deleteCategoryById(@PathVariable Long id) {
        restTemplate.delete(getInventoryURL("categories") + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * PRODUCTS API
     */

    @HystrixCommand(fallbackMethod = "getProductsFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
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

        for (Product product : products) {
            this.productCache.put(product.getId(), product);
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> createNewProduct(@RequestBody NewProduct newProduct) {
        Product product = restTemplate.postForObject(getInventoryURL("products"), newProduct, Product.class);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @HystrixCommand(fallbackMethod = "getProductFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject(getInventoryURL("products") + "/{id}", Product.class, id);
        this.productCache.put(product.getId(), product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> deleteProductById(@PathVariable Long id) {
        restTemplate.delete(getInventoryURL("products") + "/{id}", id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * USERS API
     */

    @HystrixCommand(fallbackMethod = "getUserFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = restTemplate.getForObject(getUserURL("users") + "/{username}", User.class, username);
            this.userCache.put(user.getUsername(), user);
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

    @HystrixCommand(fallbackMethod = "getRoleFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    @RequestMapping(value = "/roles/{level}", method = RequestMethod.GET)
    public ResponseEntity<Role> getRoleByLevel(@PathVariable int level) {
        try {
            Role role = restTemplate.getForObject(getUserURL("roles") + "/{level}", Role.class, level);
            this.roleCache.put(role.getLevel(), role);
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

    private ResponseEntity<Category[]> getCategoriesFromCache() {
        return ResponseEntity.ok(categoryCache.entrySet().stream().map(e -> e.getValue()).toArray(Category[]::new));
    }

    private ResponseEntity<Category> getCategoryFromCache(Long id) {
        return ResponseEntity.ok(categoryCache.get(id));
    }

    private ResponseEntity<Product> getProductFromCache(Long id) {
        return ResponseEntity.ok(productCache.get(id));
    }

    private ResponseEntity<Product[]> getProductsFromCache(String description, Double minPrice, Double maxPrice) {
        return ResponseEntity.ok(productCache.entrySet()
            .stream()
            .map(e -> e.getValue())
            .filter(p -> p.getDetails().contains(description))
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .toArray(Product[]::new));
    }

    private ResponseEntity<User> getUserFromCache(String username) {
        return ResponseEntity.ok(userCache.get(username));
    }

    private ResponseEntity<Role> getRoleFromCache(int level) {
        return ResponseEntity.ok(roleCache.get(level));
    }
}