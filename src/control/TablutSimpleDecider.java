package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
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
    private TablutBoard board;
    public Move bestMove;

    private double bestDistanceScore = 0;

    public TablutSimpleDecider(Model model, Controller control) {
        super(model, control);
        t = (TablutController) control;

        applyGameParameters();
    }

    private void applyGameParameters() {
        TablutStageModel stage = (TablutStageModel) model.getGameStage();
        TablutBoard board = stage.getBoard();

        this.winCoords = board.getWinCoords();
    }

    @Override
    public ActionList decide() {
        TablutStageModel stage = (TablutStageModel) model.getGameStage();
        board = stage.getBoard();

        int teamId = model.getIdPlayer();
        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        List<Move> moves = board.computeAllValidCells(currentTeam);

        return simpleStrategy(moves);
    }

    private ActionList simpleStrategy(List<Move> moves) {
        ActionList actionList;
        bestMove = moves.getFirst();
        Pawn pawn = (Pawn) board.getElement(moves.getFirst().sourceX, moves.getFirst().sourceY);
        Pawn bestPawn = pawn;
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;
        bestDistanceScore = 0;

        for (Move move : moves) {

            pawn = (Pawn) board.getElement(move.sourceX, move.sourceY);
            switch (pawn.getTeam()) {
                case SWEDISH -> score = evaluateSwedish(board, move, pawn);
                case MUSCOVITES -> score = evaluateMuscovites(board, move, pawn);
            }

            if (score > bestScore) {
                bestScore = score;
                bestPawn = pawn;
                bestMove = move;
            }
        }

        System.out.println("Best move: " + bestMove.humanReadable());
        actionList = ActionFactory.generateMoveWithinContainer(model, bestPawn, bestMove.destX, bestMove.destY);
        actionList.setDoEndOfTurn(true);

        return actionList;
    }

    private int evaluateSwedish(TablutBoard board, Move move, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        int type = pawn.getType();
        King king = board.findKing();
        int nSourceEnemyNeighbors = pawn.countEnemyNeighbors(pawn.getNeighbors(move.sourceX, move.sourceY), team);
        int nDestEnemyNeighbors = pawn.countEnemyNeighbors(pawn.getNeighbors(move.destX, move.destY), team);

        if (pawn instanceof King) {
            double distanceScore = 0;

            // Max score if winning move
            for (Coordinate winCoord : board.getWinCoords()) {
                if (winCoord.getX() == move.destX && winCoord.getY() == move.destY) {
                    return Integer.MAX_VALUE;
                }
            }

            // Gets closer to win coordinates
            for (Coordinate winCoord : winCoords) {
                int destDist = Math.abs(winCoord.getX() - move.destX) + Math.abs(winCoord.getY() - move.destY);

                distanceScore = destDist * 500;

                if (distanceScore > bestDistanceScore) {
                    bestDistanceScore = distanceScore;
                }
            }

            score += (int) bestDistanceScore;
        } else { // instanceof Soldier

        }

        // If can capture an enemy pawn

        // if can capture an enemy pawn next turn

        // To move away from king's path
        if (board.canSeeKing(new Coordinate(move.sourceX, move.sourceY)) && !board.canSeeKing(new Coordinate(move.destX, move.destY)))
            score += 300;
        else if ((move.sourceX == king.getXInBoard() || move.sourceY == king.getYInBoard()) && (move.destX != king.getXInBoard() || move.destY != king.getYInBoard()))
            score += 100;

        // Don't go in surrounded cells
        if (nSourceEnemyNeighbors == 0)
            score -= 100 * nDestEnemyNeighbors;

        return score;
    }

    private int evaluateMuscovites(TablutBoard board, Move move, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        King king = board.findKing();
        int kingX = king.getXInBoard();
        int kingY = king.getYInBoard();
        int nSourceEnemyNeighbors = pawn.countEnemyNeighbors(pawn.getNeighbors(move.sourceX, move.sourceY), team);
        int nDestEnemyNeighbors = pawn.countEnemyNeighbors(pawn.getNeighbors(move.destX, move.destY), team);

        // Max score if captures the king

        // Don't go in surrounded cells
        if (nSourceEnemyNeighbors == 0)
            score -= 100 * nDestEnemyNeighbors;

        return score;
    }

    private int evalCellDangerosity(Coordinate coord) {
        int score = 0;

        ArrayList<Coordinate> enemyCoords = board.getAdjacentEnemyPawnsCoords(coord);

        for (Coordinate coordEnemy : enemyCoords) {
            score += 100;
            Coordinate farCoord = board.getFartherVisibleCoord(coord, new int[]{coordEnemy.getX() - coord.getX(), coordEnemy.getY() - coord.getY()});
            if (!board.isEmptyAt(farCoord.getX(), farCoord.getY()) && ((Pawn) board.getElement(farCoord.getX(), farCoord.getY())).getTeam() != currentTeam) {

            }
        }
        return score;
    }
}
