package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.CategoryDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.models.NewCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static hska.iwi.eShopMaster.model.ApiConfig.API_CATEGORIES;

public class CategoryManagerImpl implements CategoryManager {
	private CategoryDAO helper;
	private RestTemplate restTemplate = new RestTemplate(
			new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

	public CategoryManagerImpl() {
		helper = new CategoryDAO();
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
	}

	public List<Category> getCategories() {
		Category[] categories = restTemplate.getForObject(API_CATEGORIES, Category[].class);
		List<Category> targetList = new ArrayList<Category>(Arrays.asList(categories));
		return targetList;
	}

	public Category getCategory(int id) {
		return restTemplate.getForObject(API_CATEGORIES + "/{id}", Category.class, id);
	}

	public Category getCategoryByName(String name) {
		// TODO ?
		return helper.getObjectByName(name);
	}

	public void addCategory(String name) {

		NewCategory newCategory = new NewCategory(name);
		restTemplate.postForLocation(API_CATEGORIES, newCategory);
	}

	public void delCategory(Category cat) {
		// Products are also deleted because of relation in Category.java
		restTemplate.delete(API_CATEGORIES + "/{id}", cat.getId());
	}

	public void delCategoryById(int id) {
		restTemplate.delete(API_CATEGORIES + "/{id}", id);
	}
}
