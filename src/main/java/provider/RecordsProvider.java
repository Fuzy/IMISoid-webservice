package provider;

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

import exceptions.MyException;

@Path("/records")
public class RecordsProvider {
  private static Logger log = Logger.getLogger("imisoid");

  @GET
  @Path("{username}")//TODO icp vs username
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("username") String username,
      @QueryParam("from") String from, @QueryParam("to") String to) throws MyException {
    log.info("user: " + username + " from: " + from + " to: " + to);
    List<Record> records = null;
    try {
      // return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
      records = RecordManager.processGetRecords(username, from, to);
      log.info("events.size(): " + records.size());
    }
    catch (Exception e) {
      processServerError(e);
    }
    if (records == null || records.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(records).build();
  }
  
  private void processServerError(Exception e) throws MyException {
    log.warning(e.getMessage());
    //throw new MyException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    throw new MyException(e.getMessage());
  }
}
