package com.github.devcyntrix.deathchest;

import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import com.github.devcyntrix.deathchest.api.compatibility.ServiceSupportProvider;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.support.CraftServiceSupportProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@Getter
@NoArgsConstructor
public class DeathChestSpigotPlugin extends DeathChestCorePlugin {
    private final ServiceSupportProvider serviceSupportProvider = new CraftServiceSupportProvider(this) {
        private final @Getter BreakAnimationService animationService = getUsableService(getAnimationServices());
    };
    private BukkitAudiences audiences;
    private boolean test = false;

    @SuppressWarnings("unused")
    public DeathChestSpigotPlugin(DeathChestConfig config) {
        setDeathChestConfig(config);
        this.test = true;
    }

    @Override
    public void onEnable() {
        this.audiences = BukkitAudiences.create(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        audiences.close();
        super.onDisable();
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
