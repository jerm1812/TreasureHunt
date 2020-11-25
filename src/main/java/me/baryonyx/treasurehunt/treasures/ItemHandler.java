package me.baryonyx.treasurehunt.treasures;

import me.baryonyx.treasurehunt.TreasureHunt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.Objects;

public class ItemHandler {
    private NamespacedKey key;

    public ItemHandler(TreasureHunt plugin) {
        key = new NamespacedKey(plugin, "save");
    }

    ItemStack saveItem() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Save Treasure");
        meta.setLore(Collections.singletonList("Will save this inventory as the block's treasure"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "save");
        item.setItemMeta(meta);
        return item;
    }

    ItemStack addCommandItem() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Add Command");
        meta.setLore(Collections.singletonList("Will allow you to add a command to the treasure"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "addCommand");
        item.setItemMeta(meta);
        return item;
    }

    ItemStack createCommandItem(String command) {
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(command);
        item.setItemMeta(meta);
        return item;
    }

    public boolean hasKey(ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(key, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("Tried to check item but item meta was null");
        }

        return false;
    }

    public String getKey(ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(key, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().info("Tried to check item but item meta was null");
        }

        return null;
    }
}
