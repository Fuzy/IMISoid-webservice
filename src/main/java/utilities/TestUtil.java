package utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility method for testing provider.
 * @author Martin Kadlec, A11N0109P(ZCU)
 *
 */
public class TestUtil {
  public static final DateFormat dfAbbr = new SimpleDateFormat("d.M.yy");

  
  public static long dateToLong(String dateStr) throws ParseException {
      Date date = dfAbbr.parse(dateStr);
      return date.getTime();   
  }

}
