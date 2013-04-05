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

import exceptions.MyException;
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
  @Path("{icp}")//TODO icp vs username
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("icp") String icp) throws MyException {
    log.info("");
    List<Employee> employees = null;
    try {
      conn = getConnection();
      employees =  EmployeeDao.getRecords(icp, conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   
    return Response.ok(employees).build();
  }

}
