package com.example.demo.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.tinyradius.packet.RadiusPacket;


public class RadiusAuthenticationProvider implements AuthenticationProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(RadiusAuthenticationProvider.class);

	@Value("${com.bcp.radius.server}")
	private String serverConfiguration;
	
	private List<NetworkAccessServer> clients = new ArrayList<>();
	private int clientIndex;
	
	@PostConstruct
	public void initRadiusClients() {
		
		logger.info("initializing Radius clients");
		List<RadiusServer> servers = RadiusUtil.getServers(serverConfiguration);
		servers.forEach(c -> clients.add(new NetworkAccessServer(c)));
		
	}


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		logger.info("Authenticate user : " + authentication.getName());
		logger.info("Authenticate user : " + authentication.getCredentials().toString());
		
		RadiusPacket response = null;
		int attemptToConnect = 0;
		String username = authentication.getName();
		
		while (response == null && attemptToConnect++ < clients.size()) {
			
			response = authenticateInternally(clients.get(attemptToConnect - 1), username,
					authentication.getCredentials().toString());
			clientIndex = attemptToConnect - 1;
		}
		if (response == null) {
			logger.info("calling the server doesn't return any response to user : ", username);
			return null;
		}
		if (response.getPacketType() == RadiusPacket.ACCESS_CHALLENGE) {
			logger.info("calling the server doesn't return the response for user : ", username, response);
	        BufferedReader reader =  
	                   new BufferedReader(new InputStreamReader(System.in)); 
	        String otp =null;
	        // Reading data using readLine 
	        try {
				otp = reader.readLine();
				reader.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			authenticateInternally2(clients.get(clientIndex), username, otp, 
					response.getAttribute(24).getAttributeData());
			
			return new UsernamePasswordAuthenticationToken(username, "", new ArrayList<>());
		}
		if (response.getPacketType() == RadiusPacket.ACCESS_ACCEPT) {
			logger.info("You have benn authenticated succefully M. user :", username);
			return new UsernamePasswordAuthenticationToken(username, "", new ArrayList<>());
		} else {
			logger.info("The server return the response for the user :", username, response);
			return null;
		}
		
	}
	
	private RadiusPacket authenticateInternally(NetworkAccessServer client, String username, String password) {
		
		logger.info("Calling radius server to authenticate user :", username);
		
		try {
			
			return client.authenticate(username, password);
			
		} catch (Exception e) {
			
			logger.error("an error occured while calling the server", e);
			return null;
		}
	}
	
private RadiusPacket authenticateInternally2(NetworkAccessServer client, String username, String otp, byte[] status) {
		
		logger.info("Calling radius server to authenticate user :2", username);
		
		try {
			
			return client.authenticate(username, otp, status);
			
		} catch (Exception e) {
			
			logger.error("an error occured while calling the server", e);
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	

}
