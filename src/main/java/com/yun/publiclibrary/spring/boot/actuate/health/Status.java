package com.yun.publiclibrary.spring.boot.actuate.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class Status {
    public static final Status UNKNOWN = new Status("UNKNOWN");
    public static final Status UP = new Status("UP");
    public static final Status DOWN = new Status("DOWN");
    public static final Status OUT_OF_SERVICE = new Status("OUT_OF_SERVICE");
    private final String code;
    private final String description;

    public Status(String code) { this(code, ""); }

    public Status(String code, String description) {
        Assert.notNull(code, "Code must not be null");
        Assert.notNull(description, "Description must not be null");
        this.code = code;
        this.description = description;
    }

    @JsonProperty("status")
    public String getCode() { return this.code; }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getDescription() { return this.description; }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof Status ? ObjectUtils.nullSafeEquals(this.code, ((Status)obj).code) : false;
        }
    }

    public int hashCode() { return this.code.hashCode(); }
    public String toString() { return this.code; }
}
