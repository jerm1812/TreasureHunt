package me.baryonyx.treasurehunt.treasures;

import me.baryonyx.treasurehunt.configuration.Config;
import me.baryonyx.treasurehunt.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerHandler {
    private Config config;
    private ItemHandler itemHandler;

    public List<UUID> playersInEditMode;
    public List<UUID> playersAddingCommands;
    private HashMap<UUID, Inventory> treasureInventories;
    private HashMap<UUID, Treasure> treasures;

    public PlayerHandler(Config config, ItemHandler itemHandler) {
        this.config = config;
        this.itemHandler = itemHandler;

        playersInEditMode = new ArrayList<>();
        playersAddingCommands = new ArrayList<>();
        treasureInventories = new HashMap<>();
        treasures = new HashMap<>();
    }

    public void togglePlayer(Player player) {
        if (!playersInEditMode.contains(player.getUniqueId())) {
            playersInEditMode.add(player.getUniqueId());
            player.sendMessage("[TreasureHunt] You are now in edit mode!");
        }
        else {
            playersInEditMode.remove(player.getUniqueId());
            player.sendMessage("[TreasureHunt] You have left edit mode!");
        }
    }

    public void removePlayerInventory(UUID player) {
        treasureInventories.remove(player);
    }

    public void createTreasure(Player player, Location location) {
        Treasure treasure = new Treasure(location);
        openTreasure(player, treasure);
    }

    public void editTreasure(Player player, Location location) {
        Treasure treasure = config.getTreasure(location);

        if (treasure == null) return;

        treasure.location = location;
        openTreasure(player, treasure);
    }

    private void openTreasure(Player player, Treasure treasure) {
        Inventory inventory = createInventory(player, treasure);
        treasureInventories.put(player.getUniqueId(), inventory);
        treasures.put(player.getUniqueId(), treasure);
        player.openInventory(inventory);
    }

    private Inventory createInventory(Player player, Treasure treasure) {
        Inventory inventory = Bukkit.createInventory(player, 27);
        inventory.setItem(22, itemHandler.saveItem());
        inventory.setItem(17, itemHandler.addCommandItem());

        int place = 0;

        for (ItemStack item : treasure.items) {
            inventory.setItem(place++, item);
        }

        place = 9;

        for (String command : treasure.commands) {
            inventory.setItem(place++, itemHandler.createCommandItem(command));
        }

        return inventory;
    }

    public boolean hasInventory(Inventory inventory) {
        return treasureInventories.containsValue(inventory);
    }

    public Inventory playersInventory(UUID player) {
        return treasureInventories.get(player);
    }

    public void promptForCommand(Player player) {
        playersAddingCommands.add(player.getUniqueId());
        player.sendMessage("Please type the command in chat you would like to add to the treasure");
        player.closeInventory();
        treasureInventories.remove(player.getUniqueId());
    }

    public void saveTreasure(UUID player) {
        Treasure treasure = treasures.get(player);
        config.saveTreasure(treasure, treasure.location);
        treasures.remove(player);
        treasureInventories.remove(player);
    }

    public void addItemToTreasure(UUID player, ItemStack item) {
        treasures.get(player).items.add(item.clone());
    }

    public void removeItemFromTreasure(UUID player, int item) {
        treasures.get(player).items.remove(item);
    }

    public void removeCommandFromTreasure(UUID player, int item, Inventory inventory) {
        treasures.get(player).commands.remove(item - 9);
        inventory.remove(inventory.getItem(item));
    }

    public void addCommandToTreasure(Player player, String command) {
        Treasure treasure = treasures.get(player.getUniqueId());

        if (treasure.commands.size() >= 8) {
            player.sendMessage("This treasure already has max commands!");
            return;
        }

        treasure.commands.add(command);
        editTreasure(player, treasure.location);
    }

    public void registerFoundTreasure(Player player, Location location) {
        YamlConfiguration playerFile = config.loadPlayerFile(player);

        String convertedLocation = config.convertLocation(location);

        if (playerFile.isSet(convertedLocation)) {
            long time = playerFile.getLong(convertedLocation) - System.currentTimeMillis() / 1000;
            player.sendMessage("is set " + time);

            if (time > 0) {
                player.sendMessage("You have already found this treasure! Please wait " + TimeConverter.convertMillisecondsToTime(time));
                return;
            }
        }

        givePlayerTreasureRewards(player, location);
        config.setFoundTreasureToPlayer(player, location);
        player.sendMessage("You found a treasure!");
    }

    private void givePlayerTreasureRewards(Player player, Location location) {
        Treasure treasure = config.getTreasure(location);

        if (treasure == null) {
            return;
        }

        if (!treasure.items.isEmpty()) {
            for (ItemStack item : treasure.items) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(item);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
        }

        if (!treasure.commands.isEmpty()) {
            for (String command : treasure.commands) {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                command = command.replace("%player%", player.getName()).substring(1);
                Bukkit.dispatchCommand(console, command);
            }
        }
    }
}
