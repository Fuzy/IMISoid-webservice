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
      + "where z.icp = o.oscislo and z.pomer_do >= '31.12.2002' and z.icp_ved like ? "
      + "union " + "select '0' as \"SUB\",z.icp, o.kodpra from zamestnanec z, osoba o "
      + "where z.icp = o.oscislo and z.pomer_do >= '31.12.2008' and z.icp_ved not like ?";

  // TODO TO_CHAR (SYSDATE, 'MM-DD-YYYY')

  public static List<Employee> getRecords(String icp, Connection conn) throws SQLException {
    //icp = "1493913";
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
