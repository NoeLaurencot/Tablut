package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Coordinate;
import model.TablutBoard;
import model.TablutStageModel;
import model.Pawn;
import model.Move;
import model.Team;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class  TablutDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int depth = 3;

    private List<TablutBoard> seenBoards;

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private TablutBoard baseBoard;

    public TablutDecider(Model model, Controller control) {
        super(model, control);

        getGameParameters();
    }

    private void getGameParameters() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();

        this.winCoords = board.getWinCoords();
    }

    @Override
    public ActionList decide() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        baseBoard = stage.getBoard();
        GameElement pawn = null;
        int rowDest = 0;
        int colDest = 0;

        short[][] map = baseBoard.convertGridToInt();
        int teamId = model.getIdPlayer();
        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "tablutboard", rowDest, colDest);
        actions.setDoEndOfTurn(true);

        return actions;
    }

    private int minmax(short[][] grid, int d) {
        if (d == 0) {
            return evaluate(baseBoard);
        }

        baseBoard.setGrid(grid);
        List<Move> moves = baseBoard.computeAllValidCells(currentTeam);

        for (Move move : moves) {

        }

        return 0;
    }

    private int evaluate(TablutBoard board) {
        if (board.areSwedishWinning()) return 1000;
        if (board.areMuscovitesWinning()) return -1000;
    
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
