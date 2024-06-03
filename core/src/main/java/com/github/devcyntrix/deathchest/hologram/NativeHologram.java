package com.github.devcyntrix.deathchest.hologram;

import com.github.devcyntrix.deathchest.api.controller.HologramController;
import com.github.devcyntrix.deathchest.api.hologram.Hologram;
import com.github.devcyntrix.deathchest.api.hologram.HologramTextLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class NativeHologram implements Hologram {

    @NotNull
    @Getter
    private final HologramController service;
    @NotNull
    private Location location;
    private final double lineHeight;

    private final List<HologramTextLine> list = new ArrayList<>();

    @NotNull
    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public void teleport(@NotNull Location location) {
        for (var line : list) {
            Location oldPos = line.getLocation().clone();
            Location diff = oldPos.subtract(getLocation());
            line.teleport(location.clone().add(diff));
        }
        this.location = location;
    }

    @Override
    public HologramTextLine appendLine(@NotNull String text) {
        list.forEach(line -> line.teleport(line.getLocation().add(0, lineHeight, 0)));
        var line = new NativeHologramTextLine(getLocation(), text);
        list.add(line);
        return line;
    }

    @Override
    public void delete() {
        this.list.forEach(HologramTextLine::remove);
        this.list.clear();
    }
}
