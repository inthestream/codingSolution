package com.yun.effective.ch08;

import com.yun.effective.ch10.CaseInsensitiveString;

public class Adult {
    public static void main(String[] args) {
        try (Room myRoom = new Room(7)) {
            System.out.println("Goodbye");
        }
    }
}
