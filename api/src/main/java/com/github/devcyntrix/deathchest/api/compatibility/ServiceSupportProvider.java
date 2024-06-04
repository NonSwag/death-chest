package com.github.devcyntrix.deathchest.api.compatibility;

import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.function.Function;

public interface ServiceSupportProvider {
    Map<String, Function<Plugin, BreakAnimationService>> getAnimationServices();

    Map<String, Function<Plugin, ProtectionService>> getProtectionServices();

    BreakAnimationService getAnimationService();

    ProtectionService getProtectionService();
}
