package com.github.devcyntrix.deathchest.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.command.admin.BlacklistCommandProvider;
import com.github.devcyntrix.deathchest.command.admin.DeleteinworldCommandProvider;
import com.github.devcyntrix.deathchest.command.admin.ReloadCommandProvider;
import com.github.devcyntrix.deathchest.command.admin.report.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CommandRegistry {
    private final CommandManager<CommandSender> commandManager;

    public static CommandRegistry create(DeathChestCorePlugin plugin) throws Exception {
        return new CommandRegistry(new BukkitCommandManager<>(
                plugin,
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
        ));
    }

    public void registerCommands(DeathChestCorePlugin plugin) {
        Command.Builder<CommandSender> rootCommand = commandManager.commandBuilder("deathchest");

        commandManager.command(rootCommand);

        commandManager.command(
                new ReloadCommandProvider(plugin)
                        .provide(rootCommand.literal("reload"))
        );
        commandManager.command(
                new DeleteinworldCommandProvider(plugin)
                        .provide(rootCommand.literal("deleteinworld"))
        );
        commandManager.command(
                new BlacklistCommandProvider(plugin)
                        .provide(rootCommand.literal("blacklist"))
        );

        Command.Builder<CommandSender> reportCommandBuilder = new ReportCommandProvider().provide(
                rootCommand.literal("report")
        );
        commandManager.command(reportCommandBuilder);

        commandManager.command(
                new ReportCreateCommandProvider(plugin).provide(
                        reportCommandBuilder.literal("create")
                )
        );
        commandManager.command(
                new ReportDeleteallCommandProvider(plugin).provide(
                        reportCommandBuilder.literal("deleteall")
                )
        );
        commandManager.command(
                new ReportDeleteCommandProvider(plugin).provide(
                        reportCommandBuilder.literal("delete")
                )
        );
        commandManager.command(
                new ReportLatestCommandProvider(plugin).provide(
                        reportCommandBuilder.literal("latest")
                )
        );
        commandManager.command(
                new ReportListCommandProvider(plugin).provide(
                        reportCommandBuilder.literal("list")
                )
        );

    }
}
