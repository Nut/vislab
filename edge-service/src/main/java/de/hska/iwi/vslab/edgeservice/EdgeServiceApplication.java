package de.hska.iwi.vslab.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableZuulProxy
public class EdgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}

	@Configuration
	@EnableResourceServer
	public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
            http
				.authorizeRequests()
				/**/.antMatchers("/auth/**").permitAll()
				/**/.antMatchers("/user-api/**").permitAll()
				/**/.anyRequest().authenticated()
				.and()
				/**/.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
			// @formatter:on
		}

	}

}
