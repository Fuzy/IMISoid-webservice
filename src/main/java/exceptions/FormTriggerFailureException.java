package exceptions;

public class FormTriggerFailureException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public FormTriggerFailureException(String message) {
    super(message);
  }

}
