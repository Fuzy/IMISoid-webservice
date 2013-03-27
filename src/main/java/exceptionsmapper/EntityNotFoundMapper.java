package exceptionsmapper;

import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import exceptions.MyException;

@Provider
public class EntityNotFoundMapper implements ExceptionMapper<MyException> {
  private static Logger log = Logger.getLogger("imisoid");


  @Override
  public Response toResponse(MyException exception) {
    log.warning(exception.getMessage());
    return Response.status(500).entity(exception.getMessage())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN + "; charset=UTF-8").build();
  }

  /*
   * @Override public Response toResponse(MyException ex) { log.warning("");
   * return
   * Response.status(500).entity(ex.getMessage()).type("text/plain").build(); }
   */
}
