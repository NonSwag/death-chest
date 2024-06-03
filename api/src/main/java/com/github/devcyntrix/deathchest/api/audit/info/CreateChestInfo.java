package com.github.devcyntrix.deathchest.api.audit.info;

import com.github.devcyntrix.deathchest.api.audit.AuditInfo;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;

public interface CreateChestInfo extends AuditInfo {
    DeathChestModel chest();
}
