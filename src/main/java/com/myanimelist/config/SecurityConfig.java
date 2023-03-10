package com.myanimelist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.myanimelist.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserService userService;

    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;

    @Autowired
    public SecurityConfig(UserService userService, AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl) {
        this.userService = userService;
        this.authenticationSuccessHandlerImpl = authenticationSuccessHandlerImpl;
    }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.userDetailsService(userService)
			
			.authorizeRequests()
				.antMatchers("/home").hasRole("USER")
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/anime/**").hasRole("USER")
				.antMatchers("/list/**").hasRole("USER")
				.antMatchers("/reviews/**").hasRole("USER")
				.antMatchers("/swagger*/**").permitAll()
			.and()
			
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/authenticate")
				.successHandler(authenticationSuccessHandlerImpl)
				.permitAll()
			.and()
			
			.logout()
				.permitAll()
			.and()
			
			.exceptionHandling()
				.accessDeniedPage("/access-denied");
		
		return http.build();
	}
}
