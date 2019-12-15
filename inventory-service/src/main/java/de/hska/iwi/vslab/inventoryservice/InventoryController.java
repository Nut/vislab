package de.hska.iwi.vslab.inventoryservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import de.hska.iwi.vslab.inventoryservice.models.Category;

@RestController
public class InventoryController {

    private static final String CATEGORY_SERVICE_URI = "http://category-service:8080/categories";
    private static RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Category[]> getCategories() {
        Category[] category = restTemplate.getForObject(CATEGORY_SERVICE_URI, Category[].class);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}