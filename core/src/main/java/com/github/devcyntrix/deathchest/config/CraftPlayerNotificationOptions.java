package com.github.devcyntrix.deathchest.config;

import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.PlayerNotificationOptions;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CraftPlayerNotificationOptions(
        @SerializedName("enabled") boolean enabled,
        @SerializedName("messages") String[] message) implements PlayerNotificationOptions {

    @Contract("null -> new")
    public static @NotNull CraftPlayerNotificationOptions load(@Nullable ConfigurationSection section) {
        if (section == null)
            return new CraftPlayerNotificationOptions(false, null);

        boolean enabled = section.getBoolean("enabled", false);
        String message = section.getString("message");
        String[] coloredMessage = null;
        if (message != null) {
            TextComponent deserialize = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
            String serialize = MiniMessage.miniMessage().serialize(deserialize)
                    .replace("\\", ""); // Necessary to combine legacy color codes with mini message
            coloredMessage = serialize.split("\n");
        }

        return new CraftPlayerNotificationOptions(enabled, coloredMessage);
    }

    public void showNotification(Audience audience, DeathChestModel model, PlaceholderController controller) {
        for (String message : message()) {
            message = controller.replace(model, message);

            Component deserialize = MiniMessage.miniMessage().deserialize(message);
            audience.sendMessage(deserialize);
        }
    }
}
