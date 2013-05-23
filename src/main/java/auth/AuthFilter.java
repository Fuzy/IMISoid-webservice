package auth;

import java.sql.SQLException;
import java.util.Arrays;
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
    log.info("path " + request.getPath());
    String[] splits = request.getPath().split("/");
    log.info("splits " + Arrays.toString(splits));
    boolean isTestMode = false;
    if (splits.length > 0 && splits[0].equals(TEST_PATH)) {
      isTestMode = true;
    }
    log.info("isTestMode " + isTestMode);

    
    boolean isAuthorized = false;
    String authorization = request.getHeaderValue("Authorization");
    log.info("authorization: " + authorization);
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
