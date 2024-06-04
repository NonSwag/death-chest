package com.github.devcyntrix.deathchest.api;

import com.github.devcyntrix.deathchest.api.audit.AuditManager;
import com.github.devcyntrix.deathchest.api.compatibility.ServiceSupportProvider;
import com.github.devcyntrix.deathchest.api.controller.DeathChestController;
import com.github.devcyntrix.deathchest.api.controller.HologramController;
import com.github.devcyntrix.deathchest.api.controller.LastSafeLocationController;
import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.ItemBlacklist;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public interface DeathChestService extends Plugin {

    ServiceSupportProvider getServiceSupportProvider();

    PlaceholderController getPlaceholderController();

    AuditManager getAuditManager();

    Map<Player, DeathChestModel> getLastDeathChests();

    LastSafeLocationController getLastSafeLocationController();

    ItemBlacklist getBlacklist();

    boolean isTest();

    boolean isDebugMode();

    void debug(int indents, Object... message);

    @Nullable
    DeathChestModel getLastChest(@NotNull Player player);

    boolean canPlaceChestAt(@NotNull Location location);

    DeathChestController getDeathChestController();

    DeathChestConfig getDeathChestConfig();

    @NotNull
    DeathChestModel createDeathChest(@NotNull Location location, ItemStack @NotNull ... items);

    @NotNull
    DeathChestModel createDeathChest(@NotNull Location location, @Nullable Player player, ItemStack @NotNull ... items);

    @NotNull
    DeathChestModel createDeathChest(@NotNull Location location, long expireAt, @Nullable Player player, ItemStack @NotNull ... items);

    @NotNull
    DeathChestModel createDeathChest(@NotNull Location location, long createdAt, long expireAt, @Nullable Player player, ItemStack @NotNull ... items);

    @NotNull
    DeathChestModel createDeathChest(@NotNull Location location, long createdAt, long expireAt, @Nullable Player player, boolean isProtected, ItemStack @NotNull ... items);

    @NotNull
    Stream<@NotNull DeathChestModel> getChests();

    @NotNull
    Stream<@NotNull DeathChestModel> getChests(@NotNull World world);

    void reload();

    @Nullable
    HologramController getHologramController();

    String getPrefix();

    Audience getAudience(CommandSender sender);

    void broadcast(Component component);
}
