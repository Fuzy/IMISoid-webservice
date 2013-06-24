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
  private String name;
  private boolean isSubordinate;
  private long datum;
  private long cas;
  private String kod_po;
  private String druh;
 

  public Employee() {
    this.datum = 0;
  }   

  public Employee(String icp, String kodpra, String name, boolean isSubordinate, long datum,
      long cas, String kod_po, String druh) {
    super();
    this.icp = icp;
    this.kodpra = kodpra;
    this.name = name;
    this.isSubordinate = isSubordinate;
    this.datum = datum;
    this.cas = cas;
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

  public long getDatum() {
    return datum;
  }

  public void setDatum(long datum) {
    this.datum = datum;
  }

  public long getCas() {
    return cas;
  }

  public void setCas(long cas) {
    this.cas = cas;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
      employee.setName(rsSet.getString(COL_JMENO));
    }
    if (hasColumn(rsSet, COL_SUB)) {
      employee.setSubordinate(rsSet.getBoolean(COL_SUB));
    }
    if (hasColumn(rsSet, COL_DATUM)) {
      employee.setDatum(dateToMsSinceEpoch(rsSet.getDate(COL_DATUM)));
    }
    if (hasColumn(rsSet, COL_KOD_PO)) {
      employee.setKod_po(rsSet.getString(COL_KOD_PO));
    }
    if (hasColumn(rsSet, COL_DRUH)) {
      employee.setDruh(rsSet.getString(COL_DRUH));
    }
    if (hasColumn(rsSet, COL_CAS)) {
      employee.setCas(timeFromDayDoubleToDayMs(rsSet.getLong(COL_CAS)));
    }

    return employee;
  }


  @Override
  public String toString() {
    return "Employee [icp=" + icp + ", kodpra=" + kodpra + ", isSubordinate=" + isSubordinate
        + ", datum=" + datum + ", cas=" + cas + ", kod_po=" + kod_po + ", druh=" + druh + ", name="
        + name + "]";
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
