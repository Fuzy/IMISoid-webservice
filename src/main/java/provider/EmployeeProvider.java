package provider;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import manager.EmployeeManager;
import model.Employee;

/**
 * Provider for employees of company.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
@Path("/employees")
public class EmployeeProvider {
  private static Logger log = Logger.getLogger("imisoid");

  /**
   * Return employee.
   * 
   * @param icp
   *          identification of user.
   * @return HTTP response.
   */
  @GET
  @Path("{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployee(@PathParam("icp") String icp) throws Exception {
    log.info("icp: " + icp);
    Employee employee = EmployeeManager.getEmployee(icp);
    if (employee == null)
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(employee).build();
  }

  /**
   * Return all employees. Contains information if the employee is subordinate
   * of user.
   * 
   * @param icp
   *          identification of user.
   * @return HTTP response.
   */
  @GET
  @Path("all/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployeesForUser(@PathParam("icp") String icp) throws Exception {
    log.info("");
    List<Employee> employees = EmployeeManager.getEmployeesForUser(icp);
    if (employees == null || employees.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(employees).build();
  }

  /**
   * Get last event of all users.
   * 
   * @return HTTP response.
   */
  @GET
  @Path("lastevents")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEvents() throws Exception {
    log.info("");
    List<Employee> employees = EmployeeManager.getLastEvents();
    if (employees == null || employees.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(employees).build();
  }

  /**
   * Return last event for employee.
   * 
   * @param icp
   *          identification of user.
   * @return HTTP response.
   */
  @GET
  @Path("lastevents/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEventForEmployee(@PathParam("icp") String icp) throws Exception {
    log.info("");
    Employee employee = EmployeeManager.getLastEventForEmployee(icp);
    if (employee == null)
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(employee).build();
  }

}
