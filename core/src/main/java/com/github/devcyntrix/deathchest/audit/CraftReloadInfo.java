package com.github.devcyntrix.deathchest.audit;

import com.github.devcyntrix.deathchest.api.audit.info.ReloadInfo;
import com.google.gson.annotations.Expose;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public record CraftReloadInfo(@Expose CommandSender sender) implements ReloadInfo {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CraftReloadInfo) obj;
        return Objects.equals(this.sender, that.sender);
    }

    @Override
    public String toString() {
        return CraftAuditItem.GSON.toJson(this);
    }
}
