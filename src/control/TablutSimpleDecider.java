package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;
import model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TablutSimpleDecider extends Decider {

    private final int MAX_HISTORY = 64;

    private static ArrayList<Move> moveHistory = new ArrayList<>();
    private static int repetitiveScore;

    private final int BIG_SCORE = 1000000;

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    private List<Coordinate> winCoords;
    private Team currentTeam;
    private TablutBoard board;
    private final int COOLDOWN = 500; // cooldown in ms, don't put more than ~3s as the os thinks the app isn't responding
    public Move bestMove;

    public TablutSimpleDecider(Model model, Controller control) {
        super(model, control);

        applyGameParameters();
    }

    public void applyGameParameters() {
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

    public ActionList simpleStrategy(List<Move> moves) {
        ActionList actionList;
        bestMove = moves.getFirst();
        Pawn pawn = (Pawn) board.getElement(moves.getFirst().sourceX, moves.getFirst().sourceY);
        Pawn bestPawn = pawn;
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;

        long timestamp = System.currentTimeMillis();

        for (Move move : moves) {

            pawn = (Pawn) board.getLastElement(move.sourceX, move.sourceY);
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

        TablutController.waitCooldown(COOLDOWN, timestamp);

        addMoveToHistory(bestMove);
        repetitiveScore = computeRepetitiveScore();

//        System.out.println("Repetition score: " + repetitiveScore);
//        System.out.println("Best move: " + bestMove.humanReadable());
        actionList = ActionFactory.generateMoveWithinContainer(control, model, bestPawn, bestMove.destX, bestMove.destY, AnimationTypes.MOVE_LINEARPROP, 10);

        ((TablutController) control).addAfterPlayActions(bestMove, actionList);
        ((TablutController) control).setLastMove(bestMove);

        actionList.setDoEndOfTurn(true);

        return actionList;
    }

    public int evaluateSwedish(TablutBoard board, Move move, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        King king = board.findKing();
        int kingX = king.getXInBoard();
        int kingY = king.getYInBoard();
        Coordinate sourceCoord = new Coordinate(move.sourceX, move.sourceY);
        Coordinate destCoord = new Coordinate(move.destX, move.destY);

        if (pawn instanceof King) {
            // Max score if winning move
            for (Coordinate winCoord : board.getWinCoords()) {
                if (winCoord.getX() == move.destX && winCoord.getY() == move.destY) {
                    return Integer.MAX_VALUE;
                }
            }

            score += (board.countEnemyNeighbors(sourceCoord, team) - board.countEnemyNeighbors(destCoord, team)) * 200;

            // Goes to square where the most visible borders
            score += board.countVisibleBorder(destCoord) * 100 * repetitiveScore;

            // Gets closer to win coordinates
            int minDist = Integer.MAX_VALUE;
            for (Coordinate winCoord : winCoords) {
                int d = Math.abs(winCoord.getX() - move.destX) + Math.abs(winCoord.getY() - move.destY);
                if (d < minDist) minDist = d;
            }
            score += (10 - minDist) * 80;

            if (repetitiveScore > 1) {
                score += sourceCoord.computeManhattanDistance(destCoord) * 300 * repetitiveScore;
            }
        }

        score += evalCellFavorability(sourceCoord, destCoord, pawn);

        score -= evalCellDangerosity(sourceCoord, destCoord, pawn);

        // To move away from king's path
        if (board.canSeeKing(sourceCoord) && !board.canSeeKing(destCoord))
            score += 300;
        else if ((move.sourceX == kingX || move.sourceY == kingY) && (move.destX != kingX || move.destY != kingY))
            score += 100;

        return score;
    }

    public int evaluateMuscovites(TablutBoard board, Move move, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        King king = board.findKing();
        int kingX = king.getXInBoard();
        int kingY = king.getYInBoard();
        Coordinate sourceCoord = new Coordinate(move.sourceX, move.sourceY);
        Coordinate destCoord = new Coordinate(move.destX, move.destY);

        // Max score if captures the king
        if (king.wouldBeSurroundedIfEnemyAt(new Coordinate(move.destX, move.destY))) {
            return Integer.MAX_VALUE;
        }

        score += evalCellFavorability(sourceCoord, destCoord, pawn);

        score -= evalCellDangerosity(sourceCoord, destCoord, pawn);

        // To move in the king's path
        if (!board.canSeeKing(sourceCoord) && isBetweenKingAndWinCoords(destCoord, kingX, kingY)) {
            score += BIG_SCORE;
        } else if (TablutOptionModel.getWinMode() == WinMode.CORNERS && !board.canSeeKing(sourceCoord)
                && isBetweenKingAndBorders(destCoord, kingX, kingY)) {
            score += 400;
        } else if (!board.canSeeKing(sourceCoord) && board.canSeeKing(destCoord)) {
            score += 100;
        } else if ((move.sourceX != kingX || move.sourceY != kingY) && (move.destX == kingX || move.destY == kingY))
            score += 50;

        // to stay on the king's path
        if (isBetweenKingAndBorders(sourceCoord, kingX, kingY) && !isBetweenKingAndBorders(destCoord, kingX, kingY)) {
            return Integer.MIN_VALUE;
        }

        // If goes next to king, more score if already allies surrounding it
        if (!isNextToKing(sourceCoord, new Coordinate(kingX, kingY)) && isNextToKing(destCoord, new Coordinate(kingX, kingY))) {
            int kingAdjAlly = board.countEnemyNeighbors(new Coordinate(kingX, kingY), team.oppositeTeam());
            score += 300 * (kingAdjAlly + 1) * repetitiveScore;
        }

        return score;
    }

    public int evalCellDangerosity(Coordinate soureCoord, Coordinate destCoord, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;

        // also reverse to check pawns that don't move (so the new destCoord would be where it is now)
        if (canBeCapturedNextTurn(soureCoord, destCoord, pawn)) {
            if (pawn instanceof King) {
                score += BIG_SCORE;
            } else {
                score += 800;
            }
        }

        return score;
    }

    public int evalCellFavorability(Coordinate sourceCoord, Coordinate destCoord, Pawn pawn) {
        Team team = pawn.getTeam();
        int score = 0;
        Move move = new Move(sourceCoord.getX(), sourceCoord.getY(), destCoord.getX(), destCoord.getY());

        // 400 score per capture, increasing with repetitive score
        score += (int) (board.captureCount(destCoord, team) * 400 * Math.max((Math.log(repetitiveScore) + 1), 1));

        // 200 score per capture able next turn, increasing with repetitive score
        int test = (int) (countCapturableNextTurn(destCoord, pawn) * 200 * Math.max((Math.log(repetitiveScore) + 1), 1));
        score += test;

        // Doesn't go back to visited square in the last 8 moves (16 / 2)
        if (repetitiveScore > 8 && moveHistory.subList(0, Math.min(16, moveHistory.size() - 1)).contains(move))
            score -= BIG_SCORE;

        return score;
    }

    public boolean isBetweenKingAndBorders(Coordinate coord, int kingX, int kingY) {

        Coordinate kingCoord = new Coordinate(kingX, kingY);

        // If the king is surrouned, no need to check
        if (board.getEmptyNeighbors(kingCoord).isEmpty()) {
            return false;
        }

        ArrayList<Coordinate> borders = new ArrayList<>();
        int[] colBorders = {0, board.getNbCols() - 1};
        int[] rowBorders = {0, board.getNbRows() - 1};

        // Add the horizontal border points
        for (int col : colBorders) {
            borders.add(new Coordinate(kingX, col));
        }

        // Add the vertical border points
        for (int row : rowBorders) {
            borders.add(new Coordinate(row, kingY));
        }

        for (Coordinate winCoord : borders) {

            // Checks if the king, coord, and winCoord on the exact same line
            boolean isSameRow = (coord.getX() == kingX && coord.getX() == winCoord.getX());
            boolean isSameCol = (coord.getY() == kingY && coord.getY() == winCoord.getY());

            // If they aren't in a line, it can't possibly be between them.
            if (!isSameRow && !isSameCol) {
                continue;
            }

            if (!board.isEmptyAt(winCoord.getX(), winCoord.getY())) {
                continue;
            }

            // Checks line of sight
            boolean coordCanSeeWin = board.canSeeCoordinate(coord, winCoord);
            boolean coordCanSeeKing = board.canSeeKing(coord);

            if (coordCanSeeWin && coordCanSeeKing)
                return true;
        }

        return false;
    }

    public boolean isBetweenKingAndWinCoords(Coordinate coord, int kingX, int kingY) {
        Coordinate kingCoord = new Coordinate(kingX, kingY);

        // If the king is surrouned, no need to check
        if (board.getEmptyNeighbors(kingCoord).isEmpty()) {
            return false;
        }

        for (Coordinate winCoord : this.winCoords) {

            // Checks if the king, coord, and winCoord on the exact same line
            boolean isSameRow = (coord.getX() == kingX && coord.getX() == winCoord.getX());
            boolean isSameCol = (coord.getY() == kingY && coord.getY() == winCoord.getY());

            // If they aren't in a line, it can't possibly be between them.
            if (!isSameRow && !isSameCol) {
                continue;
            }

            if (!board.isEmptyAt(winCoord.getX(), winCoord.getY())) {
                continue;
            }

            // Checks line of sight
            boolean coordCanSeeWin = board.canSeeCoordinate(coord, winCoord) || winCoord.equals(coord);
            boolean coordCanSeeKing = board.canSeeKing(coord);

            if (coordCanSeeWin && coordCanSeeKing)
                return true;
        }

        return false;
    }

    public boolean canBeCapturedNextTurn(Coordinate sourceCoord, Coordinate destCoord, Pawn pawn) {
        Team team = pawn.getTeam();

        if (board.countEnemyNeighbors(destCoord, team) == 0) {
            return false;
        }

        ArrayList<Coordinate> emptyNeighbors = new ArrayList<>(board.getEmptyNeighbors(destCoord));
        // if source is adjacent to dest it adds it to empty neighbo list
        if (Math.abs(sourceCoord.getX() - destCoord.getX()) + Math.abs(sourceCoord.getY() - destCoord.getY()) == 1) {
            boolean alreadyIncluded = false;
            for (Coordinate coord : emptyNeighbors) {
                if (coord.equals(sourceCoord)) {
                    alreadyIncluded = true;
                    break;
                }
            }
            if (!alreadyIncluded) {
                emptyNeighbors.add(sourceCoord);
            }
        }

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        for (Coordinate emptyCoord : emptyNeighbors) {
            if (!board.wouldBeSurroundedIfEnemyAt(destCoord, emptyCoord, pawn)) {
                continue;
            }

            // checks if there is an enemy that can go to the empty adjacent cell next turn
            for (int[] vect : directions) {
                Coordinate cursor = new Coordinate(emptyCoord);

                while (true) {
                    cursor.increment(vect[0], vect[1]);

                    if (board.outOfBounds(cursor)) {
                        break;
                    }
                    if (cursor.equals(destCoord)) {
                        // our pawn blocks the path after the move
                        break;
                    }
                    if (cursor.equals(sourceCoord)) {
                        // treat source as empty after the move
                        continue;
                    }
                    if (board.isEmptyAt(cursor.getX(), cursor.getY())) {
                        continue;
                    }

                    GameElement blocker = board.getElement(cursor.getX(), cursor.getY());
                    if (blocker instanceof Pawn && ((Pawn) blocker).getTeam() != team) {
                        return true;
                    }
                    break;
                }
            }
        }

        return false;
    }

    public int countCapturableNextTurn(Coordinate coord, Pawn pawn) {
        Team team = pawn.getTeam();
        int counter = 0;

        for (Coordinate enemyCoord : board.getAdjacentEnemyPawnsCoords(coord, team)) {
            Coordinate acrossCoord = new Coordinate(
                    enemyCoord.getX() * 2 - coord.getX(),
                    enemyCoord.getY() * 2 - coord.getY()
            );
            if (!board.outOfBounds(acrossCoord) && board.isAnyEnemyVisible(acrossCoord, team))
                counter++;
        }

        return counter;
    }

    public void addMoveToHistory(Move move) {
        if (moveHistory.size() == MAX_HISTORY) moveHistory.removeLast(); // oldest
        moveHistory.addFirst(move); // newest
    }

    /**
     * Determines a score that tells how much a move pattern have repeated, helps unstuck infinite games between 2 AIs
     *
     * @return the repetition score. Higher is more repetitive, default is 1
     */
    public int computeRepetitiveScore() {
        final int PATTERN_SIZE = 4;
        Move[] repeatPattern = new Move[PATTERN_SIZE];

        if (MAX_HISTORY < PATTERN_SIZE) {
            System.out.println("Max history size smaller than pattern size");
            return 1;
        }

        if (moveHistory.size() < PATTERN_SIZE) {
            return 1;
        }

        int repeatingCnt = 0;

        for (int i = 0; i < PATTERN_SIZE; i++) {
            repeatPattern[i] = moveHistory.get(i);
        }
        for (int i = PATTERN_SIZE; i <= moveHistory.size() - 1; i++) {
            if (repeatPattern[i % PATTERN_SIZE].equals(moveHistory.get(i))) {
                repeatingCnt++;
            } else {
                break;
            }
        }
        return repeatingCnt + 1;
    }

    public boolean isNextToKing(Coordinate coord, Coordinate kingCoord) {
        return Math.abs(coord.getX() - kingCoord.getX()) + Math.abs(coord.getY() - kingCoord.getY()) == 1;
    }
}
