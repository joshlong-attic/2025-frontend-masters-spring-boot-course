package bootiful.passwordless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
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
public class PasswordlessApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasswordlessApplication.class, args);
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


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .with(authorizationServer(), as -> as.oidc(Customizer.withDefaults()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oneTimeTokenLogin(configurer -> configurer.tokenGenerationSuccessHandler(
                        (request, response, oneTimeToken) -> {
                            var msg = "go to http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue();
                            System.out.println(msg);
                            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                            response.getWriter().print("you've got console mail!");
                        }))
                .webAuthn(c -> c
                        .rpId("localhost")
                        .rpName("bootiful passkeys")
                        .allowedOrigins("http://localhost:8080")
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }
}

@Controller
@ResponseBody
class SecuredController {

    @GetMapping("/admin")
    Map<String, String> admin(Principal principal) {
        return Map.of("admin", principal.getName());
    }

    @GetMapping("/")
    Map<String, String> hello(Principal principal) {
        return Map.of("user", principal.getName());
    }
}