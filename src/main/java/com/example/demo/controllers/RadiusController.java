package com.example.demo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.authentication.Account;
import com.example.demo.authentication.RadiusAuthenticationProvider;


/*
 * 
 * @Zakaria El KOTB
 */

@RestController
public class RadiusController {
	
	@Autowired
	private RadiusAuthenticationProvider radiusProvider; 
	@Value("${com.bcp.radius.server}")
	private String serverConfiguration;
	

	
	@GetMapping("/name")
	public String showName(Principal principal) {
		return principal.getName();
	}
	
	@PostMapping("/otp")
	public String sendOTPreq(@RequestBody Account a) {
		radiusProvider.initRadiusClients(serverConfiguration);
		Authentication auth;
		try {
			System.out.println(a.getUsername()+"::"+a.getPassword());
			 auth = radiusProvider.authenticate(
					new UsernamePasswordAuthenticationToken(a.getUsername(), a.getPassword()));
		} catch (Exception e) {
			return e.getMessage();
		}
		
		if(auth!=null) {
			SecurityContextHolder.getContext().setAuthentication(auth);
			return "You have ben authenticated succefully M. user :" + SecurityContextHolder
					.getContext().getAuthentication().getName();
		}else {
			return "One-Time password has been sent to mobile phone. Enter it to login";
		}
		
	}


	
}
