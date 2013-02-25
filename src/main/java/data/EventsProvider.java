package data;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/events")
public class EventsProvider { 
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@DELETE
	@Path("{rowid}")
	public void deleteEvent(@PathParam("rowid") String rowid) {
		System.out.println("EventsProvider.deleteEvent() rowid: " + rowid);
		System.out.println(EventDao.deleteEvent(rowid));
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Event> getEventsToBrowser() {
		try {
			System.out.println("EventsProvider.getEventsToBrowser()");
			return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		/*
		 * Event event1 = new Event(); event1.setIcp("1"); Event event2 = new
		 * Event(); event2.setIcp("2"); List<Event> events = new
		 * ArrayList<Event>(); events.add(event1); events.add(event2); return
		 * events;
		 */
	}

	// http://localhost:8080/Imisoid_WS/events/0000001?from=29.7.2004&to=30.7.2004
	// ?from={from}&to={to}"
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEventsForUser(@PathParam("username") String username,
			@QueryParam("from") String from, @QueryParam("to") String to) {
		System.out.println("EventsProvider.getEventsForUser() " + "user: "
				+ username + " from: " + from + " to: " + to);
		try {
			// return EventDao.getEvents("0000001", "29.7.2004", "30.7.2004");
			return EventDao.getEvents(username, from, to);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createEvent(@FormParam("event") String eventIn) {
		try {
			JSONObject event = new JSONObject(eventIn);
			System.out.println("Event: " + event);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
