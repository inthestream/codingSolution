package com.yun.design.cohesion;

import java.util.List;

public interface WordsListener {
    void onWordsChanged(List<String> sorted);
}
