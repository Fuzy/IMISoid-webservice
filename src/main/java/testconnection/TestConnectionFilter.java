package testconnection;

import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import database.connection.TestConnection;

public class TestConnectionFilter implements ContainerRequestFilter {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String TEST_CONN_PATH = "testconnection";
  private static final String TEST_PATH = "test";

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    String[] splits = request.getPath().split("/");

    if (!(splits.length > 0 && splits[splits.length - 1].equals(TEST_CONN_PATH))) {
      return request; // it is not a connection test, deliver to next step
    }

    boolean isTestMode = false;
    if (splits.length > 0 && splits[0].equals(TEST_PATH)) {
      isTestMode = true;
    }

    Response response = null;
    if (isTestMode) {
      response = Response.ok("Test spojení úspěšný").build();
      log.info("isTestMode: " + isTestMode + " ok");
    }
    else {
      String result = "";
      try {
        result = TestConnection.testConnection();
        response = Response.ok("Test spojení úspěšný: " + result).build();
        log.info("isTestMode: " + isTestMode + " ok");
      }
      catch (Exception e) {
        response = Response.ok("Test spojení neúspěšný: " + e.getMessage()).status(500).build();
        log.info("isTestMode: " + isTestMode + " N/A");
      }     
    }

    throw new WebApplicationException(response);
  }

}
