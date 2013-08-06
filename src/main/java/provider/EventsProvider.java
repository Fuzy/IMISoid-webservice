package provider;

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

import manager.EventManager;
import model.Event;

@Path("/events")
public class EventsProvider {
  private static Logger log = Logger.getLogger("imisoid");

  @DELETE
  @Path("{rowid}")
  public Response deleteEvent(@PathParam("rowid") String rowid) throws Exception {
    log.info("");
    boolean deleted = false;
    deleted = EventManager.processDeleteEvent(rowid);
    log.info("rowid: " + rowid + " deleted: " + deleted);
    return Response.ok().build();
  }

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

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createEvent(Event event) throws Exception {
    log.info("event: " + event);
    String rowid = null;
    rowid = EventManager.processCreateEvent(event);
    URI createdUri = URI.create(rowid);// TODO null pointer ex
    log.info("created: " + rowid);
    return Response.created(createdUri).build();
  }

  @PUT
  @Path("{rowid}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateEvent(Event event) throws Exception {
    log.info("event: " + event);
    boolean updated = EventManager.processUpdateEvent(event);
    log.info("updated: " + updated);
    return Response.status(Response.Status.ACCEPTED).build();
  }

}
