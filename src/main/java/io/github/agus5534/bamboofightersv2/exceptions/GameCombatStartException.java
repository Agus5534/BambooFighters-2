package io.github.agus5534.bamboofightersv2.exceptions;

public class GameCombatStartException extends RuntimeException {

    public GameCombatStartException(String message, Throwable error) {
        super(message, error);
    }

    public GameCombatStartException() {
        super();
    }
}
