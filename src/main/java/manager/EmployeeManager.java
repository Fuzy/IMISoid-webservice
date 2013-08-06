package manager;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import model.Employee;

import database.connection.ConnectionManager;
import database.dao.EmployeeDao;

public class EmployeeManager {
  private static Logger log = Logger.getLogger("imisoid");

  private static ConnectionManager connectionManager;
  private static Connection conn = null;

  static {
    connectionManager = new ConnectionManager();
  }

  private static Connection getConnection() throws SQLException {
    if (conn != null && conn.isClosed() != true) {
      return conn;
    }
    return connectionManager.getConnection();
  }

  public static Employee getEmployee(String icp) throws Exception {
    log.info("");
    conn = getConnection();
    Employee employee = EmployeeDao.getEmployee(icp, conn);
    closeConnection(conn, null, null);
    return employee;
  }
  
  public static Employee getLastEventForEmployee(String icp) throws Exception {
    log.info("");
    conn = getConnection();
    Employee employee = EmployeeDao.getLastEventForEmployee(icp, conn);
    closeConnection(conn, null, null);
    return employee;
  }
  
  public static List<Employee> getEmployeesForUser(String icp) throws Exception {
    log.info("");
    conn = getConnection();
    List<Employee> employees = EmployeeDao.getEmployees(icp, conn);
    closeConnection(conn, null, null);
    return employees;
  }
  
  public static List<Employee> getLastEvents() throws Exception {
    log.info("");
    conn = getConnection();
    List<Employee> employees = EmployeeDao.getLastEvents(conn);
    closeConnection(conn, null, null);
    return employees;
  }

}
