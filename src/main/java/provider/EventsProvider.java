package provider;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import manager.EventManager;
import model.Event;

import database.connection.TestConnection;

import exceptions.MyException;

@Path("/events")
public class EventsProvider {
  private static Logger log = Logger.getLogger("imisoid");
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  @DELETE
  @Path("{rowid}")
  public Response deleteEvent(@PathParam("rowid") String rowid) throws MyException {
    log.info("");
    boolean deleted = false;
    String errMsg = null;
    try {
      deleted = EventManager.processDeleteEvent(rowid);
    }
    catch (Exception e) {
      processServerError(e);
    }
    log.info("rowid: " + rowid + " deleted: " + deleted + " errMsg: " + errMsg);
    // TODO neresim deleted
    return Response.ok().build();
  }

  // TODO pouzit jako test spojeni
  /*
   * @GET
   * 
   * @Produces(MediaType.TEXT_PLAIN) public List<Event> getEventsToBrowser() {
   * try { System.out.println("EventsProvider.getEventsToBrowser()"); return
   * EventDao.getEvents("0000001", "29.7.2004", "30.7.2004"); } catch
   * (SQLException e) { e.printStackTrace(); return null; } }
   */

  // TODO WS ok - 200, DB - ne ok 503 (jinak 404)
  @GET
  @Produces({ MediaType.TEXT_PLAIN + ";charset=utf-8", MediaType.TEXT_HTML + ";charset=utf-8" })
  public Response test() {
    log.info("");
    String result = "";
    try {
      result = TestConnection.testConnection();
    }
    catch (Exception e) {
      return Response.ok("Test spojení neúspěšný: " + e.getMessage()).status(500).build();
    }
    return Response.ok("Test spojení úspěšný: " + result).build();
  }

  // http://localhost:8080/Imisoid_WS/events/0000001?from=29.7.2004&to=30.7.2004
  // ?from={from}&to={to}"
  // TODO syncMarkerFrom
  @GET
  @Path("{username}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEventsForUser(@PathParam("username") String username,
      @QueryParam("from") String from, @QueryParam("to") String to) throws MyException {
    log.info("user: " + username + " from: " + from + " to: " + to);
    List<Event> events = null;
    try {
      // return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
      events = EventManager.processGetEvents(username, from, to);
      log.info("events.size(): " + events.size());
    }
    catch (Exception e) {
      processServerError(e);
    }
    if (events == null || events.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(events).build();
  }

  /*
   * @POST
   * 
   * @Consumes(MediaType.APPLICATION_FORM_URLENCODED) public void
   * updateEvent(@FormParam("event") String eventIn) {
   * System.out.println("EventsProvider.updateEvent()"); if (eventIn == null) {
   * throw new WebApplicationException(Response.Status.BAD_REQUEST); } try {
   * JSONObject event = new JSONObject(eventIn); System.out.println("Event: " +
   * event); //boolean updated = EventDao.updateEvent(event); } catch
   * (JSONException e) { // TODO Auto-generated catch block e.printStackTrace();
   * } }
   */

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateEvent(Event event) throws MyException {
    log.info("Event: " + event);
    // boolean created

    if (event.getServer_id() == null) {
      // insert
      String rowid = null;
      try {
        rowid = EventManager.processCreateEvent(event);
      }
      catch (Exception e) {
        processServerError(e);
      }
      URI createdUri = URI.create(rowid);// TODO null pointer ex
      log.info("created: " + rowid);
      return Response.created(createdUri).build();

    }
    else {
      // update
      boolean updated = false;
      try {
        updated = EventManager.processUpdateEvent(event);
      }
      catch (Exception e) {
        processServerError(e);
      }
      log.info("updated: " + updated);
      return Response.status(Response.Status.ACCEPTED).build();
    }
  }

  private void processServerError(Exception e) throws MyException {
    log.warning(e.getMessage());
    //throw new MyException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    throw new MyException(e.getMessage());
  }

  

}
