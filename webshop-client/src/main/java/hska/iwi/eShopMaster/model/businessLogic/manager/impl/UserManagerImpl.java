package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.RoleDAO;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.UserDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.models.NewUser;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {

	private final RestTemplate restTemplate = new RestTemplate();

	UserDAO helper;

	public UserManagerImpl() {
		helper = new UserDAO();
	}

	public void registerUser(String username, String name, String lastname, String password, Role role) {
		// User user = new User(username, name, lastname, password, role);
		// helper.saveObject(user);

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);

		NewUser newUser = new NewUser(username, name, lastname, password, role.getLevel());
		String WEBSHOP_URI = "http://webshop-api:8080";

		try {
			restTemplate.postForLocation(WEBSHOP_URI + "/users", newUser);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}
		return helper.getUserByUsername(username);
	}

	public boolean deleteUserById(int id) {
		User user = new User();
		user.setId(id);
		helper.deleteObject(user);
		return true;
	}

	public Role getRoleByLevel(int level) {
		RoleDAO roleHelper = new RoleDAO();
		return roleHelper.getRoleByLevel(level);
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
