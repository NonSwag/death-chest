package com.github.devcyntrix.deathchest.support.placeholder;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class LastDeathChestLocationExpansion extends PlaceholderExpansion {
    public final DeathChestService service;

    private final @NotNull String locationFormat = getString("location_format", "<x> <y> <z> <world>");
    private final @NotNull String fallbackMessage = getString("fallback_message", "&cChest not found");

    @Override
    public @NotNull String getIdentifier() {
        return service.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", service.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return service.getDescription().getVersion();
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Collections.singletonList("last_location");
    }

    @Nullable
    public String onRequest(final OfflinePlayer player, @NotNull final String params) {
        if (player != null && player.isOnline()) {
            return onPlaceholderRequest(player.getPlayer(), params);
        }

        return onPlaceholderRequest(null, params);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null)
            return null;

        var deathChest = service.getLastChest(player);
        if (deathChest == null) {
            return ChatColor.translateAlternateColorCodes('&', this.fallbackMessage);
        }

        if (params.equalsIgnoreCase("last_location")) {
            String locationFormat = getString(deathChest);
            return ChatColor.translateAlternateColorCodes('&', locationFormat);
        }

        return null;
    }

    private @NotNull String getString(DeathChestModel deathChest) {
        Location location = deathChest.getLocation();
        String locationFormat = this.locationFormat
                .replace("<x>", String.valueOf(location.getBlockX()))
                .replace("<y>", String.valueOf(location.getBlockY()))
                .replace("<z>", String.valueOf(location.getBlockZ()));
        if (location.getWorld() != null) {
            var config = service.getDeathChestConfig();
            locationFormat = locationFormat.replace("<world>", config.worldAlias().getAlias(location.getWorld().getName()));
        }
        return locationFormat;
    }

}
