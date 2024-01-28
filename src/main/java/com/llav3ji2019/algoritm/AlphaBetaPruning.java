package com.llav3ji2019.algoritm;

import com.llav3ji2019.controller.Board;
import com.llav3ji2019.exception.ParameterException;
import com.llav3ji2019.state.PlayerState;
import com.llav3ji2019.structure.Coordinates;

/**
 * This class is used to implement the algorithm Alpha Beta Pruning
 */
public class AlphaBetaPruning {
    private static final int WIN_MATCH_SCORE = 10;
    private static final int LOSE_MATCH_SCORE = -10;
    private static final int DRAW_MATCH_SCORE = 0;
    private static final double MAX_DEPTH = 8;

    /**
     * This function is used to configure alphaBetaPruning func
     * @param aiPlayerState value of artificial intelligence state in game (X or Y)
     * @param board value of Board
     * @throws ParameterException if parameters are not valid
     */
    public static void start(PlayerState aiPlayerState, Board board) {
        checkAIPlayerParameter(aiPlayerState);
        alphaBetaPruning(aiPlayerState, board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    /**
     * it is first part of recursive function of alpha Beta Pruning
     * @param aiPlayerState value of artificial intelligence state in game (X or Y)
     * @param board value of Board
     * @param alpha value of current alpha
     * @param beta value of current alpha
     * @param currentDepth value of current depth in recursion
     * @return score
     */
    private static int alphaBetaPruning(PlayerState aiPlayerState, Board board, int alpha, int beta, int currentDepth) {
        if (currentDepth++ == MAX_DEPTH || board.isGameFinished()) {
            return getScore(aiPlayerState, board);
        }
        return getResult(aiPlayerState, board, alpha, beta, currentDepth);
    }

    /**
     * It is function for choosing the best variant of move
     * @param aiPlayerState value of artificial intelligence state in game (X or Y)
     * @param board value of Board
     * @param alpha value of current alpha
     * @param beta value of current alpha
     * @param currentDepth value of current depth in recursion
     * @return score
     * @throws ParameterException if parameters are not valid
     */
    private static int getResult(PlayerState aiPlayerState, Board board, int alpha, int beta, int currentDepth) {
        Coordinates coordinatesOfBestMove = null;

        for (var curCoordinate : board.getPositionsAvailable()) {
            Board modifiedBoard = board.getBoardCopy();

            modifiedBoard.makeMove(curCoordinate);
            int score = alphaBetaPruning(aiPlayerState, modifiedBoard, alpha, beta, currentDepth);

            if (board.getPlayerTurn() == aiPlayerState && score > alpha) {
                alpha = score;
                coordinatesOfBestMove = curCoordinate;
            } else if (board.getPlayerTurn() != aiPlayerState && score < beta) {
                beta = score;
                coordinatesOfBestMove = curCoordinate;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (coordinatesOfBestMove != null) {
            board.makeMove(coordinatesOfBestMove);
        }
        return board.getPlayerTurn() != aiPlayerState ? alpha : beta;
    }

    /**
     * It is func for counting score
     * @param aiPlayerState value of artificial intelligence state in game (X or Y)
     * @param board value of Board
     * @return current score
     */
    private static int getScore(PlayerState aiPlayerState, Board board) {
        PlayerState opponentPlayer = (aiPlayerState == PlayerState.X) ? PlayerState.O : PlayerState.X;

        if (board.isGameFinished() && board.getWinner() == aiPlayerState.convertBoardStateToWinnerState()) {
            return WIN_MATCH_SCORE;
        } else if (board.isGameFinished() && board.getWinner() == opponentPlayer.convertBoardStateToWinnerState()) {
            return LOSE_MATCH_SCORE;
        } else {
            return DRAW_MATCH_SCORE;
        }
    }

    /**
     * func for checking state of AI
     * @param aiPlayerState value of artificial intelligence state in game (X or Y)
     * @throws ParameterException if value of aiPlayer is EMPTY
     */
    private static void checkAIPlayerParameter(PlayerState aiPlayerState) {
        if (aiPlayerState == PlayerState.EMPTY) {
            throw new ParameterException("Player must be X or O.");
        }
    }
}
