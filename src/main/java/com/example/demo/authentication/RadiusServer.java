package com.example.demo.authentication;

public class RadiusServer {

	private String ip;
	private String secret;
	private int port;
	private int timeout;
	
	public RadiusServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RadiusServer(String ip, String secret, int port, int timeout) {
		super();
		this.ip = ip;
		this.secret = secret;
		this.port = port;
		this.timeout = timeout;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	

}
