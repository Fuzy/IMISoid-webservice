package auth;

import static database.DatabaseUtility.closeConnection;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import database.connection.ConnectionManager;

import oracle.jdbc.OracleTypes;

public class UserValidator {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String PASS = "TST";
  private static final String ICP = "TST";

  private static ConnectionManager connectionManager;
  private static Connection conn = null;

  static {
    connectionManager = new ConnectionManager();
  }

  public static Connection getConnection() throws SQLException {
    if (conn != null && conn.isClosed() != true) {
      log.info("spojeni existuje");
      return conn;
    }
    log.info("---Zacinam spojeni---");
    return connectionManager.getConnection();
  }

  public static boolean validateHeslo(String icp, String heslo, Connection conn)
      throws SQLException {
    log.info("");
    CallableStatement callableStatement = null;
    BigDecimal bool;
    boolean match = false;

    String SpravneHeslo = "{? = call IMISOID_HESLO_WRAPPER(?, ?)}";
    try {
      callableStatement = conn.prepareCall(SpravneHeslo);
      callableStatement.setQueryTimeout(5);
      callableStatement.setString(2, icp);
      callableStatement.setString(3, heslo);
      callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
      callableStatement.executeUpdate();
      bool = callableStatement.getBigDecimal(1);

      log.info("bool " + bool);
      if (bool.compareTo(BigDecimal.ONE) == 0)
        match = true;
      log.info("match " + match);
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    finally {
      closeConnection(null, callableStatement, null);
    }
    return match;
  }

  public static boolean validateTestUser(String authorization) {
    String[] credentials = parseCredentials(authorization);
    if (credentials[1] == null)
      return false;
    if (credentials[0].equals(ICP) && credentials[1].equals(PASS))
      return true;
    return false;
  }

  public static boolean validateUser(String authorization) throws SQLException {
    String[] credentials = parseCredentials(authorization);
    try {
      conn = getConnection();
      return UserValidator.validateHeslo(credentials[0], credentials[1], conn);
    }
    catch (SQLException e) {
      if (conn != null) conn.rollback();//TODO pokud spojeni neexistuje, null pointer exception
      throw e;
    }
    finally {
      closeConnection(conn, null, null);
    }

  }

  private static String[] parseCredentials(String authorization) {
    String s = authorization.substring(authorization.indexOf(" ") + 1);
    byte[] pair = javax.xml.bind.DatatypeConverter.parseBase64Binary(s);
    String pairStr = new String(pair);
    String[] tmp = pairStr.split(":");
    String[] credentials = new String[2];
    credentials[0] = tmp[0];
    credentials[1] = (tmp.length > 1) ? tmp[1] : null;
    log.info("s:" + s + " icp: " + credentials[0] + "heslo: " + credentials[1]);
    return credentials;
  }
}
