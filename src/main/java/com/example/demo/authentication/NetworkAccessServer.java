package com.example.demo.authentication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusEndpoint;
import org.tinyradius.util.RadiusException;

/*
 * 
 * methods for radius authentication
 * 
 * 
 */
public class NetworkAccessServer {
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkAccessServer.class);
	
	private byte[] state;
	

	public byte[] getState() {
		return state;
	}

	public void setState(byte[] state) {
		this.state = state;
	}

	private static final String NAS_IP_ADDRESS = "NAS-IP-Address";
	private static final String NAS_PORT_ID = "NAS-Port-Id";
	private RadiusClient radiusClient;

    public NetworkAccessServer(RadiusServer radiusServer) {
    	
    	this.radiusClient = initRadiusClient(radiusServer);
    	
    }

    private RadiusClient initRadiusClient(RadiusServer radiusServer)  {
    	
		try {
			
			logger.info("-----we are initializing a Radius client with ip :" + radiusServer.getIp());
			logger.info("-----we are initializing a Radius client with port :" + radiusServer.getPort());
			
			RadiusEndpoint endpoint = new RadiusEndpoint(new InetSocketAddress(radiusServer.getIp(),radiusServer.getPort()),radiusServer.getSecret());
	    	RadiusClient radiusClient = new RadiusClient(endpoint);
			radiusClient.setAuthPort(radiusServer.getPort());
			System.out.println("--------"+radiusClient.getAuthPort()+"-------");
			radiusClient.setSocketTimeout(radiusServer.getTimeout());
			return radiusClient;
		} catch (SocketException e) {

			logger.error("---an error occured while initializing radius client",e);
			throw new IllegalStateException(e);
		}
    }

	public RadiusPacket authenticate(String username, String password) throws IOException, RadiusException {
		logger.info("we are tring to send the request : for user :"+username);
    	AccessRequest ar = new AccessRequest(username, password);

        //attribute that we send to server
        ar.addAttribute(NAS_PORT_ID,InetAddress.getLocalHost().getHostName());
    	ar.addAttribute(NAS_IP_ADDRESS,InetAddress.getLocalHost().getHostAddress());

        RadiusPacket response = radiusClient.authenticate(ar);
        return response;
	}


	public RadiusPacket authenticate(String username, String otp, byte[] status) throws IOException, RadiusException {
		System.out.println("this is my second request :");
		AccessRequest ar = new AccessRequest(username,otp);
		ar.addAttribute(new RadiusAttribute(24,status));
		return radiusClient.authenticate(ar);
	}

}
