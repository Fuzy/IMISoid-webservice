package database.validation;

import static database.DatabaseUtility.closeConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.ClientErrorException;

import model.Event;

/**
 * Validator of attendance event.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class EventValidator {
  //private static Logger log = Logger.getLogger("imisoid");

  public static void validateEvent(Event event, Connection conn) throws SQLException,
      ClientErrorException {
    validateICP(event.getIcp(), conn);
    validateDruh(event.getDruh(), conn);
    validateKod_po(event.getKod_po(), event.getDruh(), conn);
  }

  private static void validateICP(String icp, Connection conn) throws SQLException,
      ClientErrorException {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {

      stmt = conn.prepareStatement("select 'x' from zamestnanec where icp = ?");
      stmt.setString(1, icp);
      rset = stmt.executeQuery();

      boolean found = rset.next();
      if (found == false)
        throw new ClientErrorException("Chybný kód zaměstnance.");
    }
    catch (SQLException e) {
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }
  }

  private static void validateDruh(String druh, Connection conn) throws ClientErrorException {
    if ((druh.equals("P") || druh.equals("O")) == false) {
      throw new ClientErrorException("Povolené hodnoty jsou P (Příchod), O (Odchod).");
    }
  }

  private static void validateKod_po(String kod_po, String druh, Connection conn)
      throws ClientErrorException, SQLException {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    String priznak;

    try {
      stmt = conn.prepareStatement("select p.kod_po,p.kod_doby A,d.kod_doby B,d.priznak"
          + " from kody_dob d,kody_po p" + " where ?=p.kod_po and p.kod_doby=d.kod_doby");
      stmt.setString(1, kod_po);
      rset = stmt.executeQuery();

      boolean found = rset.next();
      if (found) {
        priznak = rset.getString(4);
        if (druh.equals("P") && ("P-".indexOf(priznak) == -1)) {
          throw new ClientErrorException("Skupinu " + kod_po + " nelze použít při příchodu.");
        }
        if (druh.equals("O") && ("NO-".indexOf(priznak) == -1)) {
          throw new ClientErrorException("Skupinu " + kod_po + " nelze použít při odchodu.");
        }
      }
      else {
        throw new ClientErrorException("Tabulka kod_doby neobsahuje hodnotu " + kod_po);
      }

    }
    catch (SQLException e) {
      throw e;
    }
    finally {
      closeConnection(null, stmt, rset);
    }

  }
}
