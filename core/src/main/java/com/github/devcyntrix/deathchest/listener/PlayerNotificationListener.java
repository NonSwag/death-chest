package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerNotificationListener implements Listener {
    private final DeathChestService service;

    @EventHandler
    public void onSpawn(DeathChestSpawnEvent event) {
        var deathChestConfig = service.getDeathChestConfig();

        var deathChest = event.getDeathChest();
        var controller = service.getPlaceholderController();

        var audience = service.getAudience(event.getPlayer());

        // Player notification
        var playerNotificationOptions = deathChestConfig.playerNotificationOptions();
        if (!playerNotificationOptions.enabled() || playerNotificationOptions.message() == null) return;
        playerNotificationOptions.showNotification(audience, deathChest, controller);
    }
}
