package com.github.devcyntrix.deathchest.api.storage;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public interface DeathChestStorage extends Closeable {

    ConfigurationSection getDefaultOptions();

    void init(DeathChestService plugin, ConfigurationSection section) throws IOException;

    void put(DeathChestModel chest);

    void update(Collection<DeathChestModel> chests);

    Set<DeathChestModel> getChests();

    Set<DeathChestModel> getChests(@NotNull World world);

    void remove(DeathChestModel chest);

}
