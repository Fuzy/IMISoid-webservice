package libs;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;

public class BArchivLibrary {

	public boolean lzeVlozit(String icp, long date) {
	  OracleConnection conn = null;
    Statement stmt = null;
    ResultSet rset = null;
    java.sql.Date PomerOd = null, PomerDo = null, Vyneti = null, Konec = null;
    try {
      Class.forName("oracle.jdbc.OracleDriver");
      conn = (OracleConnection) DriverManager.getConnection(url, user, password);

      stmt = conn.createStatement();
      // rset = stmt.executeQuery("select * from ZAMESTNANEC where rownum < 3");
      rset = stmt.executeQuery("select trunc(pomer_od) PomerOd,"
          + " trunc(nvl(pomer_do,to_date('4000','YYYY'))) PomerDo,"
          + "trunc(nvl(datum_vyneti,to_date('4000','YYYY'))) Vyneti,"
          + " trunc(nvl(datum_konec_vyneti,to_date('1000','YYYY'))) Konec"
          + " from zamestnanec  where icp = '0000001'");

      boolean found = rset.next();
      if (found) {
        PomerOd = rset.getDate(1);
        PomerDo = rset.getDate(2);
        Vyneti = rset.getDate(3);
        Konec = rset.getDate(4);
      } else {
        //TODO exception
      }
      System.out.println("PomerOd: " + PomerOd + " PomerDo: " + PomerDo + " Vyneti: " + Vyneti
          + " Konec: " + Konec);
    }
    catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
      closeConnection(conn, stmt, rset);
    }
		return true;
	}
	/*trunc ( date, [ format ] ) If the format parameter is omitted, the trunc function will truncate the date to the day value, 
	so that any hours, minutes, or seconds will be truncated off.*/
	
	/*
	 * function lze_vlozit(PIcp in varchar, PDatum in Date) return Boolean is
cursor c_omezeni is
  select trunc(pomer_od) PomerOd,
         trunc(nvl(pomer_do,to_date('4000','YYYY'))) PomerDo,
         trunc(nvl(datum_vyneti,to_date('4000','YYYY'))) Vyneti,
         trunc(nvl(datum_konec_vyneti,to_date('1000','YYYY'))) Konec
    from zamestnanec
    where icp = PIcp;
  r_omez  c_omezeni%ROWTYPE;
  VDatum  Date := trunc(PDatum);
begin
  open c_omezeni;
  fetch c_omezeni into r_omez;
  if c_omezeni%NOTFOUND
   then
    msg_alert_s('BLB-03401: Nenalezeno požadované ICP zaměstnance.','E');
    raise FORM_TRIGGER_FAILURE;
  end if;
  close c_omezeni;
  if VDatum >= r_omez.PomerOd and VDatum <= r_omez.PomerDo and
     ((r_omez.Konec > r_omez.Vyneti and
       (VDatum < r_omez.Vyneti or VDatum > r_omez.Konec)) or
      (VDatum > r_omez.Konec and VDatum < r_omez.Vyneti))
   then
    return true;
   else
    return false;
  end if;
RETURN NULL; end;

	 * */

}
