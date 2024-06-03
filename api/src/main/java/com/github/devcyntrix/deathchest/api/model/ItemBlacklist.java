package com.github.devcyntrix.deathchest.api.model;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Set;

public interface ItemBlacklist extends InventoryHolder {

    Set<ItemStack> getList();

    int getPage();

    void setPage(int page);

    boolean isValidItem(ItemStack stack);

    /**
     * Compares the lower item with the higher item
     */
    boolean compareItem(ItemStack higher, ItemStack lower);

    void updateInventory();

    void save() throws IOException;
}
