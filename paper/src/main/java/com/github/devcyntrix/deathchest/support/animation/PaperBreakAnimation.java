package com.github.devcyntrix.deathchest.support.animation;

import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.stream.Stream;

public class PaperBreakAnimation implements BreakAnimationService {

    @Override
    public void spawnBlockBreakAnimation(@NotNull Location location, @Range(from = -1, to = 9) int state, @NotNull Stream<? extends Player> players) {
        if (state == -1) {
            state = 0;
        }

        @Range(from = -1, to = 9) int finalState = state;

        var sourceId = location.hashCode();
        players.forEach(player -> {
            Location loc = location.toLocation(player.getWorld());
            player.sendBlockDamage(loc, finalState / 9f, sourceId);
        });
    }
}
