package org.lessons.java.pizzeria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@SuppressWarnings("removal")
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
				.requestMatchers("/pizzas/edit/*", "/pizzas/create").hasAuthority("ADMIN")
			.requestMatchers(HttpMethod.POST, "/pizzas/**").hasAuthority("ADMIN")
				.requestMatchers("/specialOffers").hasAuthority("ADMIN")
				.requestMatchers("/ingredients").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/specialOffers/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/ingredients/**").hasAuthority("ADMIN")
				.requestMatchers("/pizzas", "pizzas/*").hasAnyAuthority("ADMIN", "USER")
				.requestMatchers("/webjars/**").permitAll()
				.requestMatchers("/**").permitAll().and().formLogin().and().logout().and().exceptionHandling();
		return http.build();
	}

//	@Bean
//	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.authorizeHttpRequests(requests -> requests
//        		.requestMatchers("/menu/**").hasAnyAuthority("ADMIN", "USER")
//                .requestMatchers("/menu/create", "/menu/edit/**").hasAuthority("ADMIN")
//                .requestMatchers("/ingredients", "/specialOffers").hasAuthority("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/menu/**").hasAuthority("ADMIN")
//                .requestMatchers("/user").hasAnyAuthority("ADMIN", "USER")
//                .requestMatchers("/admin").hasAuthority("ADMIN")
//                .requestMatchers("/**").permitAll()).formLogin(withDefaults()).logout(withDefaults()).exceptionHandling(withDefaults());
//		
//		
//		return http.build();
//	}

	@Bean
	DatabaseUserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;

	}

}
