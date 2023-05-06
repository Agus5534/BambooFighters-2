package io.github.agus5534.bamboofightersv2.utils.extra;

import io.github.agus5534.bamboofightersv2.arenas.GameArena;
import io.github.agus5534.bamboofightersv2.exceptions.GameArenaCreationException;

public class Validate {
    public static boolean isNull(Object o) {
        return o == null;
    }

    public static Object notNull(Object o, String error, Throwable throwable) {
        if(o == null) {
            throw new RuntimeException(error, throwable);
        }

        return o;
    }
}