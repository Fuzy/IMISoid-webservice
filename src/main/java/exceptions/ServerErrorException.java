package exceptions;

/**
 * Exception for error on server side.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class ServerErrorException extends Exception {
  private static final long serialVersionUID = -42349007439176058L;

  public ServerErrorException(String message) {
    super( message);
  }

}
