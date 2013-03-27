package exceptions;

public class ValidationException extends Exception {
  private static final long serialVersionUID = -6675830412793348683L;

  public ValidationException(String message) {
    super("Chyba validace: " + message);
  }
  
}
