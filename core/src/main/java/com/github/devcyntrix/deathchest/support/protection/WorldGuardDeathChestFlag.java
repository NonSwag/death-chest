package com.github.devcyntrix.deathchest.support.protection;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;

public final class WorldGuardDeathChestFlag {
    public static StateFlag FLAG;

    public static void register(DeathChestService service) {
        FLAG = new StateFlag("spawn-death-chest", false);
        service.debug(0, "Registered WorldGuard flag \"%s\" (default: %s).".formatted(FLAG.getName(), FLAG.getDefault()));
        WorldGuard.getInstance().getFlagRegistry().register(FLAG);
    }
}
