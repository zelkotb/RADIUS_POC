package com.example.demo.authentication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * this class is for parsing the server configuration
 * */
public class RadiusUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RadiusUtil.class);


	private static final String SERVER_DELIMITER = ";";
	private static final String PARAM_DELIMITER = ",";
	
	public RadiusUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//read the application.properties for server configuration
	public static List<RadiusServer> getServers(String serverConfiguration){
		if (serverConfiguration == null) {
			throw new IllegalArgumentException("Radius configuration cannot be empty.");
		}
		return Stream.of(serverConfiguration.split(SERVER_DELIMITER))
				.map(s -> s.split(PARAM_DELIMITER))
				.map(p -> new RadiusServer(p[0],p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3])))
				.peek(p -> {
					logger.info("server ip : " + p.getIp());
					logger.info("server port : " + p.getPort());	
					logger.info("server secret : " + p.getSecret());
					logger.info("server timeOut : " + p.getTimeout());
						
				})
				.collect(Collectors.toList());
	}
	
	
}
