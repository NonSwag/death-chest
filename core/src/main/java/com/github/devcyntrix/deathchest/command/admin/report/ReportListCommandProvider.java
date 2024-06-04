package com.github.devcyntrix.deathchest.command.admin.report;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.report.Report;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReportListCommandProvider implements CommandProvider {
    private final DeathChestCorePlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder.handler(context -> {
            var reports = plugin.getReportManager().getReports();
            context.getSender().sendMessage(plugin.getPrefix() + "§7" + reports.stream()
                    .map(Report::date)
                    .map(Date::toString)
                    .collect(Collectors.joining(", ")));
        });
    }
}
