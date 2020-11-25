package me.baryonyx.treasurehunt.treasures;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Treasure {
    public List<ItemStack> items;
    public List<String> commands;
    public Location location;

    public Treasure(List<ItemStack> items, List<String> commands, Location location) {
        this.items = items;
        this.commands = commands;
        this.location = location;
    }

    public Treasure(Location location) {
        this.location = location;
        this.items = new ArrayList<>();
        this.commands = new ArrayList<>();
    }
}
