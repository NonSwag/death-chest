package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import com.github.devcyntrix.deathchest.api.model.PlayerNotificationOptions;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerNotificationListener implements Listener {

    private final DeathChestService service;

    @EventHandler
    public void onSpawn(DeathChestSpawnEvent event) {
        var deathChestConfig = service.getDeathChestConfig();

        Player player = event.getPlayer();
        Audience audience = service.getAudiences().player(player);
        var deathChest = event.getDeathChest();
        var controller = service.getPlaceholderController();

        // Player notification
        PlayerNotificationOptions playerNotificationOptions = deathChestConfig.playerNotificationOptions();
        if (playerNotificationOptions.enabled() && playerNotificationOptions.message() != null) {
            playerNotificationOptions.showNotification(audience, deathChest, controller);
        }
    }
}
