package com.yun.design.cohesion;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Essential {
    public boolean loadProcessAndStore(Accidental accidental) throws IOException {
        List<String> sorted = sortWords(accidental.readWords());
        return accidental.storeWords(sorted);
    }

    private List<String> sortWords(String[] words) {
        List<String> sorted = Arrays.asList(words);
        sorted.sort(null);
        return sorted;
    }
}
