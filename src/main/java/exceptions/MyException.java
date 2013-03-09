package exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyException extends WebApplicationException {
  private static final long serialVersionUID = 1L;

  public MyException(String message, int code) {
    super(Response.status(code).entity(message)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN + "; charset=UTF-8").build());
  }
}
