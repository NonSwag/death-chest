package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.DeathChestPlugin;
import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import com.github.devcyntrix.deathchest.api.model.BreakAnimationOptions;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.tasks.BreakAnimationRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

@RequiredArgsConstructor
public class BreakAnimationView implements ChestView {
    private final @NotNull DeathChestPlugin plugin;
    private final @Nullable BreakAnimationService service;
    private final @NotNull BreakAnimationOptions options;

    @Override
    public void onCreate(DeathChestModel model) {
        if (service == null)
            return;

        plugin.debug(0, "Starting block break animation");
        BukkitTask bukkitTask = new BreakAnimationRunnable(plugin, model, service, options).runTaskTimerAsynchronously(plugin, 20, 20);
        model.getTasks().add(bukkitTask::cancel);
    }

    @Override
    public void onDestroy(DeathChestModel model) {
        if (service == null) return;

        try {
            // Resets the breaking animation if the service is available
            if (!model.isExpiring()) return;
            if (model.getWorld() == null) return;
            var players = model.getWorld().getNearbyEntities(model.getLocation(), 20, 20, 20,
                    entity -> entity.getType() == EntityType.PLAYER).stream().map(entity -> (Player) entity);
            service.spawnBlockBreakAnimation(model.getLocation().toVector(), -1, players);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to reset the block animation of all players in the area", e);
        }
    }

    @Override
    public void onLoad(DeathChestModel model) {
        onCreate(model);
    }

    @Override
    public void onUnload(DeathChestModel model) {
        onDestroy(model);
    }
}
