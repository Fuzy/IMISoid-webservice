package provider;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import exceptions.ClientErrorException;

import utilities.TestUtil;

import model.Employee;
import model.Event;
import model.Record;

@Path("/test")
public class TestProvider {
  private static Logger log = Logger.getLogger("imisoid_test");
  private static long HOURS_22 = 75600000;

  private static List<Record> recordsList = new ArrayList<>();
  private static List<Employee> employeesList = new ArrayList<>();
  private static Map<String, Event> eventsList = new HashMap<>();
  private static int serverId = 1;

  static {
    Event event = new Event("0", "123", 1371337200000L, "00", "P", 28000L, "ABC", "O",
        1371337200000L, "poznamka ");
    eventsList.put(String.valueOf(serverId++), event);
    event = new Event("0", "123", 1371337200000L, "00", "O", 30000L, "ABC", "O",
        1371337200000L, "poznamka ");
    eventsList.put(String.valueOf(serverId++), event);

    // Records
    Record record = new Record(new BigDecimal("1"), 1371337200000L, "TST", "R-VV-2013", "V", 13,
        15, 28800000L, "hlaseni", "ukol", "moc prace");
    recordsList.add(record);
    record = new Record(new BigDecimal("2"), 1371337200000L, "TST", "A-VV-2013", "V", 13, 15,
        28800000L, "hlaseni", "ukol", "moc prace");
    recordsList.add(record);
    record = new Record(new BigDecimal("3"), 1371337200000L, "JSA", "A-VV-2013", "V", 13, 15,
        28800000L, "hlaseni", "ukol", "moc prace");
    recordsList.add(record);

    Employee employee = new Employee("123", "ABC", "Pepa Zdepa", false, 1360000000000L, 28800000L,
        "00", "P");
    employeesList.add(employee);
    employee = new Employee("345", "CDE", "Petr Svetr", true, 1360000000000L, 28800000L, "03", "O");
    employeesList.add(employee);
    employee = new Employee("456", "EFG", "Jára Mára", true, 1360000000000L, 28800000L, "03", "O");
    employeesList.add(employee);
    employee = new Employee("TST", "TST", "Jára Mára", false, 1360000000000L, 28800000L, "00", "P");
    employeesList.add(employee);
  }

  @DELETE
  @Path("events/{rowid}")
  public Response deleteEvent(@PathParam("rowid") String rowid) {
    String id = "N/A";
    Event event = eventsList.remove(rowid);
    if (event != null)
      id = rowid;
    log.info(" id: " + id);
    return Response.ok().build();
  }

  @POST
  @Path("events")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateEvent(Event event) throws Exception {
    log.info("Event: " + event);
    String id = String.valueOf(serverId++);
    if (event.getCas() > HOURS_22) {
      throw new ClientErrorException("Zadaný čas přesahuje 22:00");
    }
    eventsList.put(id, event);
    URI createdUri = URI.create(id);
    return Response.created(createdUri).build();
  }

  @PUT
  @Path("events/{rowid}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateEvent(Event event) throws Exception {
    log.info("Event: " + event);
    eventsList.put(event.getServer_id(), event);
    return Response.status(Response.Status.ACCEPTED).build();
  }

  @GET
  @Path("events/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEventsForUser(@PathParam("icp") String icp, @QueryParam("from") String from,
      @QueryParam("to") String to) {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    if (eventsList == null || eventsList.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();

    List<Event> events = new ArrayList<>();
    long fromL, toL;
    try {
      fromL = TestUtil.dateToLong(from);
      toL = TestUtil.dateToLong(to);
    }
    catch (ParseException e) {
      return Response.status(Status.BAD_REQUEST).entity("Špatný formát datumu.").build();
    }

    for (Map.Entry<String, Event> entry : eventsList.entrySet()) {
      String key = entry.getKey();
      Event event = entry.getValue();
      if (event.getIcp().equals(icp) && event.getDatum() >= fromL && event.getDatum() <= toL) {
        event.setServer_id(key);
        events.add(event);
      }

    }
    return Response.ok(events).build();
  }

  @GET
  @Path("employees/lastevents")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEvents() {
    log.info("");
    return Response.ok(employeesList).build();
  }

  @GET
  @Path("employees/all/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployeesForUser(@PathParam("icp") String icp) {
    log.info("");
    return Response.ok(employeesList).build();
  }

  @GET
  @Path("employees/lastevents/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getLastEventForEmployee(@PathParam("icp") String icp) {
    log.info("");
    Employee emp = null;
    for (Employee employee : employeesList) {
      if (employee.getIcp().equals(icp))
        emp = employee;
    }
    if (emp == null)
      return Response.status(Response.Status.NO_CONTENT).build();

    return Response.ok(emp).build();// TODO
  }

  @GET
  @Path("records/{kodpra}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsForUser(@PathParam("kodpra") String kodpra,
      @QueryParam("from") String from, @QueryParam("to") String to) {

    List<Record> records = new ArrayList<>();

    try {
      long fromL = TestUtil.dateToLong(from);
      long toL = TestUtil.dateToLong(to);

      for (Record record : recordsList) {
        if (record.getKodpra().equals(kodpra) && record.getDatum() >= fromL
            && record.getDatum() <= toL) {
          records.add(record);
        }
      }
    }
    catch (ParseException e) {
      return Response.status(Status.BAD_REQUEST).entity("Špatný formát datumu.").build();
    }
    finally {
      log.info("user: " + kodpra + " from: " + from + " to: " + to + " events.size(): "
          + records.size());
    }

    if (records.isEmpty())
      return Response.status(Response.Status.NO_CONTENT).build();
    return Response.ok(recordsList).build();
  }
  
  @GET
  @Path("records/time/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getRecordsTimeForUser(@PathParam("icp") String icp,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    
    return Response.ok(57600000L).build();
  }
  
  @GET
  @Path("events/time/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEventssTimeForUser(@PathParam("icp") String icp,
      @QueryParam("from") String from, @QueryParam("to") String to) throws Exception {
    log.info("icp: " + icp + " from: " + from + " to: " + to);
    
    return Response.ok(39660000L).build();
  }
  
  @GET
  @Path("employees/{icp}")
  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
  public Response getEmployee(@PathParam("icp") String icp) {
    log.info("");
    Employee emp = null;
    for (Employee employee : employeesList) {
      if (employee.getIcp().equals(icp))
        emp = employee;
    }
    return Response.ok(emp).build();
  }

}
