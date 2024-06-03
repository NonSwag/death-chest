package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.ParticleOptions;
import com.github.devcyntrix.deathchest.tasks.ParticleRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class ParticleView implements ChestView {
    private final DeathChestService service;
    private final ParticleOptions options;

    @Override
    public void onCreate(DeathChestModel model) {
        if (service.isTest())
            return;
        World world = model.getWorld();
        if (world == null)
            return;

        Particle.DustOptions orangeDustOptions = new Particle.DustOptions(Color.ORANGE, 0.75f);
        Particle.DustOptions aquaDustOptions = new Particle.DustOptions(Color.AQUA, 0.75f);

        service.debug(0, "Starting particle runner...");
        BukkitTask bukkitTask = new ParticleRunnable(model.getLocation(), options.count(), options.radius(), particleLocation -> {
            // Orange dust
            Location orangeDust = particleLocation.clone().add(0.5, 0.5, 0.5); // Center the particle location
            Bukkit.getScheduler().runTask(service, () -> world.spawnParticle(Particle.REDSTONE, orangeDust, 1, orangeDustOptions));

            // Aqua dust
            Location aquaDust = orangeDust.clone().subtract(0, 0.1, 0);
            Bukkit.getScheduler().runTask(service, () -> world.spawnParticle(Particle.REDSTONE, aquaDust, 1, aquaDustOptions));

        }).runTaskTimerAsynchronously(this.service, 0, (long) (20 / options.speed()));
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
