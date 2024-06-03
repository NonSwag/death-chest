package com.github.devcyntrix.deathchest.api.audit;

import java.io.Closeable;

public interface AuditManager extends Closeable {
    void audit(AuditItem item);
}
