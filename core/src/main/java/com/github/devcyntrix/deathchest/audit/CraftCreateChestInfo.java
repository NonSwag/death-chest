package com.github.devcyntrix.deathchest.audit;

import com.github.devcyntrix.deathchest.api.audit.info.CreateChestInfo;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.google.gson.annotations.Expose;

public record CraftCreateChestInfo(@Expose DeathChestModel chest) implements CreateChestInfo {
}
