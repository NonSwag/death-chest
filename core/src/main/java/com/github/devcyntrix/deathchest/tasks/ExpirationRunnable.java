package com.github.devcyntrix.deathchest.tasks;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.audit.AuditAction;
import com.github.devcyntrix.deathchest.api.audit.AuditManager;
import com.github.devcyntrix.deathchest.api.audit.info.DestroyReason;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.audit.CraftAuditItem;
import com.github.devcyntrix.deathchest.audit.CraftDestroyChestInfo;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ExpirationRunnable extends BukkitRunnable {
    private final DeathChestService service;
    private final AuditManager auditManager;
    private final DeathChestModel chest;

    @Override
    public void run() {
        // Stops the scheduler when the chest expired
        boolean dropItems = service.getDeathChestConfig().chestOptions().dropItemsAfterExpiration();
        try {
            if (dropItems) {
                chest.dropItems();
            }
        } catch (Exception e) {
            service.getLogger().log(Level.SEVERE, "Failed to drop items of the expired death chest", e);
        }
        if (auditManager != null) {
            var info = new CraftDestroyChestInfo(chest, DestroyReason.EXPIRATION, Map.of("item-drops", dropItems));
            auditManager.audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, info));
        }
        this.service.getDeathChestController().destroyChest(chest);
    }
}
