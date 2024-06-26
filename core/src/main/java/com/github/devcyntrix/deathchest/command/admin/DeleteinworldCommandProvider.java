package com.github.devcyntrix.deathchest.command.admin;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.bukkit.parsers.WorldArgument;
import cloud.commandframework.context.CommandContext;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.audit.AuditAction;
import com.github.devcyntrix.deathchest.api.audit.info.DestroyReason;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.audit.CraftAuditItem;
import com.github.devcyntrix.deathchest.audit.CraftDestroyChestInfo;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteinworldCommandProvider implements CommandProvider {
    private final DeathChestCorePlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder
                .permission("deathchest.admin")
                .permission("deathchest.command.deleteinworld")
                .argument(
                        WorldArgument.of("world"),
                        ArgumentDescription.of("The world to delete death chests in.")
                )
                .handler(context -> {
                    var world = context.<World>get("world");

                    long deletedCount = plugin.getChests()
                            .map(deathChest -> deleteChest(context, deathChest))
                            .filter(aBoolean -> aBoolean)
                            .count();

                    context.getSender().sendMessage("§aA total of " + deletedCount + " chests were deleted in world " + world.getName());
                });
    }

    private boolean deleteChest(CommandContext<CommandSender> commandContext, DeathChestModel deathChest) {
        plugin.getDeathChestController().destroyChest(deathChest);

        plugin.getAuditManager().audit(new CraftAuditItem(new Date(), AuditAction.DESTROY_CHEST, new CraftDestroyChestInfo(
                deathChest,
                DestroyReason.COMMAND,
                Map.of("executor", commandContext.getSender(),
                        "command", "/" + commandContext.getRawInputJoined())
        )));

        return true;
    }

    private String formatLocation(Location location) {
        return String.format("%d, %d, %d in world %s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }
}
