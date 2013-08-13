package exceptions;

/**
 * Exceptions for error on client request side.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class ClientErrorException extends Exception {
  private static final long serialVersionUID = -6675830412793348683L;

  public ClientErrorException(String message) {
    super(message);
  }
  
}
