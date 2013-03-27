package provider;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utilities.MyBasicNameValuePair;

@Path("/authentication")
public class AuthenticationProvider {
  
  @GET
  @Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
  public String test() {
    System.out.println("AuthenticationProvider.test()");
    return "AuthenticationProvider";
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public String getAuthenticationToken(ArrayList<MyBasicNameValuePair> params) {//ArrayList<NameValuePair> params//Map<String, String> params
    System.out.println("AuthenticationProvider.getAuthenticationToken() params: " );
    return "qwerty";
    
  }
  
  
}
