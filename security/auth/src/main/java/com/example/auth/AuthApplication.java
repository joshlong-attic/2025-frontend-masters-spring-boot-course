package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

import static org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.authorizationServer;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	SecurityFilterChain mSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.formLogin(Customizer.withDefaults())
			.authorizeHttpRequests(ae -> ae.anyRequest().authenticated())
			.webAuthn(wa -> wa.rpId("localhost").rpName("Bootiful Auth Server").allowedOrigins("http://localhost:9090"))
			.oneTimeTokenLogin(ott -> ott.tokenGenerationSuccessHandler((_, response, oneTimeToken) -> {
				var value = oneTimeToken.getTokenValue();
				System.out.println("go to http://localhost:9090/login/ott?token=" + value);
				response.getWriter().print("you've got console mail!");
				response.setContentType(MediaType.TEXT_PLAIN.toString());
			}))
			.with(authorizationServer(), as -> as.oidc(Customizer.withDefaults()))
			.build();
	}

	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	UserDetailsPasswordService userDetailsPasswordService(JdbcUserDetailsManager userDetailsManager) {
		return (user, newPassword) -> {
			var updated = User.withUserDetails(user).password(newPassword).build();
			userDetailsManager.updateUser(updated);
			return updated;
		};
	}

	@EventListener
	void authenticationSuccess(AuthenticationSuccessEvent event) {
		System.out.println("Authentication success: " + event.getAuthentication());
	}

	/*
	 * @Bean InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder
	 * passwordEncoder) { var users = Set.of(
	 * User.withUsername("josh").password(passwordEncoder.encode("pw")).roles("USER").
	 * build(),
	 * User.withUsername("rob").password(passwordEncoder.encode("pw")).roles("USER",
	 * "ADMIN").build()); return new InMemoryUserDetailsManager(users); }
	 */

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
/*
 * @Controller
 *
 * @ResponseBody class MeController {
 *
 * @GetMapping("/") Map<String, Object> me(Principal principal) { return Map.of("name",
 * "Hello " + principal.getName() + "!"); } }
 */