package com.github.devcyntrix.deathchest.support.protection;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import net.thenextlvl.protect.flag.Flag;
import net.thenextlvl.protect.flag.FlagRegistry;
import org.bukkit.Bukkit;

import java.util.Objects;

public final class ProtectDeathChestFlag {
    private static final FlagRegistry flagRegistry = Objects.requireNonNull(
            Bukkit.getServicesManager().load(FlagRegistry.class)
    );
    public static Flag<Boolean> FLAG;

    public static void register(DeathChestService service) {
        FLAG = flagRegistry.register(service, boolean.class, "spawn_death_chest", false);
        service.debug(0, "Registered Protect flag \"%s\" (default: %s).".formatted(FLAG.key(), FLAG.defaultValue()));
    }
}
