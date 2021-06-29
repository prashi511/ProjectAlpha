package com.az.modules.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.cors().and() // (1)
				//.authorizeRequests().anyRequest().authenticated()// (2)
				//.and()
		.antMatcher("/v1/getToken/").authorizeRequests().anyRequest().permitAll()
				.and()
				.oauth2ResourceServer().jwt(); // (3)*/

		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/v1/getToken/**").permitAll()
				.and()
				.authorizeRequests().antMatchers("/v1/uploadfile/**").authenticated()
				.and()
				.oauth2ResourceServer().jwt();


	}
}
