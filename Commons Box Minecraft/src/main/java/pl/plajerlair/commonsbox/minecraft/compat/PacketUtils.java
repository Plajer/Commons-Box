package pl.plajerlair.commonsbox.minecraft.compat;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class PacketUtils {

  public static void sendPacket(Player player, Object packet) {
    try {
      Object handle = player.getClass().getMethod("getHandle").invoke(player);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException ex) {
      ex.printStackTrace();
    }
  }

  public static Class<?> getNMSClass(String nmsClassName) {
    try {
      return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
      Bukkit.getConsoleSender().sendMessage("Reflection failed for " + nmsClassName);
      return null;
    }
  }

}
