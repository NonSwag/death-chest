package com.github.devcyntrix.deathchest.view.chest;

import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CloseInventoryView implements ChestView {
    private final DeathChestService service;

    @Override
    public void onCreate(DeathChestModel model) {
    }

    @Override
    public void onDestroy(DeathChestModel model) {
        try {
            var humanEntities = new ArrayList<>(model.getInventory().getViewers());
            humanEntities.forEach(HumanEntity::closeInventory);
        } catch (Exception e) {
            service.getLogger().log(Level.SEVERE, "Failed to close inventories of viewers.", e);
        }
    }

    @Override
    public void onLoad(DeathChestModel model) {
        onCreate(model);
    }

    @Override
    public void onUnload(DeathChestModel model) {
        onDestroy(model);
    }
}
