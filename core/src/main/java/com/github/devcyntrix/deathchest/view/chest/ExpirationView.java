package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.tasks.ExpirationRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class ExpirationView implements ChestView {
    private final DeathChestService service;

    @Override
    public void onCreate(DeathChestModel model) {
        if (!model.isExpiring())
            return;
        long untilDeletion = Math.max(0, model.getExpireAt() - System.currentTimeMillis());

        ExpirationRunnable runnable = new ExpirationRunnable(service, service.getAuditManager(), model);
        BukkitTask bukkitTask = runnable.runTaskLater(service, (untilDeletion / 1000) * 20);
        model.getTasks().add(bukkitTask::cancel);
    }

    @Override
    public void onDestroy(DeathChestModel model) {
    }

    @Override
    public void onLoad(DeathChestModel model) {
        onCreate(model);
    }

    @Override
    public void onUnload(DeathChestModel model) {
    }
}
