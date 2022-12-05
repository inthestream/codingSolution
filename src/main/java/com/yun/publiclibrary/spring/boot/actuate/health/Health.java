package com.yun.publiclibrary.spring.boot.actuate.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Health extends HealthComponent {
    private final Status status;
    private final Map<String, Object> details;

    private Health(Health.Builder builder) {
        Assert.notNull(builder, "Builder must not be null");
        this.status = builder.status;
        this.details = Collections.unmodifiableMap(builder.details);
    }

    Health(Status status, Map<String, Object> details) {
        this.status = status;
        this.details = details;
    }

    public Status getStatus() { return this.status; }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, Object> getDetails() { return this.details; }

    Health withoutDetails() {
        return this.details.isEmpty() ? this : status(this.getStatus()).build();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Health)) {
            return false;
        } else {
            Health other = (Health) obj;
            return this.status.equals(other.status) && this.details.equals(other.details);
        }
    }

    public int hashCode() {
        int hashCode = this.status.hashCode();
        return 13 * hashCode + this.details.hashCode();
    }

    public String toString() { return this.getStatus() + " " + this.getDetails(); }

    public static Health.Builder unknown() { return status(Status.UNKNOWN); }
    public static Health.Builder up() { return status(Status.UP); }
    public static Health.Builder down(Exception ex) { return down().withException(ex); }
    public static Health.Builder down() { return status(Status.DOWN); }
    public static Health.Builder outOfService() { return status(Status.OUT_OF_SERVICE); }

    public static Health.Builder status(String statusCode) { return status(new Status(statusCode)); }

    public static Health.Builder status(Status status) { return new Health.Builder(status); }

    public static class Builder {
        private Status status;
        private Map<String, Object> details;

        public Builder() {
            this.status = Status.UNKNOWN;
            this.details = new LinkedHashMap<>();
        }

        public Builder(Status status) {
            Assert.notNull(status, "Status must not be null");
            this.status = status;
            this.details = new LinkedHashMap<>();
        }

        public Builder(Status status, Map<String, ?> details) {
            Assert.notNull(status, "Status must not be null");
            Assert.notNull(details, "Details must not be null");
            this.status = status;
            this.details = new LinkedHashMap<>(details);
        }

        public Health.Builder withException(Throwable ex) {
            Assert.notNull(ex, "Exception must not be null");
            return this.withDetail("error", ex.getClass().getName() + ": " + ex.getMessage());
        }

        public Health.Builder withDetail(String key, Object value) {
            Assert.notNull(key, "Key must not be null");
            Assert.notNull(value, "Value must not be null");
            this.details.put(key, value);
            return this;
        }

        public Health.Builder withDetails(Map<String, ?> details) {
            Assert.notNull(details, "Details must not be null");
            this.details.putAll(details);
            return this;
        }

        public Health.Builder unknown() {
            return this.status(Status.UNKNOWN);
        }

        public Health.Builder up() {
            return this.status(Status.UP);
        }

        public Health.Builder down(Throwable ex) {
            return this.down().withException(ex);
        }

        public Health.Builder down() {
            return this.status(Status.DOWN);
        }

        public Health.Builder outOfService() {
            return this.status(Status.OUT_OF_SERVICE);
        }

        public Health.Builder status(String statusCode) {
            return this.status(new Status(statusCode));
        }

        public Health.Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Health build() { return new Health(this); }
    }

}
