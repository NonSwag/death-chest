package com.github.devcyntrix.deathchest.view.update;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.controller.UpdateController;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class AdminJoinNotificationView implements Listener {
    private final DeathChestService service;
    private final UpdateController controller;

    @EventHandler
    public void onNotifyUpdate(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (controller.getNewestVersion() == null)
            return;
        if (!player.hasPermission("deathchest.update"))
            return;
        player.sendMessage(service.getPrefix() + "§cA new version " + controller.getNewestVersion() + " is out.");
        player.sendMessage(service.getPrefix() + "§cPlease update the plugin at " + service.getDescription().getWebsite());
    }
}
