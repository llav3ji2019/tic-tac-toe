package com.llav3ji2019.controller;

import com.llav3ji2019.exception.ParameterException;
import com.llav3ji2019.state.PlayerState;
import com.llav3ji2019.state.GameState;
import com.llav3ji2019.state.WinnerState;
import com.llav3ji2019.structure.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Class for representing the Tic Tac Toe board.
 */
public class Board {

    /**
     * variable for saving states of all positions on board
     */
    private List<List<PlayerState>> boardData;

    private int boardSize;

    /**
     * variable for saving current turn of player
     */
    @Getter
    @Setter
    private PlayerState playerTurn = PlayerState.O;

    /**
     * variable for saving game state
     */
    private GameState gameState = GameState.IN_PROGRESS;

    /**
     * variable for saving winner state
     */
    @Getter
    private WinnerState winner = WinnerState.NO_WINNER;

    /**
     * set with all available positions
     */
    @Getter
    private Set<Coordinates> positionsAvailable;

    public Board(int fieldSize) {
        initBoard(fieldSize);
    }

    /**
     * function for get copy of Board's fields
     * @return copy of this Board
     * @throws ParameterException if fieldSize is less than 2
     */
    public Board getBoardCopy() {
        Board newBoard = new Board(boardSize);

        newBoard.boardData = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            newBoard.boardData.add(new ArrayList<>(this.boardData.get(i)));
        }
        newBoard.playerTurn = this.playerTurn;
        newBoard.winner = this.winner;
        newBoard.positionsAvailable = new HashSet<>(this.positionsAvailable);
        newBoard.gameState = this.gameState;
        return newBoard;
    }

    /**
     * function for making next move with coordinates
     * @param coordinates X and Y coordinate in the board where we need to change state
     * @throws ParameterException if coordinates are incorrect
     */
    public void makeMove(Coordinates coordinates) {
        checkParameters(coordinates);

        boardData.get(coordinates.yCord()).set(coordinates.xCord(), playerTurn);
        positionsAvailable.remove(coordinates);

        checkDraw();
        checkWinnerOfGame();

        playerTurn = getNextPlayerTurn(playerTurn);
    }

    private static PlayerState getNextPlayerTurn(PlayerState playerTurn) {
        return playerTurn == PlayerState.X ? PlayerState.O : PlayerState.X;
    }

    private void checkWinnerOfGame() {
        if (hasWinner()) {
            winner = playerTurn.convertBoardStateToWinnerState();
            gameState = GameState.FINISHED;
        }
    }

    private void checkDraw() {
        if (hasNoPositionsOnBoard()) {
            winner = WinnerState.DRAW;
            gameState = GameState.FINISHED;
        }
    }

    public boolean isPositionEmpty(Coordinates move) {
        return boardData.get(move.yCord()).get(move.xCord()) == PlayerState.EMPTY;
    }

    public boolean isGameFinished() {
        return gameState == GameState.FINISHED;
    }

    /**
     * function for checking winner of game
     * @return if there is a winner in the current move or not
     */
    public boolean hasWinner() {
        StringBuilder diagonalLine = new StringBuilder();
        StringBuilder oppositeDiagonalLine = new StringBuilder();

        for (int i = 0; i < boardSize; i++) {
            StringBuilder horizontalLine = new StringBuilder();
            for (int j = 0; j < boardSize; j++) {
                horizontalLine.append(boardData.get(i).get(j));
            }

            StringBuilder verticalLine = new StringBuilder();
            for (int j = 0; j < boardSize; j++) {
                verticalLine.append(boardData.get(j).get(i));
            }

            diagonalLine.append(boardData.get(i).get(i));
            oppositeDiagonalLine.append(boardData.get(i).get(boardSize - i - 1));
            if (hasWinnerInLine(horizontalLine.toString()) || hasWinnerInLine(verticalLine.toString())) {
                return true;
            }
        }
        return hasWinnerInLine(diagonalLine.toString()) || hasWinnerInLine(oppositeDiagonalLine.toString());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getBeginOrEndLine());

        for (int i = 0; i < boardSize; i++) {
            result.append("|");

            for (int j = 0; j < boardSize; j++) {
                String currentData = boardData.get(i).get(j).toString();

                result.append(" ")
                        .append(currentData)
                        .append(" |");
            }

            result.append("\n");

            if (i != boardSize - 1) {
                result.append(getLineSeparator());
            }
        }

        result.append(getBeginOrEndLine());
        return result.toString();
    }

    private String getBeginOrEndLine() {
        return "|" +
                "---|".repeat(boardSize) +
                "\n";
    }

    private String getLineSeparator() {
        return "|" +
                "----".repeat(boardSize - 1) +
                "---|\n";
    }

    private boolean hasWinnerInLine(String line) {
        String currentPlayerWinComb = playerTurn.name().repeat(boardSize);
        return line.equals(currentPlayerWinComb);
    }

    private void initBoard(int fieldSize) {

        this.boardSize = fieldSize;

        boardData = new ArrayList<>(fieldSize);
        positionsAvailable = new HashSet<>(fieldSize);

        for (int i = 0; i < fieldSize; i++) {
            boardData.add(new ArrayList<>(boardData.size()));
        }

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                boardData.get(i).add(PlayerState.EMPTY);
            }
        }

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                positionsAvailable.add(new Coordinates(i, j));
            }
        }
    }

    private void checkParameters(Coordinates coordinates) {
        if (areCoordinatesIncorrect(coordinates)) {
            throw new ParameterException("Error: Coordinates are incorrect. Try again!");
        }
        if (!isPositionEmpty(coordinates)) {
            throw new ParameterException("Error: This position is not empty. Repeat your choice");
        }
        if (isGameFinished()) {
            throw new ParameterException("Error: TicTacToe is finished.");
        }
    }

    public boolean areCoordinatesIncorrect(Coordinates coordinates) {
        return coordinates.xCord() >= boardSize || coordinates.yCord() >= boardSize || coordinates.yCord() < 0 || coordinates.xCord() < 0;
    }

    private boolean hasNoPositionsOnBoard() {
        return positionsAvailable.isEmpty();
    }
}
