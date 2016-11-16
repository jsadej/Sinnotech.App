package com.sinnotech.first_rest;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.json.simple.JSONObject;


public class AppCommunicator {
	private String login;
	private String password;
	private String loginBase64;
	private String passwordBase64;
	private String token;
	private static String baseuri = "http://apps.transqual.pl/app-monitor/rest/login?";

	

	public AppCommunicator(String login, String password) {
		super();
		this.login = login;
		this.password = password;
		encodebase64();
	}
	private void encodebase64(){
		try {
			loginBase64 = Base64.getEncoder().encodeToString(login.getBytes("utf-8"));
			passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes("utf-8"));
		   
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	

	}
		
	public JSONObject getAuthorization() {
		JSONObject jsonResponse = null;

		try {

			Client client = ClientBuilder.newClient();

			WebTarget LoginWebTarget = client.target(baseuri);
			WebTarget LoginWebTargetWithQueryParam = LoginWebTarget.queryParam("l", loginBase64)
					.queryParam("p", passwordBase64).queryParam("ln", "");

			Invocation.Builder invocationBuilder = LoginWebTargetWithQueryParam.request(MediaType.TEXT_PLAIN);

			Response response = invocationBuilder.accept(MediaType.APPLICATION_JSON).get();

			jsonResponse = response.readEntity(JSONObject.class);

			System.out.println(response.getStatus());
			System.out.println(jsonResponse);

		} catch (Exception e) {

			e.printStackTrace();

		}
		return jsonResponse;

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
