package com.yun.design.cohesion;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BadCohesion {
    public boolean loadProcessAndStore() throws IOException {
        String[] words = readWords();
        List<String> sorted = sortWords(words);
        return storeWords(sorted);
    }

    private String[] readWords() throws IOException {
        try (FileReader reader = new FileReader("./resources/words/txt")) {
            char[] chars = new char[1024];
            reader.read(chars);
            return new String(chars).split(" |\0");
        }
    }

    private List<String> sortWords(String[] words) {
        List<String> sorted = Arrays.asList(words);
        sorted.sort(null);
        return sorted;
    }

    private boolean storeWords(List<String> sorted) throws IOException {
        try (FileWriter writer = new FileWriter("./resources/test/sorted.txt")) {
            for (String word : sorted) {
                writer.write(word);
                writer.write("\n");
            }
            return true;
        }
    }
}
