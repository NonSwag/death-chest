package com.github.devcyntrix.deathchest.api.audit;

import java.util.Date;

public interface AuditItem {
    AuditAction action();

    AuditInfo info();

    Date date();
}
