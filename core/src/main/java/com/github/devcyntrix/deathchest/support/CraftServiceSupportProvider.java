package com.github.devcyntrix.deathchest.support;

import com.comphenix.protocol.ProtocolLibrary;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import com.github.devcyntrix.deathchest.api.compatibility.ServiceSupportProvider;
import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import com.github.devcyntrix.deathchest.support.animation.ProtocolLibBreakAnimation;
import com.github.devcyntrix.deathchest.support.protection.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class CraftServiceSupportProvider implements ServiceSupportProvider {
    private final @Getter Map<String, Function<Plugin, ProtectionService>> protectionServices = Map.of(
            "WorldGuard", plugin -> new WorldGuardProtection(),
            "PlotSquared", plugin -> new PlotSquaredProtection(),
            "GriefPrevention", plugin -> new GriefPreventionProtection(),
            "RedProtect", plugin -> new RedProtectProtection(),
            "minePlots", plugin -> new MinePlotsProtection(),
            "Protect", plugin -> new ProtectProtection()
    );
    protected final DeathChestService service;

    private final @Getter Map<String, Function<Plugin, BreakAnimationService>> animationServices = Map.of(
            "ProtocolLib", plugin -> ProtocolLibrary.getProtocolManager() != null ? new ProtocolLibBreakAnimation() : null
    );
    private @Nullable CombinedProtectionService protectionService;

    @Override
    public ProtectionService getProtectionService() {
        if (protectionService != null) return protectionService;
        var services = new ArrayList<ProtectionService>();
        getProtectionServices().entrySet().stream()
                .filter(entry -> isServiceUsable(entry.getKey()))
                .map(this::mapServiceEntry)
                .forEach(services::add);
        services.add(new MinecraftProtection());
        var array = services.toArray(ProtectionService[]::new);
        return protectionService = new CombinedProtectionService(array);
    }

    public <T> @Nullable T getUsableService(@NotNull Map<String, Function<Plugin, T>> map) {
        return map.entrySet().stream()
                .filter(entry -> isServiceUsable(entry.getKey()))
                .map(this::mapServiceEntry)
                .findAny()
                .orElse(null);
    }

    private <T> T mapServiceEntry(Map.Entry<String, Function<Plugin, T>> entry) {
        service.debug(1, "Using \"" + entry.getKey() + "\" protection service");
        return entry.getValue().apply(service);
    }

    private boolean isServiceUsable(@NotNull String service) {
        if (Bukkit.getPluginManager().isPluginEnabled(service)) return true;
        this.service.getLogger().warning("Cannot use service \"%s\"".formatted(service));
        return false;
    }
}
