package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.BreakAnimationOptions;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.tasks.BreakAnimationRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@RequiredArgsConstructor
public class BreakAnimationView implements ChestView {
    private final @NotNull DeathChestService service;
    private final @NotNull BreakAnimationOptions options;

    @Override
    public void onCreate(DeathChestModel model) {
        var service = this.service.getServiceSupportProvider().getAnimationService();
        if (service == null) return;

        this.service.debug(0, "Starting block break animation");
        var bukkitTask = new BreakAnimationRunnable(this.service, model, service, options)
                .runTaskTimerAsynchronously(this.service, 20, 20);
        model.getTasks().add(bukkitTask::cancel);
    }

    @Override
    public void onDestroy(DeathChestModel model) {
        var service = this.service.getServiceSupportProvider().getAnimationService();
        if (service == null) return;

        try {
            // Resets the breaking animation if the service is available
            if (!model.isExpiring()) return;
            if (model.getWorld() == null) return;
            var players = model.getWorld().getNearbyEntities(model.getLocation(), 20, 20, 20,
                    entity -> entity.getType() == EntityType.PLAYER).stream().map(entity -> (Player) entity);
            service.spawnBlockBreakAnimation(model.getLocation(), -1, players);
        } catch (Exception e) {
            this.service.getLogger().log(Level.WARNING, "Failed to reset the block animation of all players in the area", e);
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
