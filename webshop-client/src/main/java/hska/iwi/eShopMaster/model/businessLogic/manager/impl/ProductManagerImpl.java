package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.ProductDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.models.NewProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static hska.iwi.eShopMaster.model.ApiConfig.API_PRODUCTS;;

public class ProductManagerImpl implements ProductManager {
	private ProductDAO helper;
	private RestTemplate restTemplate = new RestTemplate(
			new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

	public ProductManagerImpl() {
		helper = new ProductDAO();
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
	}

	public List<Product> getProducts() {
		// return helper.getObjectList();
		Product[] products = restTemplate.getForObject(API_PRODUCTS, Product[].class);
		List<Product> targetList = new ArrayList<Product>(Arrays.asList(products));
		return targetList;
	}

	public List<Product> getProductsForSearchValues(String searchDescription, Double searchMinPrice,
			Double searchMaxPrice) {
		return new ProductDAO().getProductListByCriteria(searchDescription, searchMinPrice, searchMaxPrice);
	}

	public Product getProductById(int id) {
		// return helper.getObjectById(id);
		return restTemplate.getForObject(API_PRODUCTS + "/{id}", Product.class, id);
	}

	public Product getProductByName(String name) {
		// Do we need this?
		return helper.getObjectByName(name);
	}

	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;

		CategoryManager categoryManager = new CategoryManagerImpl();
		Category category = categoryManager.getCategory(categoryId);

		if (category != null) {
			NewProduct product;
			if (details == null) {
				// product = new Product(name, price, category);
				product = new NewProduct(name, price, categoryId, "");
			} else {
				// product = new Product(name, price, category, details);
				product = new NewProduct(name, price, categoryId, details);
			}

			// helper.saveObject(product);
			// Product newCategory = new NewCategory(name);
			Product returnedProduct = restTemplate.postForObject(API_PRODUCTS, product, Product.class);
			productId = returnedProduct.getId();
		}

		return productId;
	}

	public void deleteProductById(int id) {
		helper.deleteById(id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
