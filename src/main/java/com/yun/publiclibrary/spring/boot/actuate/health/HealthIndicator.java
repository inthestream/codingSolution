package com.yun.publiclibrary.spring.boot.actuate.health;

@FunctionalInterface
public interface HealthIndicator extends HealthContributor {
    default Health getHealth(boolean includeDetails) {
        Health health = this.health();
        return includeDetails ? health : health.withoutDetails();
    }

    Health health();
}
