package com.example.demo.configurations;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.authentication.Account;
import com.example.demo.authentication.NetworkAccessServer;
import com.example.demo.authentication.RadiusAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthFilter extends UsernamePasswordAuthenticationFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkAccessServer.class);
	private RadiusAuthenticationProvider radiusProvider; 

	
	
	public AuthFilter(RadiusAuthenticationProvider radiusProvider) {
		super();
		this.radiusProvider = radiusProvider;
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		Account a = null;
		try {
			a = new ObjectMapper().readValue(request.getInputStream(), Account.class);
		} catch (IOException e) {
			logger.error("cannot read this request");
			throw new RuntimeException("cannot read this request");
		}
		return radiusProvider.authenticate(
				new UsernamePasswordAuthenticationToken(a.getUsername(), a.getPassword()));
	}
}
