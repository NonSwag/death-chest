package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestDestroyEvent;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Comparator;

@RequiredArgsConstructor
public class LastDeathChestListener implements Listener {
    private final DeathChestService service;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        var first = this.service.getChests()
                .filter(deathChest -> deathChest.getOwner() != null)
                .filter(deathChest -> player.equals(deathChest.getOwner()))
                .max(Comparator.comparingLong(DeathChestModel::getCreatedAt));
        first.ifPresent(model -> service.getLastDeathChests().put(player, model));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(DeathChestSpawnEvent event) {
        Player player = event.getPlayer();
        DeathChestModel deathChest = event.getDeathChest();
        DeathChestModel oldChest = service.getLastDeathChests().get(player);
        if (oldChest == null) {
            service.getLastDeathChests().put(player, deathChest);
            return;
        }
        if (deathChest.getCreatedAt() > oldChest.getCreatedAt()) {
            service.getLastDeathChests().put(player, deathChest);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDestroy(DeathChestDestroyEvent event) {
        if (event.getDeathChest().getOwner() == null) return;
        var player = event.getDeathChest().getOwner().getPlayer();
        if (player == null) return;
        var first = service.getChests()
                .filter(deathChest -> deathChest.getOwner() != null)
                .filter(deathChest -> event.getDeathChest().getOwner().equals(deathChest.getOwner()))
                .max(Comparator.comparingLong(DeathChestModel::getCreatedAt));
        first.ifPresentOrElse(model -> service.getLastDeathChests().put(player, model),
                () -> service.getLastDeathChests().remove(player));
    }
}
