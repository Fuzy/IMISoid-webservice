package provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.connection.ConnectionManager;
import database.dao.EmployeeDao;

import model.Employee;

@Path("/employees")
public class EmployeeProvider {
  private static Logger log = Logger.getLogger("imisoid");

  private static ConnectionManager connectionManager;
  private static Connection conn = null;

  static {
    connectionManager = new ConnectionManager();
  }

  public static Connection getConnection() throws SQLException {
    if (conn != null && conn.isClosed() != true) {
      log.info("spojeni existuje");
      return conn;
    }
    log.info("---Zacinam spojeni---");
    return connectionManager.getConnection();
  }

  @GET
  @Path("{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployee(@PathParam("icp") String icp) throws Exception {
    log.info("icp: " + icp);
    Employee employee = null;
    try {
      conn = getConnection();
      employee = EmployeeDao.getEmployee(icp, conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return Response.ok(employee).build();
  }

  @GET
  @Path("all/{icp}")
  // TODO seznam vsech
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployeesForUser(@PathParam("icp") String icp) throws Exception {
    log.info("");
    List<Employee> employees = null;
    try {
      conn = getConnection();
      employees = EmployeeDao.getEmployees(icp, conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return Response.ok(employees).build();
  }

  @GET
  @Path("lastevents")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEvents() throws Exception {
    log.info("");
    List<Employee> employees = null;
    try {
      conn = getConnection();
      employees = EmployeeDao.getLastEvents(conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return Response.ok(employees).build();
  }

  @GET
  @Path("lastevents/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEvent(@PathParam("icp") String icp) throws Exception {
    log.info("");
    Employee employee = null;
    try {
      conn = getConnection();
      employee = EmployeeDao.getLastEventForEmployee(icp, conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // TODO null NO_CONTENT
    return Response.ok(employee).build();
  }

}
