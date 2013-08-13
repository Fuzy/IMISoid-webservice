package database.dao;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Employee;

/**
 * Data access object for employees.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class EmployeeDao {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String DAYS_LIMIT = "100";// TODO mensi interval do
                                                 // ostreho provozu
  private static final String SQL_GET_EMPLOYEES = ""
      + "select '1' as \"SUB\",z.icp,z.jmeno,o.kodpra from zamestnanec z, osoba o "
      + "where z.icp = o.oscislo and (z.pomer_do >= SYSDATE or z.pomer_do is null) and z.icp_ved like ? " 
      + "union "
      + "select '0' as \"SUB\",z.icp,z.jmeno,o.kodpra from zamestnanec z, osoba o "
      + "where z.icp = o.oscislo and (z.pomer_do >= SYSDATE or z.pomer_do is null) and z.icp_ved not like ?";

  private static final String SQL_GET_EMPLOYEES_LAST_EVENT = "select k.icp, k.datum, k.kod_po, k.druh, k.cas "
      + "from (select ki.icp, ki.datum, ki.kod_po, ki.druh, ki.cas, "
      + "ROW_NUMBER() over (partition by ki.icp order by datum desc, cas desc) rnk "
      + "from karta ki where ki.datum > (sysdate - " + DAYS_LIMIT + ")) k where rnk = 1";

  private static final String SQL_GET_LAST_EVENT_FOR_EMPLOYEE = "select * from "
      + "(select k.icp, k.datum, k.kod_po, k.druh, k.cas from karta k "
      + "where k.datum > (sysdate - " + DAYS_LIMIT + ") and k.icp like ? "
      + "order by k.datum desc, k.cas desc) where rownum <=1";
  
  private static final String SQL_GET_EMPLOYEE = "select z.icp, z.jmeno, o.kodpra from zamestnanec z, osoba o " +
  		"where z.icp like ? and z.icp = o.oscislo";

  public static List<Employee> getEmployees(String icp, Connection conn) throws SQLException {
    log.info("");

    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Employee> employees = new ArrayList<Employee>();
    try {
      stmt = conn.prepareStatement(SQL_GET_EMPLOYEES);
      stmt.setString(1, icp);
      stmt.setString(2, icp);
      rset = stmt.executeQuery();
      while (rset.next()) {
        Employee employee = Employee.resultSetToEmployee(rset);
        employees.add(employee);
        log.info(employee.toString());
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

    return employees;
  }

  public static List<Employee> getLastEvents(Connection conn) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Employee> employees = new ArrayList<Employee>();

    try {
      stmt = conn.prepareStatement(SQL_GET_EMPLOYEES_LAST_EVENT);
      rset = stmt.executeQuery();
      while (rset.next()) {
        Employee employee = Employee.resultSetToEmployee(rset);
        employees.add(employee);
        log.info(employee.toString());
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

    return employees;
  }

  public static Employee getLastEventForEmployee(String icp, Connection conn) throws SQLException {
    log.info("");

    PreparedStatement stmt = null;
    ResultSet rset = null;
    Employee employee = null;

    try {
      stmt = conn.prepareStatement(SQL_GET_LAST_EVENT_FOR_EMPLOYEE);
      stmt.setString(1, icp);
      rset = stmt.executeQuery();
      while (rset.next()) {
        employee = Employee.resultSetToEmployee(rset);
        log.info(employee.toString());
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

    return employee;
  }

  public static Employee getEmployee(String icp, Connection conn) throws SQLException {
    log.info("");

    PreparedStatement stmt = null;
    ResultSet rset = null;
    Employee employee = null;

    try {
      stmt = conn.prepareStatement(SQL_GET_EMPLOYEE);
      stmt.setString(1, icp);
      rset = stmt.executeQuery();
      while (rset.next()) {
        employee = Employee.resultSetToEmployee(rset);
        log.info(employee.toString());
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

    return employee;
  }
}
