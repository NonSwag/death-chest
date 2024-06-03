package com.github.devcyntrix.deathchest.support.placeholder;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.compatibility.Compatibility;
import org.bukkit.Server;

public class PlaceholderAPICompatibility extends Compatibility {
    private LastDeathChestLocationExpansion expansion;

    @Override
    public boolean isValid(Server server) {
        return server.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    protected void enable(DeathChestService plugin) {
        this.expansion = new LastDeathChestLocationExpansion(plugin);
        this.expansion.register();
    }

    @Override
    protected void disable(DeathChestService plugin) {
        if (this.expansion != null && this.expansion.unregister())
            plugin.getLogger().info("Placeholder API expansion has successfully unregistered");
    }
}
