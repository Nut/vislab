package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.models.NewUser;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import static hska.iwi.eShopMaster.model.ApiConfig.API_USERS;
import static hska.iwi.eShopMaster.model.ApiConfig.API_ROLES;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {

	private RestTemplate restTemplate = new RestTemplate(
			new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

	public UserManagerImpl() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
	}

	public void registerUser(String username, String name, String lastname, String password, Role role) {
		NewUser newUser = new NewUser(username, name, lastname, password, role.getLevel());

		try {
			restTemplate.postForLocation(API_USERS, newUser);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}

		try {
			return restTemplate.getForObject(API_USERS + "/{username}", User.class, username);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean deleteUserById(int id) {
		return true;
	}

	public Role getRoleByLevel(int level) {
		return restTemplate.getForObject(API_ROLES + "/{level}", Role.class, level);
	}

	public boolean doesUserAlreadyExist(String username) {

		User dbUser = this.getUserByUsername(username);

		if (dbUser != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validate(User user) {
		if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null
				|| user.getLastname() == null || user.getUsername() == null) {
			return false;
		}

		return true;
	}

}
