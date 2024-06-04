package com.github.devcyntrix.deathchest.command.admin.report;

import cloud.commandframework.Command;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.report.ReportManager;
import com.github.devcyntrix.deathchest.command.CommandProvider;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;

@RequiredArgsConstructor
public class ReportLatestCommandProvider implements CommandProvider {
    private final DeathChestCorePlugin plugin;

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder.handler(commandContext -> {
            var latestReport = plugin.getReportManager().getLatestReport();
            if (latestReport == null) {
                commandContext.getSender().sendMessage(plugin.getPrefix() + "§cNo report found");
                return;
            }

            BaseComponent[] baseComponents = TextComponent.fromLegacyText(plugin.getPrefix() + "§7");
            DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
            TextComponent message = new TextComponent(
                    "The latest report you created is from " +
                    dateTimeInstance.format(latestReport.date()) +
                    " "
            );
            message.setColor(ChatColor.GRAY);

            TextComponent copy = new TextComponent("[Copy]");
            copy.setColor(ChatColor.RED);
            copy.setUnderlined(true);
            copy.setClickEvent(
                    new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                            ReportManager.formatISO(latestReport.date()))
            );
            copy.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to copy the file name"))
            );


            BaseComponent[] components = new BaseComponent[baseComponents.length + 2];

            System.arraycopy(baseComponents, 0, components, 0, baseComponents.length);
            components[baseComponents.length] = message;
            components[baseComponents.length + 1] = copy;

            commandContext.getSender().spigot().sendMessage(components);
        });
    }
}
