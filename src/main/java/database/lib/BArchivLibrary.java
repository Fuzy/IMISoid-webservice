package database.lib;

import static utilities.Util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static database.DatabaseUtility.*;
import exceptions.FormTriggerFailureException;

public class BArchivLibrary {

  /*private static ConnectionManager connectionManager;

  static {
    connectionManager = new ConnectionManager();
  }

  private static Connection getConnection() throws SQLException {
    return connectionManager.getConnection();
  }*/
  
  public static boolean lzeVlozit(String icp, long date, Connection conn) throws SQLException, FormTriggerFailureException {
    //Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    long PomerOd = -1, PomerDo = -1, Vyneti = -1, Konec = -1;
    long VDatum = date; // TODO trunc?
    try {
      //conn = getConnection();

      stmt = conn.prepareStatement("select trunc(pomer_od) PomerOd,"
          + " trunc(nvl(pomer_do,to_date('4000','YYYY'))) PomerDo,"
          + "trunc(nvl(datum_vyneti,to_date('4000','YYYY'))) Vyneti,"
          + " trunc(nvl(datum_konec_vyneti,to_date('1000','YYYY'))) Konec"
          + " from zamestnanec  where icp like ?");//'0000001'
      stmt.setString(1, icp);
      rset = stmt.executeQuery();

      boolean found = rset.next();
      if (found) {
        PomerOd = dateToMsSinceEpoch(rset.getDate(1));
        PomerDo = dateToMsSinceEpoch(rset.getDate(2));
        Vyneti = dateToMsSinceEpoch(rset.getDate(3));
        Konec = dateToMsSinceEpoch(rset.getDate(4));
      }
      else {
        throw new FormTriggerFailureException("BLB-03401: Nenalezeno požadované ICP zaměstnance.");
      }
      System.out.println("VDatum: " + VDatum + " PomerOd: " + PomerOd + " PomerDo: " + PomerDo
          + " Vyneti: " + Vyneti + " Konec: " + Konec);

      if (VDatum >= PomerOd
          && VDatum <= PomerDo
          && ((Konec > Vyneti && (VDatum < Vyneti || VDatum > Konec)) || (VDatum > Konec && VDatum < Vyneti))) {
        return true;
      }
      else {
        return false;
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
  }

}
