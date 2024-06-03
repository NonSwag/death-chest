package com.github.devcyntrix.deathchest.api.audit.info;

import com.github.devcyntrix.deathchest.api.audit.AuditInfo;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;

import java.util.Map;

public interface DestroyChestInfo extends AuditInfo {
    DeathChestModel chest();

    DestroyReason reason();

    Map<String, Object> extra();
}
