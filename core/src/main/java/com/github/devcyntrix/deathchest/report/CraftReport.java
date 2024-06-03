package com.github.devcyntrix.deathchest.report;

import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.report.PluginInfo;
import com.github.devcyntrix.deathchest.api.report.Report;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record CraftReport(
        @SerializedName("date") Date date,
        @SerializedName("plugins") Set<PluginInfo> plugins,
        @SerializedName("config") DeathChestConfig config,
        @SerializedName("extra") Map<String, Object> extra
) implements Report {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CraftReport report = (CraftReport) o;
        return Objects.equals(date, report.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
