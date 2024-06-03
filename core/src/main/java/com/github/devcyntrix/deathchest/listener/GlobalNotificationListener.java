package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import com.github.devcyntrix.deathchest.api.model.GlobalNotificationOptions;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GlobalNotificationListener implements Listener {

    private final DeathChestService service;

    @EventHandler
    public void onSpawn(DeathChestSpawnEvent event) {
        var deathChestConfig = service.getDeathChestConfig();

        GlobalNotificationOptions globalNotificationOptions = deathChestConfig.globalNotificationOptions();
        if (globalNotificationOptions.enabled() && globalNotificationOptions.message() != null) {
            globalNotificationOptions.showNotification(event.getDeathChest(), service.getPlaceholderController());
        }
    }

}
