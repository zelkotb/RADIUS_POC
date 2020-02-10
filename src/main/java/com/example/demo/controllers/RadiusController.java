package com.example.demo.controllers;

import java.security.Principal;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class RadiusController {
	

	@GetMapping("/name")
	public String showName(Principal principal) {
		return principal.getName();
	}
	
//	@PostMapping("/login")
//	public String login(HttpServletRequest request) {
//		System.out.println("----------hello----------");
//		Principal userPrincipal = request.getUserPrincipal();
//		System.out.println("----------hello----------");
//		Authentication auth = (Authentication)userPrincipal;
//		System.out.println("----------hello----------");
//		r.authenticate(auth);
//		System.out.println("----------hello----------");
//		return "Done";
//	}


	
}
