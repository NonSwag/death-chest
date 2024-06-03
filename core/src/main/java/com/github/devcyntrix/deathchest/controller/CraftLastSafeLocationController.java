package com.github.devcyntrix.deathchest.controller;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.controller.LastSafeLocationController;
import com.github.devcyntrix.deathchest.util.LastLocationMetadata;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import javax.annotation.Nullable;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class CraftLastSafeLocationController implements LastSafeLocationController {

    private static final String lastSafePosition = "last-safe-position";

    private final DeathChestService service;

    public void updatePosition(Player player) {
        Location location = player.getLocation().clone().subtract(0, 0.2, 0);
        Block block = location.getBlock();
        if (block.isEmpty()) // Check if the player is in air
            return;

        Location blockLoc = player.getLocation().getBlock().getLocation();
        if (!service.canPlaceChestAt(blockLoc))
            return;

        List<MetadataValue> metadata = player.getMetadata(lastSafePosition);
        if (metadata.isEmpty()) {
            player.setMetadata(lastSafePosition, new LastLocationMetadata(service, blockLoc.clone()));
        } else {
            MetadataValue metadataValue = metadata.getFirst();
            if (!(metadataValue instanceof LastLocationMetadata meta))
                return;
            if (System.currentTimeMillis() - meta.getUpdatedAt() < 500)
                return;

            meta.setLocation(blockLoc.clone());
        }
    }

    public @Nullable Location getPosition(Player player) {
        List<MetadataValue> metadata = player.getMetadata(lastSafePosition);
        if (metadata.isEmpty())
            return null;

        MetadataValue metadataValue = metadata.getFirst();
        if (!(metadataValue instanceof LastLocationMetadata meta))
            return null;
        return meta.value();
    }
}
