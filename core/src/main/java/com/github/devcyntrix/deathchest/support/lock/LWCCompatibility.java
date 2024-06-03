package com.github.devcyntrix.deathchest.support.lock;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.compatibility.Compatibility;
import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;

public class LWCCompatibility extends Compatibility {

    @Override
    public boolean isValid(Server server) {
        return server.getPluginManager().isPluginEnabled("LWC");
    }

    @Override
    protected void enable(DeathChestService plugin) {
        try {
            Class.forName("com.griefcraft.lwc.LWC");
            LWC.getInstance().getModuleLoader().registerModule(plugin, new LWCModule(plugin));
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Override
    protected void disable(DeathChestService plugin) {
        try {
            Class.forName("com.griefcraft.lwc.LWC");
            LWC.getInstance().getModuleLoader().removeModules(plugin);
        } catch (ClassNotFoundException ignored) {
        }
    }

    @RequiredArgsConstructor
    public static class LWCModule extends JavaModule {
        private final DeathChestService service;

        @Override
        public void onRegisterProtection(LWCProtectionRegisterEvent event) {
            var controller = service.getDeathChestController();
            if (controller.getChest(event.getBlock().getLocation()) != null) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(service.getPrefix() + "§cYou cannot lock this chest.");
            }
        }
    }

}
