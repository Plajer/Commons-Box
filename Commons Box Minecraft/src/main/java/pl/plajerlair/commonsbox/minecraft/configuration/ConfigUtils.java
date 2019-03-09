package pl.plajerlair.commonsbox.minecraft.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class ConfigUtils {

  /**
   * Gets fileconfiguration from data folder of plugin, creates new if not exists
   *
   * @param plugin   javaplugin to get datafolder
   * @param filename file name (without .yml)
   * @return FileConfiguration to edit file
   */
  public static FileConfiguration getConfig(JavaPlugin plugin, String filename) {
    File file = new File(plugin.getDataFolder() + File.separator + filename + ".yml");
    if(!file.exists()) {
      plugin.getLogger().info("Creating " + filename + ".yml because it does not exist!");
      plugin.saveResource(filename + ".yml", true);
    }
    file = new File(plugin.getDataFolder(), filename + ".yml");
    YamlConfiguration config = new YamlConfiguration();
    try {
      config.load(file);
    } catch(InvalidConfigurationException | IOException ex) {
      ex.printStackTrace();
      Bukkit.getConsoleSender().sendMessage("Cannot load file " + filename + ".yml!");
      Bukkit.getConsoleSender().sendMessage("Create blank file " + filename + ".yml or restart the server!");
    }
    return config;
  }

  /**
   * Saves config to specified name
   *
   * @param plugin javaplugin to get data folder
   * @param config FileConfiguration to save
   * @param name   file name to save (without .yml)
   */
  public static void saveConfig(JavaPlugin plugin, FileConfiguration config, String name) {
    try {
      config.save(new File(plugin.getDataFolder(), name + ".yml"));
    } catch(IOException e) {
      e.printStackTrace();
      Bukkit.getConsoleSender().sendMessage("Cannot save file " + name + ".yml!");
      Bukkit.getConsoleSender().sendMessage("Create blank file " + name + ".yml or restart the server!");
    }
  }

}
