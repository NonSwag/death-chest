package com.github.devcyntrix.deathchest.support.animation;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class ProtocolLibBreakAnimation implements BreakAnimationService {
    @Override
    public void spawnBlockBreakAnimation(@NotNull Location location, @Range(from = -1, to = 9) int state, @NotNull Stream<? extends Player> players) {

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);

        packet.getIntegers().write(0, location.hashCode());
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        packet.getIntegers().write(1, state);

        players.forEach(player -> {
            try {
                manager.sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

}
