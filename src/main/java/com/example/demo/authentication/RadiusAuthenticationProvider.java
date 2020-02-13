package com.example.demo.authentication;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.tinyradius.packet.RadiusPacket;

import com.example.demo.AccountRepo;


public class RadiusAuthenticationProvider implements AuthenticationProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(RadiusAuthenticationProvider.class);

	@Autowired
	private AccountRepo accountRepo;
	
	private List<NetworkAccessServer> clients = new ArrayList<>();
	

	public void initRadiusClients(String serverConfiguration) {
		
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
		}
		if (response == null) {
			logger.info("calling the server doesn't return any response to user : ", username);
			throw new RuntimeException("no response from server");
		}
		//for 2FA authentication
		if (response.getPacketType() == RadiusPacket.ACCESS_CHALLENGE) {
			logger.info("calling the server for access Challenge : ", username, response);

			Account a = new Account();
			a.setUsername(username);
			a.setStatus(response.getAttribute(24).getAttributeData());
			accountRepo.save(a);
			return null;
		}
		if (response.getPacketType() == RadiusPacket.ACCESS_ACCEPT) {
			logger.info("You have ben authenticated succefully M. user :"+ username);
			return new UsernamePasswordAuthenticationToken(username, "", new ArrayList<>());
		} else {
			logger.info("The server return the response for the user :", username, response);
			throw new RuntimeException("wrong credentials");
		}
		
	}
	
	private RadiusPacket authenticateInternally(NetworkAccessServer client, String username, String password) {
		
		Account a1 = accountRepo.findByUsername(username);
		if(a1==null) {
			logger.info("Calling radius server to authenticate user :"+ username);
			
			try {
				
				return client.authenticate(username, password);
				
			} catch (Exception e) {
				
				logger.error("an error occured while calling the server", e);
				return null;
			}
		}else {
			logger.info("Calling radius server to authenticate user :2", username);
			
			try {
				accountRepo.deleteById(a1.getId());
				return client.authenticate(username, password, a1.getStatus());
				
			} catch (Exception e) {
				
				logger.error("an error occured while calling the server", e);
				return null;
			}
		}
		
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	

}
