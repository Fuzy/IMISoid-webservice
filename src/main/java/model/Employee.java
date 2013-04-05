package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
  String icp;
  String kodpra;
  boolean isSubordinate;
  long lastEventTime;
  String kod_po;
  
  public Employee() {
  }
  
  public Employee(String icp, String kodpra, boolean isSubordinate, long lastEventTime,
      String kod_po) {
    super();
    this.icp = icp;
    this.kodpra = kodpra;
    this.isSubordinate = isSubordinate;
    this.lastEventTime = lastEventTime;
    this.kod_po = kod_po;
  }

  public String getIcp() {
    return icp;
  }

  public void setIcp(String icp) {
    this.icp = icp;
  }

  public String getKodpra() {
    return kodpra;
  }

  public void setKodpra(String kodpra) {
    this.kodpra = kodpra;
  }

  public boolean isSubordinate() {
    return isSubordinate;
  }

  public void setSubordinate(boolean isSubordinate) {
    this.isSubordinate = isSubordinate;
  }

  public long getLastEventTime() {
    return lastEventTime;
  }

  public void setLastEventTime(long lastEventTime) {
    this.lastEventTime = lastEventTime;
  }

  public String getKod_po() {
    return kod_po;
  }

  public void setKod_po(String kod_po) {
    this.kod_po = kod_po;
  }
  
  public static Employee resultSetToEmployee(ResultSet rsSet) throws SQLException {
    Employee employee = new Employee();
    employee.setIcp(rsSet.getString(COL_ICP));
    employee.setKodpra(rsSet.getString(COL_KODPRA));
    employee.setSubordinate(rsSet.getBoolean(COL_SUB));
    return employee;    
  }

  @Override
  public String toString() {
    return "Employee [icp=" + icp + ", kodpra=" + kodpra + ", isSubordinate=" + isSubordinate
        + ", lastEventTime=" + lastEventTime + ", kod_po=" + kod_po + "]";
  }
  
  private static String COL_ICP = "ICP";
  private static String COL_KODPRA = "KODPRA";
  private static String COL_SUB = "SUB";

  

}
