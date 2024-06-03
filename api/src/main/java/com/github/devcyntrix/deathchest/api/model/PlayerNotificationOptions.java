package com.github.devcyntrix.deathchest.api.model;

import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;
import net.kyori.adventure.audience.Audience;

public interface PlayerNotificationOptions {
    boolean enabled();

    String[] message();

    void showNotification(Audience audience, DeathChestModel model, PlaceholderController controller);
}
