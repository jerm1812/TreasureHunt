package me.baryonyx.treasurehunt;

import me.baryonyx.treasurehunt.commands.BaseCommand;
import me.baryonyx.treasurehunt.configuration.Config;
import me.baryonyx.treasurehunt.listeners.BlockListener;
import me.baryonyx.treasurehunt.listeners.InventoryListener;
import me.baryonyx.treasurehunt.listeners.PlayerListener;
import me.baryonyx.treasurehunt.treasures.ItemHandler;
import me.baryonyx.treasurehunt.treasures.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreasureHunt extends JavaPlugin {


    @Override
    public void onEnable() {
        // Plugin startup logic
        Config config = new Config(this);
        ItemHandler itemHandler = new ItemHandler(this);
        PlayerHandler playerHandler = new PlayerHandler(config, itemHandler);

        getServer().getPluginManager().registerEvents(new BlockListener(playerHandler, config), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerHandler), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(playerHandler, this), this);

        getCommand("treasurehunt").setExecutor(new BaseCommand(playerHandler));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
