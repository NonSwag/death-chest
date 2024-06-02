package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.CraftDeathChestModel;
import com.github.devcyntrix.deathchest.api.ChestView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CloseInventoryView implements ChestView {

    private final JavaPlugin plugin;

    public CloseInventoryView(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCreate(CraftDeathChestModel model) {

    }

    @Override
    public void onDestroy(CraftDeathChestModel model) {
        if (model.getInventory() == null)
            return;

        try {
            List<HumanEntity> humanEntities = new ArrayList<>(model.getInventory().getViewers()); // Copies the list to avoid a concurrent modification exception
            humanEntities.forEach(HumanEntity::closeInventory);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close inventories of viewers.", e);
        }
    }

    @Override
    public void onLoad(CraftDeathChestModel model) {
        onCreate(model);
    }

    @Override
    public void onUnload(CraftDeathChestModel model) {
        onDestroy(model);
    }
}
