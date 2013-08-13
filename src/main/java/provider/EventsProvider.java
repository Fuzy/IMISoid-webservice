package provider;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import exceptions.ServerErrorException;

import manager.EventManager;
import model.Event;

/**
 * Provider for attendance events (entry of employee's arrive or leave).
 * 
 * @author Martin Kadlec, A11N0109P(ZCU)
 * 
 */
@Path("/events")
public class EventsProvider {
  private static Logger log = Logger.getLogger("imisoid");

  /**
   * Delete event.
   * 
   * @param rowid
   *          identificator of event.
   * @return HTTP response.
   */
  @DELETE
  @Path("{rowid}")
  public Response deleteEvent(@PathParam("rowid") String rowid) throws Exception {
    log.info("");
    boolean deleted = false;
    deleted = EventManager.processDeleteEvent(rowid);
    log.info("rowid: " + rowid + " deleted: " + deleted);
    return Response.ok().build();
  }

  /**
   * Returns events of user for selected period.
   * 
   * @param username
   *          identification of user.
   * @param from
   *          start of period (inclusive).
   * @param to
   *          end of period (inclusive).
   * @return HTTP response.
   */
  @GET
  @Path("{username}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEventsForUser(@PathParam("username") String username,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("user: " + username + " from: " + from + " to: " + to);
    List<Event> events = null;
    events = EventManager.processGetEvents(username, from, to);
    if (events != null)
      log.info("events.size(): " + events.size());
    if (events == null || events.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(events).build();
  }

  /**
   * Create event.
   * 
   * @param event
   *          event to create.
   * @return HTTP response.
   * @throws Exception
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createEvent(Event event) throws Exception {
    log.info("event: " + event);
    String rowid = null;
    rowid = EventManager.processCreateEvent(event);
    if (rowid == null)
      throw new ServerErrorException("Vytvoření záznamu se nezdařilo");
    URI createdUri = URI.create(rowid);
    log.info("created: " + rowid);
    return Response.created(createdUri).build();
  }

  /**
   * Update event.
   * 
   * @param event
   *          event to update
   * @return HTTP response.
   * @throws Exception
   */
  @PUT
  @Path("{rowid}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateEvent(Event event) throws Exception {
    log.info("event: " + event);
    boolean updated = EventManager.processUpdateEvent(event);
    log.info("updated: " + updated);
    return Response.status(Response.Status.ACCEPTED).build();
  }

  /**
   * Return total time of all events for period.
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
  public Response getEventsTimeForUser(@PathParam("icp") String icp,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    BigDecimal time = EventManager.getTime(icp, from, to);
    return Response.ok(time).build();
  }

}
