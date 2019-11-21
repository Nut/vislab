package de.hska.iwi.vslab.categoryservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @RequestMapping(value="/categories", method= RequestMethod.GET)
    public String getAllCategories() {
        return "Hallo World!";
    }

    @RequestMapping(value="/categories", method= RequestMethod.POST)
    public String createNewCategory(@RequestParam String category) {
        return "Hallo World!";
    }

    @RequestMapping(value="/categories/{id}", method= RequestMethod.GET)
    public String getCategoryById(@PathVariable("id") String id) {
        return "Hallo World!";
    }

    @RequestMapping(value="/categories/{id}", method= RequestMethod.DELETE)
    public String deleteCategoryById(@PathVariable("id") String id) {
        return "Hallo World!";
    }
}