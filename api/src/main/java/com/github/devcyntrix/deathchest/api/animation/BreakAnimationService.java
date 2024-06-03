package com.github.devcyntrix.deathchest.api.animation;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.stream.Stream;

public interface BreakAnimationService {
    void spawnBlockBreakAnimation(@NotNull Vector location, @Range(from = -1, to = 9) int state, @NotNull Stream<? extends Player> players);
}
