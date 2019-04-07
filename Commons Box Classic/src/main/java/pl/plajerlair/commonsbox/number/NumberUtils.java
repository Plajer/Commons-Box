package pl.plajerlair.commonsbox.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class NumberUtils {

  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  public static boolean isDouble(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  /**
   * Rounds value to x places
   *
   * @param value  value to round
   * @param places places
   * @return rounded value
   * @throws IllegalArgumentException when places to round is lower than 0
   */
  public static double round(double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
