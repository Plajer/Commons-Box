package pl.plajerlair.commonsbox.string;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Plajer
 * <p>
 * Created at 10.07.2019
 */
public class StringMatcherTest {

  @Test
  public void match() {
    List<StringMatcher.Match> matches = StringMatcher.match("text", Arrays.asList("textone", "adsasd", "sample"));
    Assert.assertEquals("textone", matches.get(0).getMatch());

    //*best match logic*
    Assert.assertEquals("nothing", StringMatcher.match("123b", Arrays.asList("nothing", "boo", "aaa")).get(0).getMatch());
  }

}