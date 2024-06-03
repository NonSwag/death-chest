package com.github.devcyntrix.deathchest.api.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
public class InventoryChangeSlotItemEvent extends Event implements Cancellable {
    private static final @Getter HandlerList handlerList = new HandlerList();
    private final HumanEntity entity;
    private final Inventory inventory;
    private final int slot;
    private ItemStack from;
    private ItemStack to;

    private boolean cancelled;

    public InventoryChangeSlotItemEvent(HumanEntity entity, Inventory inventory, int slot, ItemStack from, ItemStack to) {
        this.entity = entity;
        this.inventory = inventory;
        this.slot = slot;
        this.from = from;
        this.to = to;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }
}
