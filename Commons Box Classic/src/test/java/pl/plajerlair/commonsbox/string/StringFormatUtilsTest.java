package pl.plajerlair.commonsbox.string;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Plajer
 * <p>
 * Created at 10.07.2019
 */
public class StringFormatUtilsTest {

  @Test
  public void formatIntoMMSS() {
    Assert.assertEquals("01:01", StringFormatUtils.formatIntoMMSS(61));
    Assert.assertEquals("04:00", StringFormatUtils.formatIntoMMSS(4 * 60));
  }

}