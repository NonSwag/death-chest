package com.github.devcyntrix.deathchest.api.model;

import org.bukkit.World;

import java.util.Set;
import java.util.function.Predicate;

public interface WorldFilterConfig extends Predicate<World> {
    FilterType filterType();

    Set<String> worlds();
}
