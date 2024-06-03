package com.github.devcyntrix.deathchest.api.controller;

import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;

import java.util.function.Function;

public interface PlaceholderController {
    DeathChestConfig getConfig();

    Function<Long, String> getDuration();

    String replace(DeathChestModel model, String base);
}
