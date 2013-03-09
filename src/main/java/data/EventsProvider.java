package data;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

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

import exceptions.MyException;

@Path("/events")
public class EventsProvider {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  @DELETE
  @Path("{rowid}")
  public Response deleteEvent(@PathParam("rowid") String rowid) {    
    boolean deleted = false;
    String errMsg = null;
    try {
      deleted = EventDao.deleteEvent(rowid);
    }
    catch (SQLException e) {
      processServerError(e);
    }
    System.out.println("EventsProvider.deleteEvent() rowid: " + rowid + " deleted: " + deleted + " errMsg: " + errMsg);
    //TODO neresim deleted
    return Response.ok().build();
  }

  @GET
  @Produces(MediaType.TEXT_XML)
  public List<Event> getEventsToBrowser() {
    try {
      System.out.println("EventsProvider.getEventsToBrowser()");
      return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
    }
    catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    /*
     * Event event1 = new Event(); event1.setIcp("1"); Event event2 = new
     * Event(); event2.setIcp("2"); List<Event> events = new ArrayList<Event>();
     * events.add(event1); events.add(event2); return events;
     */
  }

  // http://localhost:8080/Imisoid_WS/events/0000001?from=29.7.2004&to=30.7.2004
  // ?from={from}&to={to}"
  //TODO syncMarkerFrom
  @GET
  @Path("{username}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEventsForUser(@PathParam("username") String username,
      @QueryParam("from") String from, @QueryParam("to") String to) {
    System.out.println("EventsProvider.getEventsForUser() " + "user: " + username + " from: "
        + from + " to: " + to);
    List<Event> events = null;
    try {
      // return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
      events = EventDao.getEvents(username, from, to);
    }
    catch (SQLException e) {
      processServerError(e);
    }
    if (events == null || events.isEmpty())  return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(events).build();
  }

  /*@POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void updateEvent(@FormParam("event") String eventIn) {
    System.out.println("EventsProvider.updateEvent()");
    if (eventIn == null) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    try {
      JSONObject event = new JSONObject(eventIn);
      System.out.println("Event: " + event);
      //boolean updated = EventDao.updateEvent(event);
    }
    catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }*/
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateEvent(Event event) {
    System.out.println("Event: " + event);
    //boolean created
    
    if (event.getServer_id() ==  null) {
      // insert
      String rowid = null; 
      try {
        rowid = EventDao.createEvent(event);
      }
      catch (SQLException e) {
        processServerError(e);
      }
      URI createdUri = URI.create(rowid);//TODO null pointer ex
      System.out.println("EventsProvider.createOrUpdateEvent() created: " + rowid);
      return Response.created(createdUri).build();
       
    } else { 
      // update
      boolean updated = false;
      try {
        updated = EventDao.updateEvent(event);
      }
      catch (SQLException e) {
        processServerError(e);
      }
      System.out.println("EventsProvider.createOrUpdateEvent() updated: " + updated); 
      return Response.status(Response.Status.ACCEPTED).build();
    }
  }
  
  private void processServerError(SQLException e) {
    throw new MyException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

}
