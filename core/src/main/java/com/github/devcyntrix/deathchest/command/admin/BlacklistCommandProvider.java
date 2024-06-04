package com.github.devcyntrix.deathchest.command.admin;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.blacklist.CraftItemBlacklist;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class BlacklistCommandProvider implements CommandProvider {
    private final DeathChestCorePlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder
                .permission("deathchest.admin")
                .permission("deathchest.command.blacklist")
                .handler(context -> {
                    CommandSender sender = context.getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("§cYou have to be in game");
                        return;
                    }
                    CraftItemBlacklist blacklist = plugin.getBlacklist();
                    Inventory inventory = blacklist.getInventory();
                    player.openInventory(inventory);
                });
    }

}
