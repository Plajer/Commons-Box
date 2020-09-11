package pl.plajerlair.commonsbox.minecraft.engine;

import java.util.logging.Level;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Bukkit;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class ScriptEngine {

  private final javax.script.ScriptEngine scriptEngine;

  public ScriptEngine() {
    scriptEngine = new ScriptEngineManager().getEngineByName("js");
  }

  public void setValue(String value, Object valueObject) {
    scriptEngine.put(value, valueObject);
  }

  public void execute(String executable) {
    try {
      scriptEngine.eval(executable);
    } catch(ScriptException e) {
      Bukkit.getLogger().log(Level.SEVERE, "Script failed to parse expression! Expression was written wrongly!");
      Bukkit.getLogger().log(Level.SEVERE, "Expression value: " + executable);
      Bukkit.getLogger().log(Level.SEVERE, "Error log:");
      e.printStackTrace();
      Bukkit.getLogger().log(Level.SEVERE, "---- THIS IS AN ISSUE BY USER CONFIGURATION NOT AUTHOR BUG ----");
    }
  }

}
