package com.github.devcyntrix.deathchest.controller;

import com.github.devcyntrix.deathchest.DeathChestPlugin;
import com.github.devcyntrix.deathchest.api.controller.PlaceholderController;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.util.ChestModelStringLookup;
import com.google.inject.Singleton;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.function.Function;

@Getter
@Singleton
public class CraftPlaceholderController implements PlaceholderController {
    private final DeathChestConfig config;
    private final Function<Long, String> duration;

    public CraftPlaceholderController(DeathChestConfig config) {
        this.config = config;
        this.duration = expiresAt -> {
            long duration = expiresAt - System.currentTimeMillis();
            if (duration <= 0) duration = 0;
            return DurationFormatUtils.formatDuration(duration, config.durationFormat());
        };
    }

    public String replace(DeathChestModel model, String base) {
        StringSubstitutor substitutor = new StringSubstitutor(new ChestModelStringLookup(config, model, duration));
        base = substitutor.replace(base);
        if (DeathChestPlugin.isPlaceholderAPIEnabled()) base = PlaceholderAPI.setPlaceholders(model.getOwner(), base);
        return base;
    }

}
