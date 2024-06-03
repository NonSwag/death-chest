package com.github.devcyntrix.deathchest.command.admin.report;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestPlugin;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ReportDeleteallCommandProvider implements CommandProvider {
    private final DeathChestPlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder.handler(context -> {
            plugin.getReportManager().deleteReports();
            context.getSender().sendMessage(plugin.getPrefix() + "§7You deleted all reports successfully");
        });
    }
}
