package com.myanimelist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.myanimelist.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserService userService;
	
	@Autowired
    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }
	
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
    @Autowired
    public void setCustomAuthenticationSuccessHandler(@Lazy CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
    	this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
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
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/home**") .hasRole("USER")
				.antMatchers("/admin").hasRole("ADMIN")
			.and()
			
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/authenticate")
				.successHandler(customAuthenticationSuccessHandler)
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
