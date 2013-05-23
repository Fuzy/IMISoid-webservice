package testconnection;

import java.util.Arrays;
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
    log.info("path " + request.getPath());
    String[] splits = request.getPath().split("/");
    log.info("splits " + Arrays.toString(splits));

    if (!(splits.length > 0 && splits[splits.length - 1].equals(TEST_CONN_PATH))) {
      log.info("not a connection test");
      return request; // it is not a connection test, deliver to next step
    }

    boolean isTestMode = false;
    if (splits.length > 0 && splits[0].equals(TEST_PATH)) {
      isTestMode = true;
    }
    log.info("isTestMode " + isTestMode);

    Response response = null;
    if (isTestMode) {
      response = Response.ok("Test spojení úspěšný").build();
    }
    else {
      String result = "";
      try {
        result = TestConnection.testConnection();
        response = Response.ok("Test spojení úspěšný: " + result).build();
      }
      catch (Exception e) {
        log.info("Exception"); 
        response = Response.ok("Test spojení neúspěšný: " + e.getMessage()).status(500).build();
      }
     
    }

    throw new WebApplicationException(response);
  }

}
