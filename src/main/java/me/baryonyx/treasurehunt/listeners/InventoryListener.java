package me.baryonyx.treasurehunt.listeners;

import me.baryonyx.treasurehunt.TreasureHunt;
import me.baryonyx.treasurehunt.treasures.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InventoryListener implements Listener {
    private PlayerHandler playerHandler;
    private TreasureHunt plugin;

    public InventoryListener(PlayerHandler playerHandler, TreasureHunt plugin) {
        this.playerHandler = playerHandler;
        this.plugin = plugin;
    }

    @EventHandler
    public void CloseInventoryEvent(@NotNull InventoryCloseEvent event) {
        if (playerHandler.hasInventory(event.getInventory()) && !playerHandler.playersAddingCommands.contains(event.getPlayer().getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().openInventory(playerHandler.playersInventory(event.getPlayer().getUniqueId()));
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void TreasureListener(InventoryClickEvent event) {
        if (playerHandler.hasInventory(event.getClickedInventory())) {
            if (event.getSlot() > 8) {
                event.setCancelled(true);

                if (event.getSlot() < 17 && event.getClickedInventory().getItem(event.getSlot()) != null) {
                    playerHandler.removeCommandFromTreasure(event.getWhoClicked().getUniqueId(), event.getSlot(), event.getClickedInventory());
                }

                else if (event.getSlot() == 17) {
                    playerHandler.playersAddingCommands.add(event.getWhoClicked().getUniqueId());
                    playerHandler.promptForCommand((Player) event.getWhoClicked());
                }

                else if (event.getSlot() == 22) {
                    playerHandler.saveTreasure(event.getWhoClicked().getUniqueId());
                    playerHandler.removePlayerInventory(event.getWhoClicked().getUniqueId());
                    event.getWhoClicked().closeInventory();
                }
            }
            else if (isPlaceAction(event.getAction())) {
                playerHandler.addItemToTreasure(event.getWhoClicked().getUniqueId(), event.getCurrentItem());
            }
            else if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                if (event.getCursor() != null) {
                    playerHandler.addItemToTreasure(event.getWhoClicked().getUniqueId(), event.getCursor());
                }

                playerHandler.removeItemFromTreasure(event.getWhoClicked().getUniqueId(), event.getSlot());
            }
            else {
                playerHandler.removeItemFromTreasure(event.getWhoClicked().getUniqueId(), event.getSlot());
            }
        }
        else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && playerHandler.hasInventory(event.getInventory())) {
            if (event.getView().getTopInventory().firstEmpty() < 9) {
                    playerHandler.addItemToTreasure(event.getWhoClicked().getUniqueId(), event.getCurrentItem());
                    return;
            }

            event.setCancelled(true);
        }
    }

    private boolean isPlaceAction(InventoryAction action) {
        return action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME;
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event) {
        if (playerHandler.hasInventory(event.getInventory())) {
            event.setCancelled(true);
        }
    }
}
