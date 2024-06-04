package com.github.devcyntrix.deathchest.listener;

import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.event.DeathChestSpawnEvent;
import com.github.devcyntrix.deathchest.api.event.PreDeathChestSpawnEvent;
import com.github.devcyntrix.deathchest.api.model.ChangeDeathMessageOptions;
import com.github.devcyntrix.deathchest.api.model.NoExpirationPermission;
import com.github.devcyntrix.deathchest.api.model.ThiefProtectionOptions;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is only for handling if a player dies. Here is the spawning of a death chest written and some checks if a
 * chest should spawn. Additionally, here is the death message, custom info and broadcast message implemented.
 */
@RequiredArgsConstructor
public class SpawnChestListener implements Listener {
    private final DeathChestService service;

    private final Set<Player> players = Collections.newSetFromMap(new WeakHashMap<>());

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        players.remove(event.getPlayer());
    }

    /**
     * Creates the death chest if the player dies.
     * It only spawns a chest if the player has the permission to build in the region.
     * It puts the drops into the chest and clears the drops.
     *
     * @param event the event from the bukkit api
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {

        service.debug(0, "Spawning death chest...");

        Player player = event.getEntity();
        service.debug(1, "Checking multiple deaths at once...");
        if (players.contains(player)) {
            event.getDrops().clear();
            return;
        }
        players.add(player);

        World world = player.getWorld();
        var config = service.getDeathChestConfig();

        service.debug(1, "Checking keep inventory...");
        if (event.getKeepInventory())
            return;

        service.debug(1, "Removing air and blacklisted items...");
        boolean removed = event.getDrops()
                .removeIf(itemStack -> itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0 || !service.getBlacklist().isValidItem(itemStack)); // Prevent spawning an empty chest
        if (removed) {
            service.debug(2, "Inventory has been updated.");
        }
        ItemStack[] items = event.getDrops().stream()
                .filter(Objects::nonNull)
                .filter(stack -> service.getBlacklist().isValidItem(stack))
                .toArray(ItemStack[]::new);

        if (items.length == 0) {
            service.debug(1, "Clearing drops because the inventory was empty after removing blacklisted items");
            event.getDrops().clear();
            return;
        }

        service.debug(1, "Checking world filter...");
        if (!config.worldFilterConfig().test(player.getWorld()))
            return;

        service.debug(1, "Getting expiration time...");
        Duration expiration = config.chestOptions().expiration();
        if (expiration == null)
            expiration = Duration.ofSeconds(-1);

        service.debug(1, "Checking no expiration permission...");
        NoExpirationPermission permission = config.chestOptions().noExpirationPermission();
        boolean expires = !permission.enabled() || !player.hasPermission(permission.permission());
        long createdAt = System.currentTimeMillis();
        long expireAt = !expiration.isNegative() && !expiration.isZero() && expires ? createdAt + expiration.toMillis() : -1;
        if (expireAt <= 0) {
            service.debug(1, "The chest will never expire");
        } else {
            service.debug(1, "The chest will expire at " + new Date(expireAt));
        }


//        Location deathLocation = new Location(
//                player.getWorld(),
//                location.getX(),
//                Math.round(location.getY()),
//                location.getZ()
//        );


//        plugin.debug(1, "Checking world height limitations...");
//        int highestBlockYAt = !plugin.isTest() ? world.getHighestBlockYAt(deathLocation, HeightMap.WORLD_SURFACE) : 60; // This is for Mock Bukkit
//        // Check Minecraft limitation of block positions
//        if (deathLocation.getBlockY() <= world.getMinHeight()) { // Min build height
//            deathLocation.setY(Math.max(0, highestBlockYAt));
//        }
//        if (deathLocation.getBlockY() >= world.getMaxHeight()) { // Max build height
//            deathLocation.setY(highestBlockYAt);
//            if (deathLocation.getBlockY() >= world.getMaxHeight()) {
//                deathLocation.setY(world.getSeaLevel());
//            }
//        }
//
//        Location loc = deathLocation.getBlock().getLocation();
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//
//        long start = System.currentTimeMillis();
//        while (!plugin.canPlaceChestAt(loc)) {
//            if (System.currentTimeMillis() - start > 1000) {
//                loc.setY(world.getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
//                plugin.debug(1, "Finding a valid location took longer than 1 second.");
//                break;
//            }
//
//            int x = random.nextInt(10) - 5;
//            int z = random.nextInt(10) - 5;
//            loc.add(x, 0, z);
//        }


        var controller = service.getLastSafeLocationController();
        controller.updatePosition(player);
        Location lastSafePos = controller.getPosition(player);

        if (lastSafePos == null) {
            lastSafePos = player.getLocation().getBlock().getLocation().clone();
        } else if (player.getLocation().distanceSquared(lastSafePos) >= 20 * 20) {
            // Spawn the chest near to the player death location if the safe position distance is higher than 20 Blocks
            lastSafePos = player.getLocation().getBlock().getLocation().clone();
            int it = 0;
            while (lastSafePos.getBlock().isEmpty() && lastSafePos.getBlockY() >= world.getMinHeight() && it <= 20) {
                lastSafePos = lastSafePos.subtract(0, 1, 0);
                it++;
            }
            if (!lastSafePos.getBlock().isEmpty())
                lastSafePos.add(0, 1, 0); // Spawn the chest on top of the highest block
        }

        // Clip the chest spawn to the world heights
        lastSafePos.setY(Math.min(Math.max(lastSafePos.getBlockY(), world.getMinHeight()), world.getMaxHeight() - 1)); // -1 because the max height is exclusive

        service.debug(1, "Checking protection service...");
        boolean build = service.getServiceSupportProvider().getProtectionService().canBuild(player, lastSafePos, Material.CHEST);
        if (!build)
            return;

        ChangeDeathMessageOptions changeDeathMessageOptions = config.changeDeathMessageOptions();
        if (changeDeathMessageOptions.enabled()) {
            service.debug(1, "Changing death message...");
            if (changeDeathMessageOptions.message() != null && lastSafePos.getWorld() != null) {
                StringSubstitutor substitution = new StringSubstitutor(Map.of("x", lastSafePos.getBlockX(), "y", lastSafePos.getBlockY(), "z", lastSafePos.getBlockZ(), "world", lastSafePos.getWorld().getName(), "player_name", player.getName(), "player_display_name", player.getDisplayName()));

                event.setDeathMessage(Arrays.stream(changeDeathMessageOptions.message()).map(substitution::replace).map(s -> {
                    if (DeathChestCorePlugin.isPlaceholderAPIEnabled()) {
                        return PlaceholderAPI.setPlaceholders(player, s);
                    }
                    return s;
                }).collect(Collectors.joining("\n")));
            } else {
                event.setDeathMessage(null); // Disable death message
            }
        }


        try {
            ThiefProtectionOptions thiefProtectionOptions = config.chestOptions().thiefProtectionOptions();
            boolean protectedChest = thiefProtectionOptions.enabled() && player.hasPermission(thiefProtectionOptions.permission()) && config.worldChestProtectionFilter().test(lastSafePos.getWorld());
            service.debug(1, "Protected chest: %s".formatted(protectedChest));

            PreDeathChestSpawnEvent preSpawn = new PreDeathChestSpawnEvent(player, lastSafePos, protectedChest, createdAt, expireAt, items);
            Bukkit.getPluginManager().callEvent(preSpawn);

            if (preSpawn.isCancelled())
                return;

            lastSafePos = preSpawn.getLocation();
            protectedChest = preSpawn.isProtectedChest();
            createdAt = preSpawn.getCreatedAt();
            expireAt = preSpawn.getExpireAt();
            items = preSpawn.getItems();

            var deathChest = service.createDeathChest(lastSafePos, createdAt, expireAt, player, protectedChest, items);

            DeathChestSpawnEvent deathChestSpawnEvent = new DeathChestSpawnEvent(player, deathChest);
            Bukkit.getPluginManager().callEvent(deathChestSpawnEvent);

            // Clears the drops
            service.debug(1, "Clearing drops...");
            event.getDrops().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
