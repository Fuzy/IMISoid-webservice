package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import static utilities.Util.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {
  private String rowid;
  private String icp;
  private long datum;
  private String kod_po;
  private String druh;
  private long cas;
  private String ic_obs;
  private String typ;
  private long datum_zmeny;
  private String poznamka;

  public Event() {
  }

  public Event(String rowid, String icp, long datum, String kod_po, String druh, long cas,
      String ic_obs, String typ, long datum_zmeny, String poznamka) {
    super();
    this.rowid = rowid;
    this.icp = icp;
    this.datum = datum;
    this.kod_po = kod_po;
    this.druh = druh;
    this.cas = cas;
    this.ic_obs = ic_obs;
    this.typ = typ;
    this.datum_zmeny = datum_zmeny;
    this.poznamka = poznamka;
  }
  
  public Event(Event another) {
    this.rowid = another.rowid;
    this.icp = another.icp;
    this.datum = another.datum;
    this.kod_po = another.kod_po;
    this.druh = another.druh;
    this.cas = another.cas;
    this.ic_obs = another.ic_obs;
    this.typ = another.typ;
    this.datum_zmeny = another.datum_zmeny;
    this.poznamka = another.poznamka;
  }

  public String getServer_id() {
    return rowid;
  }

  public void setServer_id(String server_id) {
    this.rowid = server_id;
  }

  public String getIcp() {
    return icp;
  }

  public void setIcp(String icp) {
    this.icp = icp;
  }

  // @XmlJavaTypeAdapter(JaxbDateSerializer.class)
  public long getDatum() {
    return datum;
  }

  public void setDatum(long datum) {
    this.datum = datum;
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

  public long getCas() {
    return cas;
  }

  public void setCas(long cas) {
    this.cas = cas;
  }

  public String getIc_obs() {
    return ic_obs;
  }

  public void setIc_obs(String ic_obs) {
    this.ic_obs = ic_obs;
  }

  public String getTyp() {
    return typ;
  }

  public void setTyp(String typ) {
    this.typ = typ;
  }

  // @XmlJavaTypeAdapter(JaxbDateSerializer.class)
  public long getDatum_zmeny() {
    return datum_zmeny;
  }

  public void setDatum_zmeny(long datum_zmeny) {
    this.datum_zmeny = datum_zmeny;
  }

  public String getPoznamka() {
    return poznamka;
  }

  public void setPoznamka(String poznamka) {
    this.poznamka = poznamka;
  }

  @Override
  public String toString() {
    return "Event [server_id=" + rowid + ", icp=" + icp + ", datum=" + formatTime(datum)
        + ", kod_po=" + kod_po + ", druh=" + druh + ", cas=" + cas + ", ic_obs=" + ic_obs
        + ", typ=" + typ + ", datum_zmeny=" + formatTime(datum_zmeny) + ", poznamka=" + poznamka
        + "]";
  }

  public static Event resultSetToEvent(ResultSet rsSet) throws SQLException {
    Event event = new Event();
    event.setServer_id(rsSet.getString(COL_SERVER_ID));
    event.setIcp(rsSet.getString(COL_ICP));
    event.setDatum(dateToMsSinceEpoch(rsSet.getDate(COL_DATUM)));
    event.setKod_po(rsSet.getString(COL_KOD_PO));
    event.setDruh(rsSet.getString(COL_DRUH));
    event.setCas(timeFromDayDoubleToDayMs(rsSet.getLong(COL_CAS)));
    event.setIc_obs(rsSet.getString(COL_IC_OBS));
    event.setTyp(rsSet.getString(COL_TYP));
    event.setDatum_zmeny(dateToMsSinceEpoch(rsSet.getDate(COL_DATUM_ZMENY)));
    event.setPoznamka(rsSet.getString(COL_POZNAMKA));
    return event;
  }

  public Object[] getEventAsArrayOfObjects() {
    Object[] values = { icp, longToDate(datum), kod_po, druh, timeFromDayMsToDayDouble(cas),
        ic_obs, typ, longToDate(datum_zmeny), poznamka };
    return values;
  }

  private static String COL_SERVER_ID = "ROWID";
  private static String COL_ICP = "ICP";
  private static String COL_DATUM = "DATUM";
  private static String COL_KOD_PO = "KOD_PO";
  private static String COL_DRUH = "DRUH";
  private static String COL_CAS = "CAS";
  private static String COL_IC_OBS = "IC_OBS";
  private static String COL_TYP = "TYP";
  private static String COL_DATUM_ZMENY = "DATUM_ZMENY";
  private static String COL_POZNAMKA = "POZNAMKA";

}
