package com.github.devcyntrix.deathchest.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface WorldAliasConfig {
    Map<String, Object> aliases();

    @NotNull
    String getAlias(@NotNull String worldName);
}
