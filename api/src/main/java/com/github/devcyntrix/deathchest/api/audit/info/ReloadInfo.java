package com.github.devcyntrix.deathchest.api.audit.info;

import com.github.devcyntrix.deathchest.api.audit.AuditInfo;
import org.bukkit.command.CommandSender;

public interface ReloadInfo extends AuditInfo {
    CommandSender sender();
}
