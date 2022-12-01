package com.yun.effective.ch02_creatingAndDestryingObjects;

public class B {
    private static B b;

    public static B getInstance() {
        return b == null ? new B() : b;
    }
}
