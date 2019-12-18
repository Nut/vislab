package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.models.NewProduct;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static hska.iwi.eShopMaster.model.ApiConfig.API_PRODUCTS;;

public class ProductManagerImpl implements ProductManager {
	private RestTemplate restTemplate = new RestTemplate(
			new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

	public ProductManagerImpl() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
	}

	public List<Product> getProducts() {
		Product[] products = restTemplate.getForObject(API_PRODUCTS, Product[].class);
		List<Product> targetList = new ArrayList<Product>(Arrays.asList(products));
		return targetList;
	}

	public List<Product> getProductsForSearchValues(String searchDescription, Double searchMinPrice,
			Double searchMaxPrice) {
		URI targetUrl = UriComponentsBuilder.fromUriString(API_PRODUCTS) // Build the base link
				.queryParam("description", searchDescription) // Add query params
				.queryParam("minPrice", searchMinPrice) // Add query params
				.queryParam("maxPrice", searchMaxPrice) // Add query params
				.build() // Build the URL
				.encode() // Encode any URI items that need to be encoded
				.toUri();
		Product[] products = restTemplate.getForObject(targetUrl, Product[].class);
		List<Product> targetList = new ArrayList<Product>(Arrays.asList(products));
		return targetList;
	}

	public Product getProductById(int id) {
		return restTemplate.getForObject(API_PRODUCTS + "/{id}", Product.class, id);
	}

	public Product getProductByName(String name) {
		return null;
	}

	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;

		CategoryManager categoryManager = new CategoryManagerImpl();
		Category category = categoryManager.getCategory(categoryId);

		if (category != null) {
			NewProduct product;
			if (details == null) {
				product = new NewProduct(name, price, categoryId, "");
			} else {
				product = new NewProduct(name, price, categoryId, details);
			}

			Product returnedProduct = restTemplate.postForObject(API_PRODUCTS, product, Product.class);
			productId = returnedProduct.getId();
		}

		return productId;
	}

	public void deleteProductById(int id) {
		restTemplate.delete(API_PRODUCTS + "/{id}", id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
