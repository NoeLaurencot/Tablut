package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TablutSimpleDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private static final int depth = 2;

    private List<TablutBoard> seenBoards;
    private TablutController t;

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private TablutBoard baseBoard;
    public Move bestMove;

    public TablutSimpleDecider(Model model, Controller control) {
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

        int teamId = model.getIdPlayer();
        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        List<Move> moves = baseBoard.computeAllValidCells(currentTeam);

        return simpleStrategy(baseBoard, moves);
    }

    private ActionList simpleStrategy(TablutBoard board, List<Move> moves) {
        ActionList actionList;
        Move bestMove = moves.getFirst();
        GameElement pawn = board.getElement(moves.getFirst().sourceX, moves.getFirst().sourceY);
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;

        for (Move move : moves) {

            pawn = board.getElement(move.sourceX, move.sourceY);
            score = simpleEvaluate(board, move);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        actionList = ActionFactory.generateMoveWithinContainer(model, pawn, bestMove.destX, bestMove.destY);
        actionList.setDoEndOfTurn(true);

        return actionList;
    }

    private int simpleEvaluate(TablutBoard board, Move move) {
        int score = 0;

        GameElement pawn = board.getElement(move.sourceX, move.sourceY);

        ArrayList<GameElement> sourceNeighbors = board.getNeighbors(new Coordinate(move.sourceX, move.sourceY));
        ArrayList<GameElement> destNeighbors = board.getNeighbors(new Coordinate(move.destX, move.destY));

        score += board.countEnemyNieghbors(sourceNeighbors, ((Pawn) pawn).getTeam()) - board.countEnemyNieghbors(destNeighbors, ((Pawn) pawn).getTeam());
        return 0;
    }
}
