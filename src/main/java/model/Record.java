package model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import static utilities.Util.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Record {
  private BigDecimal id;//TODO string
  private long datum;
  private String kodpra;
  private String zc;
  private String stav_v;
  private int cpolzak;
  private int cpozzak;
  private long mnozstvi_odved;
  private String pozn_hl;
  private String pozn_ukol;
  private String poznamka;

  public Record() {
  }

  public Record(BigDecimal id, long datum, String kodpra, String zc, String stav_v, int cpolzak,
      int cpozzak, long mnozstvi_odved, String pozn_hl, String pozn_ukol, String poznamka) {
    super();
    this.id = id;
    this.datum = datum;
    this.kodpra = kodpra;
    this.zc = zc;
    this.stav_v = stav_v;
    this.cpolzak = cpolzak;
    this.cpozzak = cpozzak;
    this.mnozstvi_odved = mnozstvi_odved;
    this.pozn_hl = pozn_hl;
    this.pozn_ukol = pozn_ukol;
    this.poznamka = poznamka;
  }

  public BigDecimal getId() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public String getZc() {
    return zc;
  }

  public void setZc(String zc) {
    this.zc = zc;
  }

  public String getStav_v() {
    return stav_v;
  }

  public void setStav_v(String stav_v) {
    this.stav_v = stav_v;
  }

  public int getCpolzak() {
    return cpolzak;
  }

  public void setCpolzak(int cpolzak) {
    this.cpolzak = cpolzak;
  }

  public int getCpozzak() {
    return cpozzak;
  }

  public void setCpozzak(int cpozzak) {
    this.cpozzak = cpozzak;
  }

  public long getMnozstvi_odved() {
    return mnozstvi_odved;
  }

  public void setMnozstvi_odved(long mnozstvi_odved) {
    this.mnozstvi_odved = mnozstvi_odved;
  }

  public String getPozn_hl() {
    return pozn_hl;
  }

  public void setPozn_hl(String pozn_hl) {
    this.pozn_hl = pozn_hl;
  }

  public String getPozn_ukol() {
    return pozn_ukol;
  }

  public void setPozn_ukol(String pozn_ukol) {
    this.pozn_ukol = pozn_ukol;
  }

  public String getPoznamka() {
    return poznamka;
  }

  public void setPoznamka(String poznamka) {
    this.poznamka = poznamka;
  }

  public long getDatum() {
    return datum;
  }

  public void setDatum(long datum) {
    this.datum = datum;
  }

  public String getKodpra() {
    return kodpra;
  }

  public void setKodpra(String kodpra) {
    this.kodpra = kodpra;
  }

  public static Record resultSetToRecord(ResultSet rsSet) throws SQLException {
    Record record = new Record();
    record.setId(rsSet.getBigDecimal(COL_ID));
    record.setDatum(dateToMsSinceEpoch(rsSet.getDate(COL_DATUM)));
    record.setMnozstvi_odved(timeFromDayDoubleToDayMs(rsSet.getLong(COL_MNOZSTVI_ODVED)));
    record.setKodpra(rsSet.getString(COL_KODPRA));
    record.setZc(rsSet.getString(COL_ZC));//TODO ciselne typy jsou ok?
    record.setCpolzak(rsSet.getInt(COL_CPOLZAK));
    record.setCpozzak(rsSet.getInt(COL_CPOZZAK));
    record.setStav_v(rsSet.getString(COL_STAV_V));  
    record.setPozn_hl(rsSet.getString(COL_POZN_HL));
    record.setPozn_ukol(rsSet.getString(COL_POZN_UKOL));
    record.setPoznamka(rsSet.getString(COL_POZNAMKA));
    return record;
  }

  @Override
  public String toString() {
    return "Record [id=" + id + ", datum=" + formatDate(datum) + ", kodpra=" + kodpra + ", zc=" + zc
        + ", stav_v=" + stav_v + ", cpolzak=" + cpolzak + ", cpozzak=" + cpozzak
        + ", mnozstvi_odved=" + formatTime(mnozstvi_odved) + ", pozn_hl=" + pozn_hl + ", pozn_ukol="
        + pozn_ukol + ", poznamka=" + poznamka + "]";
  }

  private static String COL_POZN_UKOL = "POZN_UKOL";
  private static String COL_ID = "ID";
  private static String COL_DATUM = "DATUM";
  private static String COL_KODPRA = "KODPRA";
  private static String COL_MNOZSTVI_ODVED = "MNOZSTVI_ODVED";
  private static String COL_POZNAMKA = "POZNAMKA";
  private static String COL_STAV_V = "STAV_V";
  private static String COL_POZN_HL = "POZN_HL";
  private static String COL_ZC = "ZC";
  private static String COL_CPOLZAK = "CPOLZAK";
  private static String COL_CPOZZAK = "CPOZZAK";

}
