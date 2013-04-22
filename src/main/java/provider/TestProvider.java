package provider;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Employee;
import exceptions.MyException;

@Path("/test")
public class TestProvider {
  private static Logger log = Logger.getLogger("imisoid_test");
  
  @GET
  @Path("employees/{icp}")//TODO icp vs username
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("icp") String icp) throws MyException {
    log.info("");
    List<Employee> employees = null;
      //employees =  EmployeeDao.getRecords(icp, conn);
   
   
    return Response.ok(employees).build();
  }

}
