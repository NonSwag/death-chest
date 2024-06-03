package com.github.devcyntrix.deathchest.api.model;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface ThiefProtectionOptions {
    boolean enabled();

    String permission();

    String bypassPermission();

    @NotNull
    Duration expiration();

    Sound sound();

    float volume();

    float pitch();

    String[] message();

    void playSound(Player player, Location location);

    void notify(Player player);
}
