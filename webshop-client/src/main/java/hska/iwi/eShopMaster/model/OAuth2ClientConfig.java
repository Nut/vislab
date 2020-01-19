package hska.iwi.eShopMaster.model;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;

import static hska.iwi.eShopMaster.model.ApiConfig.ACCESS_TOKEN_URI;

@Component
public class OAuth2ClientConfig {
    public OAuth2ProtectedResourceDetails loginRegister() {
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
}