package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Coordinate;
import model.KingMoveMode;
import model.SmallTablutBoard;
import model.TablutBoard;
import model.TablutStageModel;
import model.Pawn;
import model.Move;
import model.Team;

import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class TablutDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int depth = 3;

    private Map<String, Integer> seenBoards;

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private KingMoveMode kingMode;
    public Move bestMove;

    public TablutDecider(Model model, Controller control) {
        super(model, control);
        getGameParameters();
    }

    public void getGameParameters() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();

        this.kingMode = stage.getKingMoveMode();
        this.winCoords = board.getWinCoords();
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
        List<Move> moves = board.computeAllValidCells(currentTeam);
        for (Move move : moves) {
            if (teamId == 0) currentTeam = Team.SWEDISH;
            else currentTeam = Team.MUSCOVITES;

            SmallTablutBoard newBoard = new SmallTablutBoard(board, kingMode);
            newBoard.applyMove(move);

            int score = minmax(newBoard, depth, !maxing);
            if (maxing) {
                if (score > bestScore) {
                    //System.out.println(score + " " + move);
                    bestScore = score;
                    bestMove = move;
                }
            } else {
                if (score < bestScore) {
                    //System.out.println(score + " " + move);
                    bestScore = score;
                    bestMove = move;
                }
            }

            if (bestScore == ((maxing) ? 10000 : -10000)) {
                break;
            }
            //System.out.println("score " + score);

            //currentTeam = currentTeam.oppositeTeam();
        }
        pawn = (Pawn) board.getElement(bestMove.sourceX, bestMove.sourceY);

        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, bestMove.destX, bestMove.destY);
        actions.setDoEndOfTurn(true);
        return actions;
    }

    public int minmax(SmallTablutBoard board, int d, boolean maxing) {
        String id = board.toString();
        if (Math.abs(smallEvaluate(board)) == 10000) {
            if (d == 0) {
            //board.print();
            //System.out.println(d + " d");                
            }
            

            int score = smallEvaluate(board);
            seenBoards.put(id, score);
            return score;
        } 
        if (seenBoards.containsKey(id)) {
            int score = seenBoards.get(id);
            if (Math.abs(d - score % 10) < 2) {
                return score - score % 10;
            }
            //System.out.println("s " + seenBoards.get(id));

        }

        if (d == 0) {
            //System.out.println("e " + evaluate(board));
            int score = evaluate(board) + d;
            //board.setScore(score);
            seenBoards.put(id, score);
            return score;
        }
        

        if (maxing) {
            currentTeam = Team.MUSCOVITES;
        } else {
            currentTeam = Team.SWEDISH;
        }
        List<Move> moves = board.computeAllValidCells(currentTeam);

        int bestScore = (maxing) ? -15000 : 15000;
        for (Move move : moves) {
            if (maxing) {
                currentTeam = Team.MUSCOVITES;
            } else {
                currentTeam = Team.SWEDISH;
            }
            //System.out.println(d + " " + board);
            //System.out.println(d + " move " + move.sourceX + " " + move.sourceY + " " +  move.destX + " " + move.destY + " " + currentTeam);
            SmallTablutBoard newBoard = new SmallTablutBoard(board, kingMode);
            newBoard.applyMove(move);
            //newBoard.print();

            int score = minmax(newBoard, d-1, !maxing);
            if (maxing) {
                if (score > bestScore) {
                    bestScore = score;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                }
            }

            if (bestScore == ((maxing) ? 10000 : -10000)) {
                break;
            }
        }
        return bestScore;
    }


    public int smallEvaluate(SmallTablutBoard board) {
        if (winSwedish(board)) return -10000;
        if (winMuscovites(board)) return 10000;
        
        return 0;
    }

    public int evaluate(SmallTablutBoard board) {
        if (winSwedish(board)) return -10000;
        if (winMuscovites(board)) return 10000;

        int score = 0;

        // Captures
        for (int i = 0 ; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                short cell = board.getGrid()[i][j];
                if (cell == 1) {
                    score -= 30;
                } else if (cell == 2) {
                    score += 20;
                }
            }
        }

        // Muscovites around the king
        Coordinate pos = board.getKingPosition();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            if (pos.getX() + vect[0] < 0 || pos.getX() + vect[0] > 8) continue;
            if (pos.getY() + vect[1] < 0 || pos.getY() + vect[1] > 8) continue;

            if (board.getGrid()[pos.getX() + vect[0]][pos.getY() + vect[1]] == 2) {
                score += 20;
            }
            else if (board.getGrid()[pos.getX() + vect[0]][pos.getY() + vect[1]] == 1) {
                score += 10;
            }
        }

        
        return score;
    }

    public boolean winSwedish(SmallTablutBoard board) {
        for (Coordinate pos : winCoords) {
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
