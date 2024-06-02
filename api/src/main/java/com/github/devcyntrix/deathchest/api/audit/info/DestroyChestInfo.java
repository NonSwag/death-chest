package com.github.devcyntrix.deathchest.api.audit.info;

import com.github.devcyntrix.deathchest.api.audit.AuditInfo;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class DestroyChestInfo extends AuditInfo {
    private final @Expose DeathChestModel chest;
    private final @Expose DestroyReason reason;
    private final @Expose Map<String, Object> extra;
}
