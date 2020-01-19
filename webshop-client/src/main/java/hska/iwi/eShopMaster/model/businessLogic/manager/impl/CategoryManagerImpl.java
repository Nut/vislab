package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.BeanUtil;
import hska.iwi.eShopMaster.model.OAuth2ClientConfig;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.models.NewCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import static hska.iwi.eShopMaster.model.ApiConfig.API_CATEGORIES;

public class CategoryManagerImpl implements CategoryManager {

	private OAuth2RestTemplate oAuthRestTemplate;

	public CategoryManagerImpl() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());

		this.oAuthRestTemplate = BeanUtil.getBean(OAuth2ClientConfig.class).oAuthRestTemplate();
		oAuthRestTemplate
				.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		oAuthRestTemplate.setInterceptors(interceptors);
	}

	public List<Category> getCategories() {
		Category[] categories = oAuthRestTemplate.getForObject(API_CATEGORIES, Category[].class);
		List<Category> targetList = new ArrayList<Category>(Arrays.asList(categories));
		return targetList;
	}

	public Category getCategory(int id) {
		return oAuthRestTemplate.getForObject(API_CATEGORIES + "/{id}", Category.class, id);
	}

	public Category getCategoryByName(String name) {
		return null;
	}

	public void addCategory(String name) {

		NewCategory newCategory = new NewCategory(name);
		oAuthRestTemplate.postForLocation(API_CATEGORIES, newCategory);
	}

	public void delCategory(Category cat) {
		// Products are also deleted because of relation in Category.java
		oAuthRestTemplate.delete(API_CATEGORIES + "/{id}", cat.getId());
	}

	public void delCategoryById(int id) {
		oAuthRestTemplate.delete(API_CATEGORIES + "/{id}", id);
	}
}
