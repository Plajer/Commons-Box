package pl.plajerlair.commonsbox.minecraft.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 * @version 1.0.0
 */
public class ItemBuilder {

  private final ItemStack itemStack;

  public ItemBuilder(final ItemStack itemStack) {
    this.itemStack = itemStack == null ? new ItemStack(Material.STONE) : itemStack;
  }

  public ItemBuilder(final Material material) {
    this.itemStack = new ItemStack(material == null ? Material.STONE : material);
  }

  public ItemBuilder type(Material material) {
    this.itemStack.setType(material == null ? Material.STONE : material);
    return this;
  }

  public ItemBuilder amount(int amount) {
    this.itemStack.setAmount(amount < 1 ? 1 : amount);
    return this;
  }

  public ItemBuilder data(byte data) {
    this.itemStack.getData().setData(data);
    return this;
  }

  public ItemBuilder name(final String name) {
    final ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      meta.setDisplayName(name == null ? "" : name);
      itemStack.setItemMeta(meta);
    }
    return this;
  }

  public ItemBuilder enchantment(Enchantment enchantment) {
    this.itemStack.addUnsafeEnchantment(enchantment, 1);
    return this;
  }

  public ItemBuilder enchantment(Enchantment enchantment, int level) {
    this.itemStack.addUnsafeEnchantment(enchantment, level);
    return this;
  }

  public ItemBuilder lore(final String... name) {
    return lore(Arrays.asList(name));
  }

  public ItemBuilder lore(final List<String> name) {
    final ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
      if (name != null) {
        lore.addAll(name);
      }
      meta.setLore(lore);
      itemStack.setItemMeta(meta);
    }
    return this;
  }

  public ItemBuilder colorizeItem() {
    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      if (meta.hasDisplayName()) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
      }
      if (meta.hasLore()) {
        meta.setLore(meta.getLore().stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
      }
    }
    return this;
  }

  public ItemStack build() {
    return itemStack;
  }

}
