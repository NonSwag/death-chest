package com.github.devcyntrix.deathchest.config;

import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.InventoryOptions;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record CraftInventoryOptions(
        @SerializedName("title") @NotNull String title,
        @SerializedName("size") @NotNull InventorySize size) implements InventoryOptions {

    public static final String DEFAULT_TITLE = "Death Chest";

    public static final InventorySize DEFAULT_SIZE = InventorySize.FLEXIBLE;

    @Contract("null -> new")
    public static @NotNull CraftInventoryOptions load(@Nullable ConfigurationSection section) {
        if (section == null)
            section = new MemoryConfiguration();

        String title = ChatColor.translateAlternateColorCodes('&', section.getString("title", DEFAULT_TITLE));
        String sizeString = section.getString("size", DEFAULT_SIZE.name().toLowerCase());
        InventorySize size = InventorySize.valueOf(sizeString.toUpperCase());

        return new CraftInventoryOptions(title, size);
    }

    public Inventory createInventory(DeathChestModel model, Function<String, String> placeholder, ItemStack... stacks) {
        String title = placeholder.apply(title());
        var inventory = Bukkit.createInventory(model, size().getSize(stacks.length), title);
        inventory.setContents(stacks);
        return inventory;
    }
}
