package com.github.devcyntrix.deathchest.api.model;

import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface InventoryOptions {
    @NotNull
    String title();

    @NotNull
    InventorySize size();

    Inventory createInventory(DeathChestModel model, Function<String, String> placeholder, ItemStack... stacks);

    @RequiredArgsConstructor
    enum InventorySize {
        CONSTANT(integer -> 9 * 5),
        FLEXIBLE(integer -> {
            double rel = integer / 9.0;
            int round = (int) Math.ceil(rel);
            return 9 * round;
        });

        private final Function<Integer, Integer> sizeFunction;

        public int getSize(int itemCount) {
            return sizeFunction.apply(itemCount);
        }
    }
}
