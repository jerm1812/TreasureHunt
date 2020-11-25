package me.baryonyx.treasurehunt.configuration;

import me.baryonyx.treasurehunt.TreasureHunt;
import me.baryonyx.treasurehunt.treasures.Treasure;
import me.baryonyx.treasurehunt.utils.TimeConverter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private TreasureHunt plugin;
    private YamlConfiguration treasureFile;
    private YamlConfiguration config;

    public Config(TreasureHunt plugin) {
        this.plugin = plugin;
        loadTreasuresFile();
        loadConfigFile();
    }

    private void loadTreasuresFile() {
        File file = new File(plugin.getDataFolder(), "treasures.yml");
        if (!file.exists()) {
            plugin.saveResource("treasures.yml", false);
        }
        treasureFile = YamlConfiguration.loadConfiguration(file);
    }

    private void loadConfigFile() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveTreasureFile() {
        File file = new File(plugin.getDataFolder(), "treasures.yml");
        try {
            treasureFile.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save the treasureFile file");
        }
    }

    @Nullable
    public Treasure getTreasure(Location location) {
        ConfigurationSection section = treasureFile.getConfigurationSection(convertLocation(location));

        if (section == null) return null;

        List<ItemStack> items = (List<ItemStack>) section.getList("items");
        List<String> commands = section.getStringList("commands");

        return new Treasure(items, commands, location);
    }
    
    public void saveTreasure(Treasure treasure, Location location) {
        treasureFile.set(convertLocation(location) + ".items", treasure.items);
        treasureFile.set(convertLocation(location) + ".commands", treasure.commands);
        saveTreasureFile();
    }

    private String convertLocation(@NotNull Location location) {
        return location.getWorld().getName() + ".x" + location.getBlockX() + "y" + location.getBlockY() + "z" + location.getBlockZ();
    }

    private void saveConfig() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save the config file");
        }
    }

    private void createPlayerFile(@NotNull Player player) {
        YamlConfiguration playerYaml = new YamlConfiguration();
        playerYaml.createSection("treasureFile-found");
        savePlayerFile(player, playerYaml);
    }

    private void savePlayerFile(@NotNull Player player, @NotNull YamlConfiguration playerYaml) {
        File file = new File(plugin.getDataFolder(), "playerdata" + File.separator + player.getUniqueId().toString() + ".yml");
        try {
            playerYaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + player.getName() + "'s file");
        }
    }

    @NotNull
    private YamlConfiguration loadPlayerFile(@NotNull Player player) {
        File file = new File(plugin.getDataFolder(), "playerdata" + File.separator + player.getUniqueId() + ".yml");

        if (!file.exists()) {
            createPlayerFile(player);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void setFoundTreasureToPlayer(@NotNull Player player, @NotNull Block block) {
        YamlConfiguration playerFile = loadPlayerFile(player);
        ConfigurationSection section = playerFile.getConfigurationSection("treasureFile-found");
        section.set(block.getLocation().toString() + ".block", block.getType().toString());
        section.set(block.getLocation().toString() + ".time", System.currentTimeMillis() / 1000 + TimeConverter.convertTimeToSeconds(config.getString("block-find-delay")));
        savePlayerFile(player, playerFile);
    }
}
