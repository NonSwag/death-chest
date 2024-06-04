package com.github.devcyntrix.deathchest.support;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.animation.BreakAnimationService;
import com.github.devcyntrix.deathchest.support.animation.PaperBreakAnimation;
import lombok.Getter;

@Getter
public class PaperServiceSupportProvider extends CraftServiceSupportProvider {
    private final BreakAnimationService animationService = new PaperBreakAnimation();

    public PaperServiceSupportProvider(DeathChestService service) {
        super(service);
    }
}
