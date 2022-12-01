package com.yun.effective.ch02_creatingAndDestryingObjects;

public class A {
    public static A from(B b) {
        return new A();
    }
}
