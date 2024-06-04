package com.github.devcyntrix.deathchest.model;

import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.GlobalNotificationOptions;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CraftGlobalNotificationOptions(
        @SerializedName("enabled") boolean enabled,
        @SerializedName("message") String[] message) implements GlobalNotificationOptions {

    public static @NotNull CraftGlobalNotificationOptions load(@Nullable ConfigurationSection section) {
        if (section == null)
            return new CraftGlobalNotificationOptions(false, null);

        boolean enabled = section.getBoolean("enabled", false);
        String message = section.getString("message");
        String[] coloredMessage = null;
        if (message != null) {
            TextComponent deserialize = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
            String serialize = MiniMessage.miniMessage().serialize(deserialize)
                    .replace("\\", ""); // Necessary to combine legacy color codes with mini message
            coloredMessage = serialize.split("\n");
        }

        return new CraftGlobalNotificationOptions(enabled, coloredMessage);
    }

    public void showNotification(DeathChestModel model, PlaceholderController controller) {
        var plugin = JavaPlugin.getPlugin(DeathChestCorePlugin.class);
        for (String message : message()) {
            message = controller.replace(model, message);
            plugin.broadcast(MiniMessage.miniMessage().deserialize(message));
        }
    }
}
