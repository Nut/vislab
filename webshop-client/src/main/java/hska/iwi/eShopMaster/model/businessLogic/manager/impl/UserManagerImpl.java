package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.BeanUtil;
import hska.iwi.eShopMaster.model.OAuth2ClientConfig;
import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.models.NewUser;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import hska.iwi.eShopMaster.model.database.LoggingRequestInterceptor;
import static hska.iwi.eShopMaster.model.ApiConfig.USER_API_USERS;
import static hska.iwi.eShopMaster.model.ApiConfig.USER_API_ROLES;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author knad0001
 */
public class UserManagerImpl implements UserManager {

	private OAuth2RestTemplate loginRegisterRestTemplate;

	public UserManagerImpl() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new LoggingRequestInterceptor());

		this.loginRegisterRestTemplate = BeanUtil.getBean(OAuth2ClientConfig.class).loginRegisterRestTemplate();
		loginRegisterRestTemplate
				.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		loginRegisterRestTemplate.setInterceptors(interceptors);
	}

	public void registerUser(String username, String name, String lastname, String password, Role role) {
		NewUser newUser = new NewUser(username, name, lastname, password, role.getLevel());

		try {
			loginRegisterRestTemplate.postForLocation(USER_API_USERS, newUser);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}

		try {
			return loginRegisterRestTemplate.getForObject(USER_API_USERS + "/{username}", User.class, username);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean deleteUserById(int id) {
		return true;
	}

	public Role getRoleByLevel(int level) {
		return loginRegisterRestTemplate.getForObject(USER_API_ROLES + "/{level}", Role.class, level);
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
