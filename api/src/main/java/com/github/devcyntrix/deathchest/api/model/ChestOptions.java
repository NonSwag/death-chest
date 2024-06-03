package com.github.devcyntrix.deathchest.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public interface ChestOptions {
    @Nullable
    Duration expiration();

    @NotNull
    NoExpirationPermission noExpirationPermission();

    boolean dropItemsAfterExpiration();

    boolean blastProtection();

    @NotNull
    ThiefProtectionOptions thiefProtectionOptions();
}