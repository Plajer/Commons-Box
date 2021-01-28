package pl.plajerlair.commonsbox.minecraft.serialization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class InventorySerializer {

  private InventorySerializer() {
  }

  /**
   * Saves player inventory to file in plugin directory
   *
   * @param plugin javaplugin to get data folder
   * @param player player to save data
   * @return true if saved properly, false if inventory is null or couldn't save
   */
  public static boolean saveInventoryToFile(JavaPlugin plugin, Player player) {
    String uuid = player.getUniqueId().toString();
    PlayerInventory inventory = player.getInventory();
    File path = new File(plugin.getDataFolder() + File.separator + "inventories");
    if (inventory == null) {
      return false;
    }
    try {
      File invFile = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator, uuid + ".invsave");
      if (!path.exists()) {
        path.mkdir();
      }
      if (invFile.exists()) {
        invFile.delete();
      }
      FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);

      invConfig.set("ExperienceProgress", player.getExp());
      invConfig.set("ExperienceLevel", player.getLevel());
      invConfig.set("Current health", player.getHealth());
      invConfig.set("Max health", player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
      invConfig.set("Food", player.getFoodLevel());
      invConfig.set("Saturation", player.getSaturation());
      invConfig.set("Fire ticks", player.getFireTicks());
      invConfig.set("GameMode", player.getGameMode().name());
      invConfig.set("Allow flight", player.getAllowFlight());

      invConfig.set("Size", inventory.getSize());
      invConfig.set("Max stack size", inventory.getMaxStackSize());
      List<String> activePotions = new ArrayList<>();
      for (PotionEffect potion : player.getActivePotionEffects()) {
        activePotions.add(potion.getType().getName() + "#" + potion.getDuration() + "#" + potion.getAmplifier());
      }
      invConfig.set("Active potion effects", activePotions);
      if (inventory.getHolder() instanceof Player) {
        invConfig.set("Holder", (inventory.getHolder()).getName());
      }

      ItemStack[] invContents = inventory.getContents();
      for (int i = 0; i < invContents.length; i++) {
        ItemStack itemInInv = invContents[i];
        if (itemInInv != null && itemInInv.getType() != Material.AIR) {
          invConfig.set("Slot " + i, itemInInv);
        }
      }

      ItemStack[] armorContents = inventory.getArmorContents();
      for (int b = 0; b < armorContents.length; b++) {
        ItemStack itemStack = armorContents[b];
        if (itemStack != null && itemStack.getType() != Material.AIR) {
          invConfig.set("Armor " + b, itemStack);
        }
      }

      invConfig.save(invFile);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      Bukkit.getConsoleSender().sendMessage("Cannot save inventory of player!");
      Bukkit.getConsoleSender().sendMessage("Disable inventory saving option in config.yml or restart the server!");
      return false;
    }
  }

  private static Inventory getInventoryFromFile(JavaPlugin plugin, String uuid) {
    File file = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator + uuid + ".invsave");
    if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".invsave")) {
      return Bukkit.createInventory(null, 9);
    }
    try {
      FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
      int invSize = invConfig.getInt("Size", 36);
      int invMaxStackSize = invConfig.getInt("Max stack size", 64);
      InventoryHolder invHolder = invConfig.contains("Holder") ? Bukkit.getPlayer(invConfig.getString("Holder")) : null;
      Inventory inventory = Bukkit.getServer().createInventory(invHolder, InventoryType.PLAYER);
      inventory.setMaxStackSize(invMaxStackSize);
      try {
        ItemStack[] invContents = new ItemStack[invSize];
        for (int i = 0; i < invSize; i++) {
          if (invConfig.contains("Slot " + i)) {
            invContents[i] = invConfig.getItemStack("Slot " + i);
          } else {
            invContents[i] = new ItemStack(Material.AIR);
          }
        }
        inventory.setContents(invContents);

      } catch (Exception ex) {
        ex.printStackTrace();
        Bukkit.getConsoleSender().sendMessage("Cannot save inventory of player! Could not get armor!");
        Bukkit.getConsoleSender().sendMessage("Disable inventory saving option in config.yml or restart the server!");
      }
      file.delete();
      return inventory;
    } catch (Exception ex) {
      ex.printStackTrace();
      Bukkit.getConsoleSender().sendMessage("Cannot save inventory of player!");
      Bukkit.getConsoleSender().sendMessage("Disable inventory saving option in config.yml or restart the server!");
      return Bukkit.createInventory(null, 9);
    }
  }

  /**
   * Loads inventory of player from data folder
   *
   * @param plugin javaplugin to get data folder
   * @param player load inventory of this player
   */
  public static void loadInventory(JavaPlugin plugin, Player player) {
    File file = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator + player.getUniqueId().toString() + ".invsave");
    if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".invsave")) {
      return;
    }
    try {
      FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
      try {
        ItemStack[] armor = new ItemStack[player.getInventory().getArmorContents().length];
        for (int i = 0; i < player.getInventory().getArmorContents().length; i++) {
          if (invConfig.contains("Armor " + i)) {
            armor[i] = invConfig.getItemStack("Armor " + i);
          } else {
            armor[i] = new ItemStack(Material.AIR);
          }
        }
        player.getInventory().setArmorContents(armor);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(invConfig.getDouble("Max health"));
        player.setExp(0);
        player.setLevel(0);
        player.setLevel(invConfig.getInt("ExperienceLevel"));
        player.setExp(Float.parseFloat(invConfig.getString("ExperienceProgress")));
        player.setHealth(invConfig.getDouble("Current health"));
        player.setFoodLevel(invConfig.getInt("Food"));
        player.setSaturation(Float.parseFloat(invConfig.getString("Saturation")));
        player.setFireTicks(invConfig.getInt("Fire ticks"));
        player.setGameMode(GameMode.valueOf(invConfig.getString("GameMode")));
        player.setAllowFlight(invConfig.getBoolean("Allow flight"));
        List<String> activePotions = invConfig.getStringList("Active potion effects");
        for (String potion : activePotions) {
          String[] splited = potion.split("#");
          player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(splited[0]), Integer.valueOf(splited[1]), Integer.valueOf(splited[2])));
        }
      } catch (Exception ignored) {
      }
      Inventory inventory = getInventoryFromFile(plugin, player.getUniqueId().toString());

      for (int i = 0; i < inventory.getContents().length; i++) {
        if (inventory.getItem(i) != null) {
          player.getInventory().setItem(i, inventory.getItem(i));
        }
      }

      player.updateInventory();
    } catch (Exception ignored) {
      //ignore any exceptions
    }
  }

}
