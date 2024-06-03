package com.github.devcyntrix.deathchest.command.admin.report;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestPlugin;
import com.github.devcyntrix.deathchest.api.report.PluginInfo;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import com.github.devcyntrix.deathchest.report.CraftReport;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReportCreateCommandProvider implements CommandProvider {
    private final DeathChestPlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder.handler(context -> {
            var collect = Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .map(PluginInfo::of)
                    .collect(Collectors.toSet());
            var report = new CraftReport(new Date(), collect, plugin.getDeathChestConfig(), new HashMap<>());
            plugin.getReportManager().addReport(report);
            context.getSender().sendMessage(plugin.getPrefix() + "§7A new report was created successfully.");
        });
    }
}
