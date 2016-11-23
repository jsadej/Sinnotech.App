package com.sinnotech.first_rest;



import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;




/*import org.glassfish.grizzly.http.server.Response;*/

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })

	public Response post() {
		String login = "TestApp1";
		String password = "letmein";

		AppCommunicator app = new AppCommunicator(login, password);
		try {
			app.getAuthorization();
		} catch (Exception e) {

		}
		JSONObject objLog = app.getListlog();

		return Response.status(200).entity((objLog)).build();

	}

}
	




