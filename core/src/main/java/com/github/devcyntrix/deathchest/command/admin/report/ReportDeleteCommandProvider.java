package com.github.devcyntrix.deathchest.command.admin.report;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.report.Report;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import com.github.devcyntrix.deathchest.command.admin.argument.DateArgument;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ReportDeleteCommandProvider implements CommandProvider {
    private final DeathChestCorePlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder
                .argument(DateArgument.of(plugin, "date"))
                .handler(context -> {
                    var date = context.<Report>get("date");
                    if (plugin.getReportManager().deleteReport(date)) {
                        context.getSender().sendMessage(
                                plugin.getPrefix() + "§7You deleted the report successfully"
                        );
                    } else {
                        context.getSender().sendMessage(
                                plugin.getPrefix() + "§cCannot find report"
                        );
                    }
                });
    }
}
