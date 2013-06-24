package auth;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter {
  private static Logger log = Logger.getLogger("imisoid");
  private static final String TEST_PATH = "test";

  @Override
  public ContainerRequest filter(ContainerRequest request) {
    String[] splits = request.getPath().split("/");
    boolean isTestMode = false;
    if (splits.length > 0 && splits[0].equals(TEST_PATH)) {
      isTestMode = true;
    }

    boolean isAuthorized = false;
    String authorization = request.getHeaderValue("Authorization");
    log.info("path " + request.getPath() + " isTestMode " + isTestMode + " Łauthorization: "
        + authorization);
    if (authorization != null) {
      if (isTestMode) {
        isAuthorized = UserValidator.validateTestUser(authorization);
        log.info("validateTestUser:" + isAuthorized);
      }
      else {
        try {
          isAuthorized = UserValidator.validateUser(authorization);
          log.info("validateUser:" + isAuthorized);
        }
        catch (SQLException e) {
          throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
      }
    }

    if (!isAuthorized) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    return request;
  }

}
