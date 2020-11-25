package me.baryonyx.treasurehunt.commands;

import me.baryonyx.treasurehunt.treasures.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BaseCommand implements TabExecutor {
    private PlayerHandler playerHandler;

    public BaseCommand(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
                playerHandler.togglePlayer(player);
                return true;
            }

            return false;
        }
        Bukkit.getLogger().info("Only a player can run the /th command");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("toggle");
        }

        return null;
    }
}
