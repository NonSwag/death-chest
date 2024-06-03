package com.github.devcyntrix.deathchest.api.report;

import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface Report {
    Date date();

    Set<PluginInfo> plugins();

    DeathChestConfig config();

    Map<String, Object> extra();
}
