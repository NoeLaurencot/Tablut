package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.*;

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
        baseBoard = stage.getBoard();

        int teamId = model.getIdPlayer();
        if (teamId == 0) currentTeam = Team.SWEDISH;
        else currentTeam = Team.MUSCOVITES;

        List<Move> moves = baseBoard.computeAllValidCells(currentTeam);

        return simpleStrategy(baseBoard, moves);
    }

    private ActionList simpleStrategy(TablutBoard board, List<Move> moves) {
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
        int nSourceEnemyNeighbors = pawn.countEnemyNieghbors(pawn.getNeighbors(move.sourceX, move.sourceY), team);
        int nDestEnemyNeighbors = pawn.countEnemyNieghbors(pawn.getNeighbors(move.destX, move.destY), team);

//        System.out.println("SWEDISH MOVE: " + move.humanReadable());
//        System.out.println("score 1 : " + score);

        if (pawn instanceof King) {
            double distanceScore = 0;

            // Gets closer to win coordinates
            for (Coordinate winCoord : winCoords) {
                int destDist = Math.abs(winCoord.getX() - move.destX) + Math.abs(winCoord.getY() - move.destY);

                distanceScore = destDist * 500;

                if (distanceScore > bestDistanceScore) {
                    bestDistanceScore = distanceScore;
                }
            }

            score += (int) bestDistanceScore;

//            System.out.println("score 2 : " + score);

            // Max score if winning move
            for (Coordinate winCoord : board.getWinCoords()) {
                if (winCoord.getX() == move.destX && winCoord.getY() == move.destY) {
                    return Integer.MAX_VALUE;
                }
            }
        } else {
            if (((Soldier) pawn).isCellCapturableNextMove(new Coordinate(move.sourceX, move.sourceY), new Coordinate(move.destX, move.destY), team)) {
                score -= 900;
            }
        }

//        System.out.println("score 3 : " + score);

        // If can capture an enemy pawn
        if (pawn.wouldCapture(move.destX, move.destY, team))
            score += 900;
        // if can capture an enemy pawn next turn
        else if (pawn.isPawnCapturingAdjcntPawnNextMove(new Coordinate(move.destX, move.destY), team))
            score += 600;

//        System.out.println("score 4 : " + score);

        // To move away from king's path
        if (pawn.canSeeKing(move.sourceX, move.sourceY) && !pawn.canSeeKing(move.destX, move.destY))
            score += 300;
        else if ((move.sourceX == king.getXInBoard() || move.sourceY == king.getYInBoard()) && (move.destX != king.getXInBoard() || move.destY != king.getYInBoard()))
            score += 100;

//        System.out.println("score 5 : " + score);

        // Don't go in surrounded cells
        if (nSourceEnemyNeighbors == 0)
            score -= 100 * nDestEnemyNeighbors;

//        System.out.println("final score : " + score);

        return score;
    }

    private int evaluateMuscovites(TablutBoard board, Move move, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        King king = board.findKing();
        int nSourceEnemyNeighbors = pawn.countEnemyNieghbors(pawn.getNeighbors(move.sourceX, move.sourceY), team);
        int nDestEnemyNeighbors = pawn.countEnemyNieghbors(pawn.getNeighbors(move.destX, move.destY), team);

//        System.out.println("MUSCOVITE MOVE: " + move.humanReadable());
//        System.out.println("score 1 : " + score);

        // If can capture an enemy pawn
        if (pawn.wouldCapture(move.destX, move.destY, team))
            score += 900;
        else if (pawn.isPawnCapturingAdjcntPawnNextMove(new Coordinate(move.destX, move.destY), team))
            score += 700;

//        System.out.println("score 1 : " + score);

        // To block king's possible path without going close to it
        if (!pawn.canSeeKing(move.sourceX, move.sourceY) && pawn.canSeeKing(move.destX, move.destY)) {
            score += 300 - 1000 / (Math.abs(king.getXInBoard() - move.destX) + Math.abs(king.getYInBoard() - move.destY));
        } else if ((move.sourceX != king.getXInBoard() || move.sourceY != king.getYInBoard()) && (move.destX == king.getXInBoard() || move.destY == king.getYInBoard())) {
            score += 100;
        }

//        System.out.println("score 2 : " + score);

        if (((Soldier) pawn).isCellCapturableNextMove(new Coordinate(move.sourceX, move.sourceY), new Coordinate(move.destX, move.destY), team)) {
            score -= 900;
        }

//        System.out.println("score 3 : " + score);

        // Don't go in surrounded cells
        if (nSourceEnemyNeighbors == 0)
            score -= 100 * nDestEnemyNeighbors;

//        System.out.println("final score : " + score);

        return score;
    }
}
