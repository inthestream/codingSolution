package com.yun.design.cohesion;

import java.io.IOException;
import java.util.List;

public interface Accidental {
    String[] readWords() throws IOException;
    boolean storeWords(List<String> sorted) throws IOException;
}
