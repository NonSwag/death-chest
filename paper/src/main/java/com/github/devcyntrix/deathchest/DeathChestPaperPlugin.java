package com.github.devcyntrix.deathchest;

import com.github.devcyntrix.deathchest.api.compatibility.ServiceSupportProvider;
import com.github.devcyntrix.deathchest.support.PaperServiceSupportProvider;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@Getter
public class DeathChestPaperPlugin extends DeathChestCorePlugin {
    private final ServiceSupportProvider serviceSupportProvider = new PaperServiceSupportProvider(this);

    @Override
    public Audience getAudience(CommandSender sender) {
        return sender;
    }

    @Override
    public void broadcast(Component component) {
        Bukkit.broadcast(component);
    }
}
