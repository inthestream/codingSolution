package com.yun.thread.ch02;

import java.util.Random;

public class RandomCharacterGenerator extends Thread implements CharacterSource {
    static char[] chars;
    static String charArray = "abcdefghijklmnopqrstuvwxyz0123456789";
    static {
        chars = charArray.toCharArray();
    }

    Random random;
    CharacterEventHandler handler;

    public RandomCharacterGenerator() {
        random = new Random();
        handler = new CharacterEventHandler();
    }

    public int getPauseTime() {
        return (int) (Math.max(1000, 5000 * random.nextDouble()));
    }

    @Override
    public void addCharacterListener(CharacterListener c1) {
        handler.addCharacterListener(c1);
    }

    @Override
    public void removeCharacterListener(CharacterListener c1) {
        handler.removeCharacterListener(c1);
    }

    public void nextCharacter() {
        int i = random.nextInt(chars.length);
        System.out.println(i);
        handler.fireNewCharacter(this, (int) chars[i]);
    }

    public void run() {
        for(;;) {
            nextCharacter();
            try {
                Thread.sleep(getPauseTime());
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
