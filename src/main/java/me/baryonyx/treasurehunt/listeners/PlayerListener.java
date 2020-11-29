package me.baryonyx.treasurehunt.listeners;

import me.baryonyx.treasurehunt.treasures.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private PlayerHandler playerHandler;

    public PlayerListener(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        playerHandler.playersAddingCommands.remove(player.getUniqueId());
        playerHandler.playersInEditMode.remove(player.getUniqueId());
    }

    @EventHandler
    public void PlayerChatEvent(PlayerCommandPreprocessEvent event) {
        if (playerHandler.playersAddingCommands.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            playerHandler.addCommandToTreasure(event.getPlayer(), event.getMessage());
        }
    }
}
