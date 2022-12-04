package com.yun.effective.ch02_creatingAndDestryingObjects.singleton;

// Singleton with public final field
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public void leaveTheBuilding() {}
}
