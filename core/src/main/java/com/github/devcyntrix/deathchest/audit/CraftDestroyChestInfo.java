package com.github.devcyntrix.deathchest.audit;

import com.github.devcyntrix.deathchest.api.audit.info.DestroyChestInfo;
import com.github.devcyntrix.deathchest.api.audit.info.DestroyReason;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.google.gson.annotations.Expose;

import java.util.Map;

public record CraftDestroyChestInfo(
        @Expose DeathChestModel chest,
        @Expose DestroyReason reason,
        @Expose Map<String, Object> extra
) implements DestroyChestInfo {
}
