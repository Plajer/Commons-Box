package pl.plajerlair.commonsbox.minecraft.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import pl.plajerlair.commonsbox.minecraft.compat.VersionResolver;
import pl.plajerlair.commonsbox.minecraft.compat.XMaterial;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class ItemUtils {

  public static final ItemStack PLAYER_HEAD_ITEM = VersionResolver.isBefore1_13() ? new ItemStack(Material.SKULL_ITEM, 1, (short) 3) : XMaterial.PLAYER_HEAD.parseItem();

  private ItemUtils() {
  }

  /**
   * Checks whether itemstack is named (not null, has meta and display name)
   *
   * @param stack item stack to check
   * @return true if named, false otherwise
   */
  public static boolean isItemStackNamed(ItemStack stack) {
    if (stack == null) {
      return false;
    }
    return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName();
  }

  public static ItemStack getSkull(String url) {
    ItemStack head = PLAYER_HEAD_ITEM.clone();
    if (url.isEmpty()) {
      return head;
    }

    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", url));
    Field profileField;
    try {
      profileField = headMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(headMeta, profile);

    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
    }

    head.setItemMeta(headMeta);
    return head;
  }

}
