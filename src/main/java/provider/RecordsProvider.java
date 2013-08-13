package provider;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import manager.RecordManager;
import model.Record;

/**
 * Provider for work records.
 * 
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
@Path("/records")
public class RecordsProvider {
  private static Logger log = Logger.getLogger("imisoid");

  /**
   * Returns work records of user for selected period.
   * 
   * @param kodpra
   *          identification of user.
   * @param from
   *          start of period (inclusive).
   * @param to
   *          end of period (inclusive).
   * @return HTTP response.
   */
  @GET
  @Path("{kodpra}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("kodpra") String kodpra,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("user: " + kodpra + " from: " + from + " to: " + to);
    List<Record> records = null;
    records = RecordManager.processGetRecords(kodpra, from, to);
    if (records == null || records.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(records).build();
  }

  /**
   * Return total time of all records for period.
   * 
   * @param icp
   *          identification of user.
   * @param from
   *          start of period (inclusive).
   * @param to
   *          end of period (inclusive).
   * @return HTTP response.
   * @throws Exception
   */
  @GET
  @Path("time/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsTimeForUser(@PathParam("icp") String icp,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    BigDecimal time = RecordManager.getTime(icp, from, to);
    return Response.ok(time).build();
  }
}
