package com.yun.publiclibrary.spring.boot.actuate.health;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public abstract class HealthComponent {
    HealthComponent() {}

    @JsonUnwrapped
    public abstract Status getStatus();
}
