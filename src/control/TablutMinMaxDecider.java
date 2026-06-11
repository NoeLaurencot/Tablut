package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;

import model.*;

import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class TablutMinMaxDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int depth = 3;
    private static final int depthDifferenceAcceptance = 0;

    private Map<String, Integer> seenBoards;

    private List<CoordinateByte> winCoords;
    private Team currentTeam;
    private KingMoveMode kingMode;
    public Move bestMove;

    public TablutMinMaxDecider(Model model, Controller control) {
        super(model, control);
        getGameParameters();
    }

    private void getGameParameters() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();

        this.kingMode = TablutOptionModel.getKingMode();

        this.winCoords = new ArrayList<>();
        for (Coordinate coord : board.getWinCoords()) {
            this.winCoords.add(new CoordinateByte(coord));
        }
    }

    @Override
    public ActionList decide() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();
        Pawn pawn = null;
        int rowDest = 0;
        int colDest = 0;

        seenBoards = new HashMap();
        int teamId = model.getIdPlayer();

        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        boolean maxing = currentTeam == Team.MUSCOVITES;
        int bestScore = (maxing) ? -20000 : 20000;

        int alpha = -20000;
        int beta = 20000;
        SmallTablutBoard newBoard = new SmallTablutBoard(board, kingMode);
        List<MoveByte> moves = newBoard.computeAllValidCells(currentTeam);
        for (MoveByte move : moves) {
            if (teamId == 0) currentTeam = Team.SWEDISH;
            else currentTeam = Team.MUSCOVITES;

            newBoard = new SmallTablutBoard(board, kingMode);
            newBoard.applyMove(move);

            int score = minmax(newBoard, depth, alpha, beta, !maxing);
            score -= score % 10;

            if (maxing) {
                // beta

                if (score > bestScore) {
                    System.out.println(score + " " + move);
                    bestScore = score;
                    bestMove = new Move(move);

                    alpha = score;
                }
            } else {
                // alpha

                if (score < bestScore) {
                    System.out.println(score + " " + move);
                    bestScore = score;
                    bestMove = new Move(move);

                    beta = score;
                }
            }

            if (bestScore == ((maxing) ? 11000 : -11000)) {
                break;
            }
            //System.out.println("score " + score);

            //currentTeam = currentTeam.oppositeTeam();
        }
        pawn = (Pawn) board.getLastElement(bestMove.sourceX, bestMove.sourceY);

        ActionList actions = ActionFactory.generateMoveWithinContainer(control, model, pawn, (int)bestMove.destX, (int)bestMove.destY, AnimationTypes.MOVE_LINEARPROP, 10);
        actions.setDoEndOfTurn(true);
        return actions;
    }

    public int minmax(SmallTablutBoard board, int d, int alpha, int beta, boolean maxing) {

        int quick = smallEvaluate(board);
        if (quick != 0) return quick;

        String id = board.toString();
        if (seenBoards.containsKey(id)) {
            int score = seenBoards.get(id);
            if (Math.abs(d - score % 10) <= depthDifferenceAcceptance) {
                return score;
            }
            //System.out.println("s " + seenBoards.get(id));
        }

        if (d == 0) return evaluate(board);

        Team team = maxing ? Team.MUSCOVITES : Team.SWEDISH;
        List<MoveByte> moves = board.computeAllValidCells(team);

        int bestScore = maxing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (MoveByte move : moves) {
            SmallTablutBoard newBoard = new SmallTablutBoard(board, kingMode);
            newBoard.applyMove(move);

            int score = minmax(newBoard, d - 1, alpha, beta, !maxing);
            score -= score % 10;

            if (bestScore == ((maxing) ? 11000 : -11000)) return score;

            if (maxing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
                if (bestScore >= beta) break;
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
                if (bestScore <= alpha) break;
            }
        }
        return bestScore;
    }


    private int smallEvaluate(SmallTablutBoard board) {
        if (winSwedish(board)) return -11000;
        if (winMuscovites(board)) return 11000;

        short[][] grid = board.getGrid();
        CoordinateByte pos = board.getKingPosition();

        // Tuichi
        List<CoordinateByte> kingMoves = board.computeKingValidCells(pos.getX(), pos.getY());
        int winningPositions = 0;
        for (CoordinateByte move : kingMoves) {
            for (CoordinateByte coord : winCoords) {
                if (move.equals(coord)) {
                    winningPositions ++;
                }
            }
        }

        if (winningPositions >= 2) {
            return -10000;
        }
        
        return 0;
    }

    private int evaluate(SmallTablutBoard board) {
        if (winSwedish(board)) return -11000;
        if (winMuscovites(board)) return 11000;

        short[][] grid = board.getGrid();
        CoordinateByte pos = board.getKingPosition();
        List<CoordinateByte> kingMoves = board.computeKingValidCells(pos.getX(), pos.getY());

        // Tuichi
        int winningPositions = 0;
        for (CoordinateByte move : kingMoves) {
            for (CoordinateByte coord : winCoords) {
                if (move.equals(coord)) {
                    winningPositions ++;
                }
            }
        }

        if (winningPositions >= 2) {
            return -10000;
        }

        int score = 0;

        // Captures

        
        for (int i = 0 ; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                short cell = grid[i][j];
                if (cell == 1) {
                    score -= 30;
                } else if (cell == 2) {
                    score += 60;
                }
            }
        }

        // King freedom

        score -= kingMoves.size() * 10;

        // King close to the border
        score -= Math.abs(4-pos.getX()) * 10;
        score -= Math.abs(4-pos.getY()) * 10;

        // Soldiers around the king
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            if (pos.getX() + vect[0] < 0 || pos.getX() + vect[0] > 8) continue;
            if (pos.getY() + vect[1] < 0 || pos.getY() + vect[1] > 8) continue;

            if (grid[pos.getX() + vect[0]][pos.getY() + vect[1]] == 2) {
                score += 40;
            }
            else if (grid[pos.getX() + vect[0]][pos.getY() + vect[1]] == 1) {
                score += 20;
            }
        }
        int[][] diagonals = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] vect : diagonals) {
            if (pos.getX() + vect[0] < 0 || pos.getX() + vect[0] > 8) continue;
            if (pos.getY() + vect[1] < 0 || pos.getY() + vect[1] > 8) continue;

            if (grid[pos.getX() + vect[0]][pos.getY() + vect[1]] == 2) {
                score += 20;
            }
            else if (grid[pos.getX() + vect[0]][pos.getY() + vect[1]] == 1) {
                score += 10;
            }
        }

        
        return score;
    }

    private boolean winSwedish(SmallTablutBoard board) {
        for (CoordinateByte pos : winCoords) {
            if (board.getGrid()[pos.getX()][pos.getY()] == 3) {
                return true;
            }
        }
        return false;
    }

    public boolean winMuscovites(SmallTablutBoard board) {
        short[][] grid = board.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 3) {
                    return false;
                }
            }
        }
        return true;
    }
}
