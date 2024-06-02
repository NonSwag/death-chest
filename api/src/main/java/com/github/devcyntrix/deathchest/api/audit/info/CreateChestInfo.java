package com.github.devcyntrix.deathchest.api.audit.info;

import com.github.devcyntrix.deathchest.api.audit.AuditInfo;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateChestInfo extends AuditInfo {
    @Expose
    private final DeathChestModel chest;
}
