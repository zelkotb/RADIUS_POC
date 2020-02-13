package com.example.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.demo.authentication.RadiusAuthenticationProvider;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	RadiusAuthenticationProvider radiusProvider;
	@Value("${com.bcp.radius.server}")
	private String serverConfiguration;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 

		http.csrf().disable()
		// don't authenticate this particular request
		.authorizeRequests()
		.antMatchers("/otp").permitAll()
		.anyRequest().authenticated();
		//radiusProvider.initRadiusClients(serverConfiguration);
		//http.addFilter(new AuthFilter(radiusProvider));

	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(radiusAuthenticationProvider());
	}

	@Bean
	public RadiusAuthenticationProvider radiusAuthenticationProvider() {
		return new RadiusAuthenticationProvider();
	}
}
