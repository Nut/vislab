package de.hska.iwi.vslab.productservice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByDetailsContainingAndPriceBetween(String details, double minPrice, double maxPrice);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}