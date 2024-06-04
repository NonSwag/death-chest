package com.github.devcyntrix.deathchest.model;

import com.github.devcyntrix.deathchest.api.model.FilterType;
import com.github.devcyntrix.deathchest.api.model.WorldFilterConfig;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CraftWorldFilterConfig(FilterType filterType, Set<String> worlds) implements WorldFilterConfig {

    public static final FilterType DEFAULT_TYPE = FilterType.BLACKLIST;

    @Contract("null -> new")
    public static @NotNull CraftWorldFilterConfig load(@Nullable ConfigurationSection section) {
        if (section == null)
            return new CraftWorldFilterConfig(DEFAULT_TYPE, Collections.emptySet());

        String filterString = section.getString("filter");
        if (filterString != null) {
            try {
                FilterType filterType = FilterType.valueOf(filterString.toUpperCase());
                List<String> worlds = section.getStringList("worlds");
                return new CraftWorldFilterConfig(filterType, new HashSet<>(worlds));
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown world filter in DeathChest/config.yml");
                e.printStackTrace();
            }
        }
        return new CraftWorldFilterConfig(DEFAULT_TYPE, Collections.emptySet());
    }


    @Override
    public boolean test(World world) {
        return (filterType() == FilterType.WHITELIST && worlds().contains(world.getName())) || (filterType() == FilterType.BLACKLIST && !worlds().contains(world.getName()));
    }
}
