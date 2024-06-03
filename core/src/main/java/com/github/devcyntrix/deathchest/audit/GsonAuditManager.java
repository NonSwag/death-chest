package com.github.devcyntrix.deathchest.audit;

import com.github.devcyntrix.deathchest.api.audit.AuditItem;
import com.github.devcyntrix.deathchest.api.audit.AuditManager;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Singleton
public class GsonAuditManager extends Thread implements AuditManager {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final File dataFolder;

    private final BlockingQueue<AuditItem> items = new LinkedBlockingQueue<>();

    public GsonAuditManager(File dataFolder) {
        this.dataFolder = dataFolder;
        if (!this.dataFolder.isDirectory() && !this.dataFolder.mkdirs())
            throw new RuntimeException("Cannot create audit folder \"%s\"".formatted(dataFolder));
        start();
    }

    @Override
    public void audit(AuditItem item) {
        Preconditions.checkState(items.offer(item), "Cannot offer audit item to the queue " + item);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                var item = items.take();

                var audit = "audit-" + formatter.format(item.date().toInstant()) + ".csv";
                var file = new File(dataFolder, audit);

                try (var writer = new FileWriter(file, true)) {
                    writer.write(item + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void close() {
        interrupt();
    }
}
