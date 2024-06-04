package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GlobalNotificationListener implements Listener {
    private final DeathChestService service;

    @EventHandler
    public void onSpawn(DeathChestSpawnEvent event) {
        var deathChestConfig = service.getDeathChestConfig();
        var options = deathChestConfig.globalNotificationOptions();
        if (!options.enabled() || options.message() == null) return;
        options.showNotification(event.getDeathChest(), service.getPlaceholderController());
    }
}
