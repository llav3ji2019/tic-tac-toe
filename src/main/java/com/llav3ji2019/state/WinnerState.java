package com.llav3ji2019.state;

import lombok.Getter;

@Getter
public enum WinnerState {
    DRAW("The TicTacToe is a Draw."),
    X_WINNER("Player X is the winner."),
    O_WINNER("Player O is the winner."),
    NO_WINNER("Match is not finished.");

    private final String value;

    WinnerState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
