package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.*;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class  TablutDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int depth = 1;

    private List<TablutBoard> seenBoards;
    private TablutController t;

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private TablutBoard baseBoard;
    public Move bestMove;

    public TablutDecider(Model model, Controller control) {
        super(model, control);
        t = (TablutController) control;

        applyGameParameters();
    }

    private void applyGameParameters() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();

        this.winCoords = board.getWinCoords();
    }

    @Override
    public ActionList decide() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        baseBoard = stage.getBoard();
        baseBoard.storeGrid();
        Pawn pawn = null;
        int rowDest = 0;
        int colDest = 0;



        short[][] map = baseBoard.convertGridToInt();
        int teamId = model.getIdPlayer();
        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        int bestScore = -2000;
        List<Move> moves = baseBoard.computeAllValidCells(currentTeam);
        for (Move move : moves) {
            baseBoard.setGrid(map);
            applyMove(move);
            
            int score = minmax(baseBoard.convertGridToInt(), depth, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            currentTeam = currentTeam.oppositeTeam();
        }

        baseBoard.restoreGrid();
        pawn = (Pawn) baseBoard.getElement(bestMove.sourceX, bestMove.sourceY);

        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, bestMove.destX, bestMove.destY);
        actions.setDoEndOfTurn(true);
        //System.out.println("final " + bestMove.sourceX + " " + bestMove.sourceY + " " + bestMove.destX + " " + bestMove.destY );
        return actions;
    }

    private int minmax(short[][] grid, int d, boolean maxing) {
        

        baseBoard.setGrid(grid);
        if (d == 0) {
            return evaluate(baseBoard);
        }

        currentTeam = currentTeam.oppositeTeam();
        List<Move> moves = baseBoard.computeAllValidCells(currentTeam);

        int bestScore = (maxing) ? -2000 : 2000;
        for (Move move : moves) {
            baseBoard.setGrid(grid);
            //System.out.println(d + " move " + move.sourceX + " " + move.sourceY + " " +  move.destX + " " + move.destY + " " + currentTeam);

            applyMove(move);
            int score = minmax(baseBoard.convertGridToInt(), d-1, !maxing);
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
            //currentTeam = currentTeam.oppositeTeam();

        }

        return bestScore;
    }

    private void applyMove(Move move) {
        Pawn pawn = (Pawn) baseBoard.getElement(move.sourceX, move.sourceY);
        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, move.destX, move.destY);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, t, actions);
        play.start();
        t.afterPlay(move.destX, move.destY);
        /*
        System.out.println("---------------------");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(baseBoard.convertGridToInt()[i][j]);
            }
            System.out.println();
        }*/

    }

    private int simpleEvaluate(TablutBoard board) {
        board.computeAllValidCells(Team.SWEDISH);
        return 0;
    }

    private int evaluate(TablutBoard board) {
        if (board.areSwedishWinning()) {
            return -1000;
        } 
        if (board.areMuscovitesWinning()) return 1000;
    
        return 0;
    }

    private boolean winSwedish(short[][] board) {
        for (Coordinate pos : winCoords) {
            if (board[pos.getX()][pos.getY()] == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean winMuscovites(short[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 3) {
                    return false;
                }
            }
        }
        return true;
    }
}
