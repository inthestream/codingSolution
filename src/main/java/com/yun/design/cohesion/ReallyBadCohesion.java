package com.yun.design.cohesion;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReallyBadCohesion {
    public boolean loadProcessAndStore() throws IOException {
        String[] words;
        List<String> sorted;

        try(FileReader reader = new FileReader("./resources/words.txt")) {
            char[] chars = new char[1024];
            reader.read(chars);
            words = new String(chars).split(" |\0");
        }
        sorted = Arrays.asList(words);
        sorted.sort(null);

        try (FileWriter writer = new FileWriter("./resources/test/sorted.txt")) {
            for (String word : sorted) {
                writer.write(word);
                writer.write("\n");
            }
            return true;
        }
    }
}
