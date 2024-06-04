package com.github.devcyntrix.deathchest.view.update;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class AdminNotificationView implements Consumer<String> {
    private final DeathChestService service;

    @Override
    public void accept(String version) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("deathchest.update"))
                .forEach(player -> {
                    player.sendMessage(this.service.getPrefix() + "§cA new version " + version + " is out.");
                    player.sendMessage(this.service.getPrefix() + "§cPlease update the plugin at " + service.getDescription().getWebsite());
                });
    }
}
