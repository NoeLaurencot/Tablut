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

    private Map<String, SmallTablutBoard> seenBoards;

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private KingMoveMode kingMode;
    public Move bestMove;

    public TablutDecider(Model model, Controller control) {
        super(model, control);
        getGameParameters();
    }

    private void getGameParameters() {
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
        int bestScore = -2000;
        List<Move> moves = board.computeAllValidCells(currentTeam);
        for (Move move : moves) {
            if (teamId == 0) currentTeam = Team.SWEDISH;
            else currentTeam = Team.MUSCOVITES;

            SmallTablutBoard newBoard = new SmallTablutBoard(board, kingMode);
            newBoard.applyMove(move);
            //newBoard.print();
            int score = minmax(newBoard, depth, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;

            }
            //System.out.println("score " + score);

            //currentTeam = currentTeam.oppositeTeam();
        }
        pawn = (Pawn) board.getElement(bestMove.sourceX, bestMove.sourceY);

        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, bestMove.destX, bestMove.destY);
        actions.setDoEndOfTurn(true);
        return actions;
    }

    private int minmax(SmallTablutBoard board, int d, boolean maxing) {
        String id = board.toString();
        if (seenBoards.containsKey(id)) {
            //System.out.println("s " + seenBoards.get(id).getScore());
            return seenBoards.get(id).getScore();
        }
        if (d == 0) {
            //System.out.println("e " + evaluate(board));
            seenBoards.put(id, board);
            return evaluate(board);
        }

        if (maxing) {
            currentTeam = Team.MUSCOVITES;
        } else {
            currentTeam = Team.SWEDISH;
        }
        List<Move> moves = board.computeAllValidCells(currentTeam);

        int bestScore = (maxing) ? -1500 : 1500;
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
            //System.out.println(currentTeam);
            if (maxing) {
                if (score > bestScore) {
                    bestScore = score;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                }
            }
        }

        return bestScore;
    }


    private int evaluate(SmallTablutBoard board) {
        if (winSwedish(board)) return -1000;
        if (winMuscovites(board)) return 1000;
    
        return 0;
    }

    private boolean winSwedish(SmallTablutBoard board) {
        for (Coordinate pos : winCoords) {
            if (board.getGrid()[pos.getX()][pos.getY()] == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean winMuscovites(SmallTablutBoard board) {
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
