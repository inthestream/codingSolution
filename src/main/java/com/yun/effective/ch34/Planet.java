package com.yun.effective.ch34;

public enum Planet {
    MERCURY(3.302e+23, 2.439e6);


    private final double mass;          // In kilograms
    private final double radius;        // In meters
    private final double surfaceGravity;     // In m / s^2

    // Universal gravitational constant in m^3 / kg s^2
    private static final double G = 6.67300E-11;

    // Constructor
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius*radius);
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getSurfaceGravity() {
        return surfaceGravity;
    }

    public double getSurfaceWeight(double mass) {
        return mass * surfaceGravity; // F = ma
    }
}
