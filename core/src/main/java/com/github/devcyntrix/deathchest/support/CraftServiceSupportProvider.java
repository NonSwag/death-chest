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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class CraftServiceSupportProvider implements ServiceSupportProvider {
    protected final DeathChestService service;

    private final @Getter Map<String, Function<Plugin, BreakAnimationService>> animationServices = Map.of(
            "ProtocolLib", plugin -> ProtocolLibrary.getProtocolManager() != null ? new ProtocolLibBreakAnimation() : null
    );

    private final @Getter Map<String, Function<Plugin, ProtectionService>> protectionServices = Map.of(
            "WorldGuard", plugin -> new WorldGuardProtection(),
            "PlotSquared", plugin -> new PlotSquaredProtection(),
            "GriefPrevention", plugin -> new GriefPreventionProtection(),
            "RedProtect", plugin -> new RedProtectProtection(),
            "minePlots", plugin -> new MinePlotsProtection(),
            "protect", plugin -> new ProtectProtection()
    );

    @Override
    public @Nullable BreakAnimationService getAnimationService() {
        return getService(getAnimationServices(), service.getDeathChestConfig().preferredBlockBreakAnimationService());
    }

    @Override
    public ProtectionService getProtectionService() {
        List<ProtectionService> services = new ArrayList<>();
        services.add(new MinecraftProtection());
        for (Map.Entry<String, Function<Plugin, ProtectionService>> entry : getProtectionServices().entrySet()) {
            if (!Bukkit.getPluginManager().isPluginEnabled(entry.getKey()))
                continue;
            ProtectionService apply = entry.getValue().apply(service);
            if (apply == null)
                continue;
            service.debug(1, "Using " + entry.getKey() + " protection service");
            services.add(apply);
        }
        return new CombinedProtectionService(services.toArray(ProtectionService[]::new));
    }

    public <T> @Nullable T getService(@NotNull Map<String, Function<Plugin, T>> map, @Nullable String preferred) {
        return Optional.ofNullable(preferred)
                .filter(this::isServiceUsable)
                .map(map::get)
                .map(function -> function.apply(service))
                .orElseGet(() -> map.entrySet().stream()
                        .filter(entry -> isServiceUsable(entry.getKey()))
                        .map(entry -> entry.getValue().apply(this.service))
                        .findAny()
                        .orElse(null));
    }

    private boolean isServiceUsable(@NotNull String service) {
        if (Bukkit.getPluginManager().isPluginEnabled(service)) return true;
        this.service.getLogger().warning("Cannot use service \"%s\"".formatted(service));
        return false;
    }
}
