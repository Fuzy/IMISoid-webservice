package exceptions;

public class ServerErrorException extends Exception {
  private static final long serialVersionUID = -42349007439176058L;

  public ServerErrorException(String message) {
    super( message);
  }

}
