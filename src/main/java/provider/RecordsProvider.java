package provider;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.connection.ConnectionManager;
import database.dao.RecordsDao;

import manager.RecordManager;
import model.Record;

@Path("/records")
public class RecordsProvider {
  private static Logger log = Logger.getLogger("imisoid");

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

  @GET
  @Path("{kodpra}")
  // TODO icp vs username
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("kodpra") String kodpra,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("user: " + kodpra + " from: " + from + " to: " + to);
    List<Record> records = null;
    try {
      // return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
      records = RecordManager.processGetRecords(kodpra, from, to);
      log.info("events.size(): " + records.size());
    }
    catch (Exception e) {
      processServerError(e);
    }
    if (records == null || records.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(records).build();
  }

  @GET
  @Path("time/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsTimeForUser(@PathParam("icp") String icp,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    BigDecimal time = null;
    try {
      conn = getConnection();
      time = RecordsDao.getRecordsTime(icp, from, to, conn);
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return Response.ok(time).build();
  }

  private void processServerError(Exception e) throws Exception {
    log.warning(e.getMessage());
    // throw new MyException(e.getMessage(),
    // Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    throw new Exception(e.getMessage());
  }
}
