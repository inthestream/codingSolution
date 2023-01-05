package com.yun.effective.ch34;

public class WeightTable {
    public static void main(String[] args) {
        double earthWeight = Double.parseDouble(args[0]);
        double mass = earthWeight / Planet.MERCURY.getSurfaceGravity();

        for (Planet p : Planet.values())
            System.out.printf("Weight on %s  is %f%n", p, p.getSurfaceWeight(mass));
    }
}
