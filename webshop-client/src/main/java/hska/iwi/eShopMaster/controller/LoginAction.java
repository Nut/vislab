package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.UserManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.dataobjects.UserAuthDetails;

import java.util.Map;
import java.util.logging.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import static hska.iwi.eShopMaster.model.ApiConfig.ACCESS_TOKEN_URI;
import static hska.iwi.eShopMaster.model.ApiConfig.AUTH_ME_URI;

import static java.util.Objects.requireNonNull;

public class LoginAction extends ActionSupport {

	private static final Logger LOGGER = Logger.getLogger(LoginAction.class.getSimpleName());

	/**
	 *
	 */
	private static final long serialVersionUID = -983183915002226000L;
	private String username = null;
	private String password = null;
	private String firstname;
	private String lastname;
	private String role;

	@Override
	public String execute() throws Exception {

		// Return string:
		String result = "input";

		UserManager myCManager = new UserManagerImpl();

		OAuth2AccessToken accessToken;

		// Get user from DB:
		// User user = myCManager.getUserByUsername(getUsername());

		// Does user exist?
		try {
			accessToken = loadAccessToken();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("denied");
			System.out.println(e.getCause());

			addActionError(getText("error.password.wrong"));
			addActionError(getText("error.username.wrong"));
			return result;
		}

		UserAuthDetails userAuthDetails = loadUserAuthDetails(accessToken);
		LOGGER.info("Received UserAuthDetails: " + userAuthDetails);
		
		User user = userAuthDetails.getPrincipal();

		// Get session to save user role and login:
		Map<String, Object> session = ActionContext.getContext().getSession();

		// Save user object in session:
		session.put("webshop_user", user);
		session.put("message", "");
		session.put("access_token", accessToken);

		firstname = user.getFirstname();
		lastname = user.getLastname();
		role = user.getRole().getTyp();
		return "success";

		// if (user != null) {
		// // Is the password correct?
		// if (user.getPassword().equals(getPassword())) {
		// // Get session to save user role and login:
		// Map<String, Object> session = ActionContext.getContext().getSession();

		// // Save user object in session:
		// session.put("webshop_user", user);
		// session.put("message", "");
		// firstname = user.getFirstname();
		// lastname = user.getLastname();
		// role = user.getRole().getTyp();
		// result = "success";
		// } else {
		// addActionError(getText("error.password.wrong"));
		// }
		// } else {
		// addActionError(getText("error.username.wrong"));
		// }

		// return result;
	}

	private OAuth2AccessToken loadAccessToken() {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(ACCESS_TOKEN_URI);
		resource.setClientId("webshop-webclient");
		resource.setClientSecret("secret");
		resource.setGrantType("password");
		resource.setUsername(username);
		resource.setPassword(password);

		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource);

		return oAuth2RestTemplate.getAccessToken();
	}

	private UserAuthDetails loadUserAuthDetails(OAuth2AccessToken accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken.getValue());
		HttpEntity entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<UserAuthDetails> detailsEntity = restTemplate.exchange(AUTH_ME_URI, HttpMethod.GET, entity,
				UserAuthDetails.class);
		return requireNonNull(detailsEntity.getBody());
	}

	@Override
	public void validate() {
		if (getUsername().length() == 0) {
			addActionError(getText("error.username.required"));
		}
		if (getPassword().length() == 0) {
			addActionError(getText("error.password.required"));
		}
	}

	public String getUsername() {
		return (this.username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return (this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
