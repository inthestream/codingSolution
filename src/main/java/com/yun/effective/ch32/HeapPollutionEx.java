package com.yun.effective.ch32;

import java.util.Arrays;
import java.util.List;

public class HeapPollutionEx {
    public static void main(String[] args) {
        List<String> strings1 = Arrays.asList("first");
        List<String> strings2 = Arrays.asList("second");
        doSomthing(strings1, strings2);
    }

    private static void doSomthing(List<String> ... stringLists) {
        List<Integer> intList = Arrays.asList(42);
        Object[] objects = stringLists;
        objects[0] = intList; // heap pollution
        String s = stringLists[0].get(0); // ClassCastException
    }
}
