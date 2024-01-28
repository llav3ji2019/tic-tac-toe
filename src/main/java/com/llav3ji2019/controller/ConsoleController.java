package com.llav3ji2019.controller;

import com.llav3ji2019.algoritm.AlphaBetaPruning;
import com.llav3ji2019.state.PlayerState;
import com.llav3ji2019.structure.Coordinates;

import java.util.Scanner;

public class ConsoleController {
    private Board board;
    private final Scanner input = new Scanner(System.in);

    public ConsoleController() {
        reset();
    }

    public void startGame() {
        while (true) {
            play();

            System.out.println("\nWould you like to continue?(default=Y)[Y/n]");
            String continueLetter = input.next();
            if (continueLetter.equalsIgnoreCase("n")) {
                break;
            }
            reset();
        }
    }

    private void play() {
        System.out.println("Starting a new game");

        System.out.println("Would you like to start first?(default=Y)[Y/n]");
        if (input.next().equalsIgnoreCase("n")) {
            changeMovesOrder();
        }

        while (!board.isGameFinished()) {
            printGameStatus();
            makeNextMove();
        }
        printWinner();
    }

    private void changeMovesOrder() {
        board.setPlayerTurn(PlayerState.X);
    }

    private void makeNextMove() {
        if (board.getPlayerTurn() == PlayerState.X) {
            AlphaBetaPruning.start(board.getPlayerTurn(), board);
        } else {
            chooseNextPlayerPosition();
        }
    }

    private void chooseNextPlayerPosition() {
        while (true) {
            System.out.println("Please, write x and y (Use whitespace or enter between numbers):");
            int xCord = input.nextInt();
            int yCord = input.nextInt();
            Coordinates currentMove = new Coordinates(xCord - 1, yCord - 1);

            if (areCoordinatesCorrect(currentMove)) {
                board.makeMove(currentMove);
                break;
            }
        }
    }

    private boolean areCoordinatesCorrect(Coordinates coordinates) {
        return !board.areCoordinatesIncorrect(coordinates) && board.isPositionEmpty(coordinates) && !board.isGameFinished();
    }

    private void printGameStatus() {
        System.out.println("\nCurrent Board state:");
        System.out.println(board);
        System.out.println("It is turn of " + board.getPlayerTurn().name() + "\n");
    }

    private void printWinner() {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getWinner());
    }

    private void reset() {
        System.out.println("Enter the field size: ");
        int boardSize = -1;
        while (!isBoardSizeCorrect(boardSize)) {
            boardSize = input.nextInt();
            if (isBoardSizeCorrect(boardSize)) {
                board = new Board(boardSize);
            }
        }
    }

    private static boolean isBoardSizeCorrect(int boardSize) {
        return boardSize > 2;
    }
}
