package com.yun.effective.ch23;

public class Circle extends Figure {
    final double radius;

    Circle(double radius) { this. radius = radius; }

    @Override
    double area() {
        return Math.PI * (radius * radius);
    }
}
