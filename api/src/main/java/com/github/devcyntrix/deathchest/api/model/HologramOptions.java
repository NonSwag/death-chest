package com.github.devcyntrix.deathchest.api.model;

import java.util.Collection;

public interface HologramOptions {
    boolean enabled();

    double height();

    double lineHeight();

    Collection<String> lines();
}
