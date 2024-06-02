package com.github.devcyntrix.deathchest.api.event;

import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class DeathChestDestroyEvent extends Event {
    private static final @Getter HandlerList handlerList = new HandlerList();

    private final DeathChestModel deathChest;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
