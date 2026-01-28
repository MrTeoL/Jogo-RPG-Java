package br.com.rpg.util;

import java.util.Random;

public final class RandomProvider {

    private static final Random RANDOM = new Random();

    private RandomProvider() {}

    public static Random get() {
        return RANDOM;
    }
}
