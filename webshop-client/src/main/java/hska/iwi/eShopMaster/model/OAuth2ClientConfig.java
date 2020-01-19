package hska.iwi.eShopMaster.model;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import hska.iwi.eShopMaster.model.database.dataobjects.User;

import static hska.iwi.eShopMaster.model.ApiConfig.ACCESS_TOKEN_URI;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

@Component
public class OAuth2ClientConfig {
    private OAuth2ProtectedResourceDetails loginRegister() {
        System.out.println("called loginRegister()");
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId("login-register");
        details.setAccessTokenUri(ACCESS_TOKEN_URI);
        details.setClientId("user-service-client");
        details.setClientSecret("usersecret");
        details.setGrantType("client_credentials");
        return details;
    }

    public OAuth2RestTemplate loginRegisterRestTemplate() {
        System.out.println("called loginRegisterRestTemplate()");
        OAuth2RestTemplate template = new OAuth2RestTemplate(loginRegister());
        return template;
    }

    private OAuth2ProtectedResourceDetails getOAuthDetails(String username, String password) {
        System.out.println("called getOAuthDetails()");
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setId("oauth-login");
        details.setAccessTokenUri(ACCESS_TOKEN_URI);
        details.setClientId("webshop-webclient");
        details.setClientSecret("secret");
        details.setGrantType("password");
        details.setUsername(username);
        details.setPassword(password);
        return details;
    }

    public OAuth2RestTemplate oAuthRestTemplate() {
        System.out.println("called oAuthRestTemplate()");
        Map<String, Object> session = ActionContext.getContext().getSession();
        User user = (User) session.get("webshop_user");
        String username = user.getUsername();
        String password = user.getPassword();
        OAuth2RestTemplate template = new OAuth2RestTemplate(getOAuthDetails(username, password));
        return template;
    }
}