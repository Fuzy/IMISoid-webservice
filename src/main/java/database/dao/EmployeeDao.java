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

public class EmployeeDao {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String SQL_GET_EMPLOYEES = ""
      + "select '1' as \"SUB\",z.icp, o.kodpra from zamestnanec z, osoba o "
      + "where z.icp = o.oscislo and z.pomer_do >= '31.12.2008' and z.icp_ved like ? "
      + "union " + "select '0' as \"SUB\",z.icp, o.kodpra from zamestnanec z, osoba o "
      + "where z.icp = o.oscislo and z.pomer_do >= '31.12.2008' and z.icp_ved not like ?";
  //TODO to vraci i pro neextistuici icp

  // TODO TO_CHAR (SYSDATE, 'MM-DD-YYYY')
  
  private static final String SQL_GET_EMPLOYEES_LAST_EVENT = "select k.icp, k.datum, k.kod_po, k.druh, k.cas " +
  		"from (select ki.icp, ki.datum, ki.kod_po, ki.druh, ki.cas, " +
  		"ROW_NUMBER() over (partition by ki.icp order by datum desc) rnk " +
  		"from karta ki where ki.datum > (sysdate - 100)) k where rnk = 1";
  //TODO mensi interval

  public static List<Employee> getRecords(String icp, Connection conn) throws SQLException {
    //icp = "1493913";
    log.info("");
    
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Employee> employees = new ArrayList<Employee>();

    try {
      //TODO test existence
      stmt = conn.prepareStatement(SQL_GET_EMPLOYEES);
      stmt.setString(1, icp);
      stmt.setString(2, icp);
      rset = stmt.executeQuery();
      while (rset.next()) {
        Employee employee  = Employee.resultSetToEmployee(rset);
        employees.add(employee);
        System.out.println(employee);
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
    
    return employees;
  }
  
  public static List<Employee> getLastRecords(Connection conn) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    List<Employee> employees = new ArrayList<Employee>();

    try {
      stmt = conn.prepareStatement(SQL_GET_EMPLOYEES_LAST_EVENT);
      rset = stmt.executeQuery();
      while (rset.next()) {
        Employee employee  = Employee.resultSetToEmployee(rset);
        employees.add(employee);
        System.out.println(employee);
      }
    }
    catch (SQLException e) {
      log.warning(e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
    
    return employees;
  }
}
