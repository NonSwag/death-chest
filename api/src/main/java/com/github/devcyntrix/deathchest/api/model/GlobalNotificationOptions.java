package com.github.devcyntrix.deathchest.api.model;

import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;

public interface GlobalNotificationOptions {
    boolean enabled();

    String[] message();

    void showNotification(DeathChestModel model, PlaceholderController controller);
}
