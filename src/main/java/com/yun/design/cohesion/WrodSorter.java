package com.yun.design.cohesion;

import java.util.Arrays;
import java.util.List;

public class WrodSorter {
    public void sortWords(WordSource words, WordsListener listener) {
        listener.onWordsChanged(sort(words.words()));
    }

    private List<String> sort(String[] words) {
        List<String> sorted = Arrays.asList(words);
        sorted.sort(null);
        return sorted;
    }
}
