package pl.plajerlair.commonsbox.minecraft.misc;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
/**
 * @author Tigerpanzer_02
 * <p>
 * Created at 26.12.2020
 */
public class ColorUtil {
  private static final Map<ChatColor, ColorSet> colorMap = new HashMap<>();

  static {
    colorMap.put(ChatColor.BLACK, new ColorSet(0, 0, 0));
    colorMap.put(ChatColor.DARK_BLUE, new ColorSet(0, 0, 170));
    colorMap.put(ChatColor.DARK_GREEN, new ColorSet(0, 170, 0));
    colorMap.put(ChatColor.DARK_AQUA, new ColorSet(0, 170, 170));
    colorMap.put(ChatColor.DARK_RED, new ColorSet(170, 0, 0));
    colorMap.put(ChatColor.DARK_PURPLE, new ColorSet(170, 0, 170));
    colorMap.put(ChatColor.GOLD, new ColorSet(255, 170, 0));
    colorMap.put(ChatColor.GRAY, new ColorSet(170, 170, 170));
    colorMap.put(ChatColor.DARK_GRAY, new ColorSet(85, 85, 85));
    colorMap.put(ChatColor.BLUE, new ColorSet(85, 85, 255));
    colorMap.put(ChatColor.GREEN, new ColorSet(85, 255, 85));
    colorMap.put(ChatColor.AQUA, new ColorSet(85, 255, 255));
    colorMap.put(ChatColor.RED, new ColorSet(255, 85, 85));
    colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet(255, 85, 255));
    colorMap.put(ChatColor.YELLOW, new ColorSet(255, 255, 85));
    colorMap.put(ChatColor.WHITE, new ColorSet(255, 255, 255));
  }

  private static class ColorSet {
    private final int red;
    private final int green;
    private final int blue;

    ColorSet(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }

    public int getRed() {
      return red;
    }

    public int getGreen() {
      return green;
    }

    public int getBlue() {
      return blue;
    }
  }

  public static ChatColor fromRGB(int r, int g, int b) {
    TreeMap<Integer, ChatColor> closest = new TreeMap<>();
    colorMap.forEach((color, set) -> {
      int red = Math.abs(r - set.getRed());
      int green = Math.abs(g - set.getGreen());
      int blue = Math.abs(b - set.getBlue());
      closest.put(red + green + blue, color);
    });
    return closest.firstEntry().getValue();
  }

  public static Color fromChatColor(ChatColor chatColor) {
   return Color.fromRGB(colorMap.get(chatColor).getRed(), colorMap.get(chatColor).getGreen(), colorMap.get(chatColor).getBlue());
  }
}