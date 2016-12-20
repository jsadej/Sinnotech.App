package com.sinnotech.first_rest;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

import com.google.gson.Gson;


public class AppCommunicator {
	private String login;
	private String password;
	private String loginBase64;
	private String passwordBase64;
	private String token;
	private Map<String, Object> logs;

	private String loginUri;
	private static String endpointuri = "http://apps.sinnotech.pl/app-monitor/rest/services/endpoint/";

	public AppCommunicator(String login, String password, String loginUri) {
		super();
		this.loginUri = loginUri;
		this.login = login;
		this.password = password;
		encodebase64();

	}

	private void encodebase64() {
		try {
			loginBase64 = Base64.getEncoder().encodeToString(login.getBytes("utf-8"));
			passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes("utf-8"));

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	public String getAuthorization() {
		JSONObject jsonResponse;
		try {

			Client client = ClientBuilder.newClient();

			WebTarget LoginWebTarget = client.target(loginUri);
			WebTarget LoginWebTargetWithQueryParam = LoginWebTarget.queryParam("l", loginBase64)
					.queryParam("p", passwordBase64).queryParam("ln", "");

			Invocation.Builder invocationBuilder = LoginWebTargetWithQueryParam.request(MediaType.TEXT_PLAIN);

			Response response = invocationBuilder.accept(MediaType.APPLICATION_JSON).get();

			jsonResponse = response.readEntity(JSONObject.class);
			token = (String) jsonResponse.get("token");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return token;

	}

	
	
	public Map<String, Object> send(String endpointName, String endpointParam, Map<String, String> params) {

		getAuthorization();

		JSONObject JsonResponse = null;
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(endpointuri);
		Invocation.Builder invocationBuilder = target.request(MediaType.TEXT_PLAIN_TYPE);
		invocationBuilder.accept(MediaType.APPLICATION_JSON);
		invocationBuilder.header("X-TQ-Token", token).header("X-TQ-ServiceVersion", "1")
				.header("X-TQ-ServiceParam", endpointParam).header("X-TQ-Name", endpointName);

		Response res = invocationBuilder.buildPost(Entity.json("")).invoke();

		JsonResponse = res.readEntity(JSONObject.class);
		String Response = JsonResponse.toJSONString();

		Gson gson = new Gson();
		logs = new HashMap<String, Object>();
		logs = (Map<String, Object>) gson.fromJson(Response, logs.getClass());

		return logs;

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
