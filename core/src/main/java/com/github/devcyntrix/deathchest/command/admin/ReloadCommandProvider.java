package com.github.devcyntrix.deathchest.command.admin;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.audit.AuditAction;
import com.github.devcyntrix.deathchest.audit.CraftAuditItem;
import com.github.devcyntrix.deathchest.audit.CraftReloadInfo;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.Date;

@RequiredArgsConstructor
public class ReloadCommandProvider implements CommandProvider {
    private final DeathChestService service;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder
                .permission("deathchest.admin")
                .permission("deathchest.command.reload")
                .handler(context -> {
                    service.getAuditManager().audit(new CraftAuditItem(
                            new Date(),
                            AuditAction.RELOAD_PLUGIN,
                            new CraftReloadInfo(context.getSender())
                    ));
                    service.reload();
                    context.getSender().sendMessage(service.getPrefix() + "§cThe plugin has been successfully reloaded");
                });
    }
}
