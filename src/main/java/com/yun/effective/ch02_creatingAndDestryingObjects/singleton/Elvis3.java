package com.yun.effective.ch02_creatingAndDestryingObjects.singleton;

public class Elvis3 {
    private static Elvis3 INSTANCE = null;
    private Elvis3() {}

    public static Elvis3 getInstance() {
        if (INSTANCE == null) {
            synchronized (Elvis3.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Elvis3();
                }
            }
        }
        return INSTANCE;
    }
}
