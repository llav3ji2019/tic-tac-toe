package com.llav3ji2019.state;

import lombok.Getter;

@Getter
public enum PlayerState {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final char value;

    PlayerState(char value) {
        this.value = value;
    }

    public WinnerState convertBoardStateToWinnerState() {
        return switch (this) {
            case O -> WinnerState.O_WINNER;
            case X -> WinnerState.X_WINNER;
            default -> WinnerState.NO_WINNER;
        };
    }

    @Override
    public String toString() {
        return String.valueOf(this.getValue());
    }
}
