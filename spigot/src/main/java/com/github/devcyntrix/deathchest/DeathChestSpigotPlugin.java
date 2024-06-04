package com.github.devcyntrix.deathchest;

import com.github.devcyntrix.deathchest.api.compatibility.ServiceSupportProvider;
import com.github.devcyntrix.deathchest.support.CraftServiceSupportProvider;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@Getter
public class DeathChestSpigotPlugin extends DeathChestCorePlugin {
    private final ServiceSupportProvider serviceSupportProvider = new CraftServiceSupportProvider(this);
    private final BukkitAudiences audiences = BukkitAudiences.create(this);

    @Override
    public void onDisable() {
        audiences.close();
        super.onDisable();
    }

    @Override
    public ServiceSupportProvider getServiceSupportProvider() {
        return null;
    }

    @Override
    public Audience getAudience(CommandSender sender) {
        return audiences.sender(sender);
    }

    @Override
    public void broadcast(Component component) {
        audiences.all().sendMessage(component);
    }
}
