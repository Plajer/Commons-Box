package pl.plajerlair.commonsbox.number;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Plajer
 * <p>
 * Created at 10.07.2019
 */
public class NumberUtilsTest {

  @Test
  public void isInteger() {
    Assert.assertTrue(NumberUtils.isInteger("123"));
    Assert.assertFalse(NumberUtils.isInteger("123.3"));
    Assert.assertFalse(NumberUtils.isInteger("123.text"));
    Assert.assertFalse(NumberUtils.isInteger("text"));
  }

  @Test
  public void isDouble() {
    Assert.assertTrue(NumberUtils.isDouble("123.0"));
    Assert.assertTrue(NumberUtils.isDouble("123.1234"));
    Assert.assertFalse(NumberUtils.isDouble("123.text"));
    Assert.assertFalse(NumberUtils.isDouble("text"));
  }

  @Test
  public void round() {
    Assert.assertEquals(1.3, NumberUtils.round(1.30, 1), 0);
    Assert.assertEquals(1.4, NumberUtils.round(1.369, 1), 0);
    Assert.assertEquals(5.68, NumberUtils.round(5.678, 2), 0);
    Assert.assertEquals(5.67, NumberUtils.round(5.673, 2), 0);
  }

}