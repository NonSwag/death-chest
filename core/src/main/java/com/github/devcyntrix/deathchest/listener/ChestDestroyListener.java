package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.audit.AuditAction;
import com.github.devcyntrix.deathchest.api.audit.info.DestroyReason;
import com.github.devcyntrix.deathchest.api.model.ThiefProtectionOptions;
import com.github.devcyntrix.deathchest.audit.CraftAuditItem;
import com.github.devcyntrix.deathchest.audit.CraftDestroyChestInfo;
import com.github.devcyntrix.deathchest.model.CraftDeathChestModel;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChestDestroyListener implements Listener {

    private final DeathChestCorePlugin plugin;

    public ChestDestroyListener(DeathChestCorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Closes the chest if the player was the last viewer and
     * removes the chest if the player closes an empty inventory,
     *
     * @param event the event from the Bukkit API
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CraftDeathChestModel model))
            return;

        HumanEntity human = event.getPlayer();
        BlockState state = model.getLocation().getBlock().getState();

        if (state instanceof Lidded lidded && human.getGameMode() != GameMode.SPECTATOR) {
            try {
                lidded.close();
            } catch (Exception ignored) {
            }
        }

        if (!inventory.isEmpty())
            return;

        DestroyReason reason = DestroyReason.PLAYER;
        if (!human.equals(model.getOwner()))
            reason = DestroyReason.THIEF;

        var info = new CraftDestroyChestInfo(model, reason, Map.of("player", human));
        this.plugin.getAuditManager().audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, info));

        // Remove the chest at the next server tick to prevent a stack overflow because it tries to close all viewer inventories.
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getDeathChestController().destroyChest(model);
            }
        }.runTask(plugin);

    }

    /**
     * Drops the item from the death chest when the player breaks the chest.
     *
     * @param event the event from the Bukkit API
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        var model = this.plugin.getDeathChestController().getChest(block.getLocation());
        if (model == null)
            return;
        Player player = event.getPlayer();

        event.setCancelled(true);

        if (!plugin.getDeathChestController().isAccessibleBy(model, player)) {
            ThiefProtectionOptions protectionOptions = plugin.getDeathChestConfig().chestOptions().thiefProtectionOptions();
            protectionOptions.playSound(player, block.getLocation());
            protectionOptions.notify(player);
            return;
        }

        Arrays.stream(model.getInventory().getContents())
                .filter(Objects::nonNull)
                .forEach(itemStack -> model.getWorld().dropItemNaturally(model.getLocation(), itemStack));

        var info = new CraftDestroyChestInfo(model, DestroyReason.BREAK, Map.of("player", player));
        this.plugin.getAuditManager().audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, info));
        model.getInventory().clear();

        plugin.getDeathChestController().destroyChest(model);
    }

    /**
     * Drops the items when the chest was destroyed by a block explosion
     *
     * @param event the event from the Bukkit API
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent event) {
        Iterator<Block> iterator = event.blockList().iterator();

        while (iterator.hasNext()) {
            Block next = iterator.next();

            var model = this.plugin.getDeathChestController().getChest(next.getLocation());
            if (model == null)
                continue;
            if (plugin.getDeathChestConfig().chestOptions().blastProtection()) {
                iterator.remove();
                continue;
            }

            for (ItemStack content : model.getInventory().getContents()) {
                if (content == null) continue;
                model.getWorld().dropItemNaturally(model.getLocation(), content);
            }
            model.getInventory().clear();

            Block block = event.getBlock();
            var info = new CraftDestroyChestInfo(model, DestroyReason.BLOCK_EXPLOSION, Map.of("block", block));
            this.plugin.getAuditManager().audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, info));
            this.plugin.getDeathChestController().destroyChest(model);
        }

    }

    /**
     * Drops the items when the chest was destroyed by an entity explosion
     *
     * @param event the event from the Bukkit API
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockExplode(EntityExplodeEvent event) {

        Iterator<Block> iterator = event.blockList().iterator();

        while (iterator.hasNext()) {
            Block next = iterator.next();

            var model = this.plugin.getDeathChestController().getChest(next.getLocation());
            if (model == null)
                continue;
            if (this.plugin.getDeathChestConfig().chestOptions().blastProtection()) {
                iterator.remove();
                continue;
            }

            for (ItemStack content : model.getInventory().getContents()) {
                if (content == null) continue;
                model.getWorld().dropItemNaturally(model.getLocation(), content);
            }
            model.getInventory().clear();

            Entity entity = event.getEntity();
            var info = new CraftDestroyChestInfo(model, DestroyReason.ENTITY_EXPLOSION, Map.of("entity", entity));
            this.plugin.getAuditManager().audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, info));
            this.plugin.getDeathChestController().destroyChest(model);
        }
    }
}
