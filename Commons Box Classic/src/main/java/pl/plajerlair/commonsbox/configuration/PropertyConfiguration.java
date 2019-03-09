package pl.plajerlair.commonsbox.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class PropertyConfiguration implements Configuration<Properties> {

  private File dataFolder;

  public PropertyConfiguration(File dataFolder) {
    this.dataFolder = dataFolder;
  }

  @Override
  public Properties getConfiguration(String file) {
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(new File(dataFolder, file + ".properties")));
      return properties;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Properties();
  }

  public File getDataFolder() {
    return dataFolder;
  }

}
