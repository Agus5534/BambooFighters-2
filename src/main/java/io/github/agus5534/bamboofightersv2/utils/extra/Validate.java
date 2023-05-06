package io.github.agus5534.bamboofightersv2.utils.extra;

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

    public static void checkNull(Object o, String error) {
        if(o == null) {
            throw new RuntimeException(error);
        }
    }

    public static void checkNull(Object o, String error, Throwable throwable) {
        if(o == null) {
            throw new RuntimeException(error, throwable);
        }
    }
}
