package idea.utility;

import javax.ws.rs.WebApplicationException;

public class Validate {
  private Validate() {};

  public static void isTrue(final boolean expression, final String message,
      final int status, final Object... values) throws WebApplicationException {
    if (!expression) {
      throw new WebApplicationException(String.format(message, values), status);
    }
  }

  public static void isFalse(final boolean expression, final String message, 
      final int status, final Object... values) throws WebApplicationException {
    if (expression) {
      throw new WebApplicationException(String.format(message, values), status);
    } 
  }

  public static void isNotNull(final Object object, final String message, 
      final int status, final Object... values) throws WebApplicationException {
    if (object == null) {
      throw new WebApplicationException(String.format(message, values), status);
    }
  }
}
