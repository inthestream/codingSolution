package com.yun.effective.ch20_again;

import com.yun.publiclibrary.java.e2.AbstractList;

import java.util.List;
import java.util.Objects;

public class ExampleList {
    // Concrete implementation built atop skeletal implementation
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return a[index];
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }
        };
    }
}
