package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import static database.DatabaseUtility.hasColumn;
import static utilities.Util.*;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Employee {
  private String icp;
  private String kodpra;
  private boolean isSubordinate;
  private long lastEventTime;
  private String kod_po;
  private String druh;
  private String jmeno;

  public Employee() {
    this.lastEventTime = 0;
  }

  public Employee(String icp, String kodpra, String jmeno, boolean isSubordinate,
      long lastEventTime, String kod_po, String druh) {
    super();
    this.icp = icp;
    this.kodpra = kodpra;
    this.jmeno = jmeno;
    this.isSubordinate = isSubordinate;
    this.lastEventTime = lastEventTime;
    this.kod_po = kod_po;
    this.druh = druh;
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

  public void addLastEventTime(long lastEventTime) {
    this.lastEventTime += lastEventTime;
  }

  public String getKod_po() {
    return kod_po;
  }

  public void setKod_po(String kod_po) {
    this.kod_po = kod_po;
  }

  public String getDruh() {
    return druh;
  }

  public void setDruh(String druh) {
    this.druh = druh;
  }

  public String getJmeno() {
    return jmeno;
  }

  public void setJmeno(String jmeno) {
    this.jmeno = jmeno;
  }

  public static Employee resultSetToEmployee(ResultSet rsSet) throws SQLException {
    Employee employee = new Employee();
    if (hasColumn(rsSet, COL_ICP)) {
      employee.setIcp(rsSet.getString(COL_ICP));
    }
    if (hasColumn(rsSet, COL_KODPRA)) {
      employee.setKodpra(rsSet.getString(COL_KODPRA));
    }
    if (hasColumn(rsSet, COL_JMENO)) {
      employee.setJmeno(rsSet.getString(COL_JMENO));
    }
    if (hasColumn(rsSet, COL_SUB)) {
      employee.setSubordinate(rsSet.getBoolean(COL_SUB));
    }
    if (hasColumn(rsSet, COL_DATUM)) {
      employee.addLastEventTime(dateToMsSinceEpoch(rsSet.getDate(COL_DATUM)));
    }
    if (hasColumn(rsSet, COL_KOD_PO)) {
      employee.setKod_po(rsSet.getString(COL_KOD_PO));
    }
    if (hasColumn(rsSet, COL_DRUH)) {
      employee.setDruh(rsSet.getString(COL_DRUH));
    }
    if (hasColumn(rsSet, COL_CAS)) {
      employee.addLastEventTime(timeFromDayDoubleToDayMs(rsSet.getLong(COL_CAS)));
    }

    return employee;
  }

  @Override
  public String toString() {
    return "Employee [icp=" + icp + ", kodpra=" + kodpra + ", isSubordinate=" + isSubordinate
        + ", lastEventTime=" + lastEventTime + ", kod_po=" + kod_po + ", druh=" + druh + ", jmeno="
        + jmeno + "]";
  }

  private static String COL_ICP = "ICP";
  private static String COL_KODPRA = "KODPRA";
  private static String COL_SUB = "SUB";
  private static String COL_DATUM = "DATUM";
  private static String COL_KOD_PO = "KOD_PO";
  private static String COL_DRUH = "DRUH";
  private static String COL_CAS = "CAS";
  private static String COL_JMENO = "JMENO";

}
