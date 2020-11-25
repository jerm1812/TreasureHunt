package me.baryonyx.treasurehunt.listeners;

import me.baryonyx.treasurehunt.configuration.Config;
import me.baryonyx.treasurehunt.treasures.PlayerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    private PlayerHandler playerHandler;
    private Config config;

    public BlockListener(PlayerHandler playerHandler, Config config) {
        this.playerHandler = playerHandler;
        this.config = config;
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent event) {
        if (playerHandler.playersInEditMode.contains(event.getPlayer().getUniqueId())) {
            playerHandler.createTreasure(event.getPlayer(), event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event) {
        if (config.getTreasure(event.getBlock().getLocation()) != null) {
            if (playerHandler.playersInEditMode.contains(event.getPlayer().getUniqueId())) {
                playerHandler.editTreasure(event.getPlayer(), event.getBlock().getLocation());
            }
            else {
                event.getPlayer().sendMessage("[TreasureHunt] You can not break this block!");
            }
            event.setCancelled(true);
        }
    }
}
