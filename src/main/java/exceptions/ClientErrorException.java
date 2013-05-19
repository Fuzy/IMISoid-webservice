package exceptions;

public class ClientErrorException extends Exception {
  private static final long serialVersionUID = -6675830412793348683L;

  public ClientErrorException(String message) {
    super(message);
  }
  
}
