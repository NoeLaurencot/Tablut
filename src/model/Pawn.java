package model;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a pawn is fixed.
 */
public abstract class Pawn extends GameElement {

    private Team team;

    public Pawn(Team team, GameStageModel gameStageModel) {
        super(gameStageModel);
        /*
        TO FULFILL (done?):
            - register a new type of element for the pawns
            - initialize attributes
         */

        this.team = team;
    }

    public Pawn(double x, double y, Team team, GameStageModel gameStageModel) {
        super(gameStageModel);
        setLocation(x, y);
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getXInBoard() {
        TablutBoard board = ((TablutStageModel) getGameStage()).getBoard();

        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getElement(i, j) == this) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getYInBoard() {
        TablutBoard board = ((TablutStageModel) getGameStage()).getBoard();

        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getElement(i, j) == this) {
                    return j;
                }
            }
        }
        return -1;
    }

    public abstract List<Coordinate> getLegalMoves();

    protected abstract boolean checkIsMoveValid();

    public abstract boolean shouldDie();

    public ActionList move(int x, int y) {
        return ActionFactory.generateMoveWithinContainer(this.getModel(), this, x, y);
    }

    public ActionList die(Pawn pawn) {
        return ActionFactory.generateRemoveFromContainer(this.getModel(), this);
    }

    public ArrayList<GameElement> getNeighbors(int x, int y) {
        TablutBoard board = ((TablutStageModel) this.getGameStage()).getBoard();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        ArrayList<GameElement> neighborList = new ArrayList<>();
        List<GameElement> cell = board.getElements(x, y);

        if (cell == null) return neighborList;

        for (int[] vect : directions) {
            int[] position = {x + vect[0], y + vect[1]};

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (board.isEmptyAt(position[0], position[1])) continue;

            neighborList.add(board.getElement(position[0], position[1]));
        }
        return neighborList;
    }

    public int countEnemyNieghbors(ArrayList<GameElement> neighborList, Team team) {
        int counter = 0;
        for (GameElement gameElement : neighborList) {
            if (((Pawn) gameElement).getTeam() != team) {
                counter++;
            }
        }
        return counter;
    }

    public boolean canSeeKing(int x, int y) {
        TablutBoard board = ((TablutStageModel) this.getGameStage()).getBoard();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        for (int[] vect : directions) {
            int[] position = {x + vect[0], y + vect[1]};

            while (position[0] >= 0 && position[0] <= 8 && position[1] >= 0 && position[1] <= 8) {
                Pawn pawn = (Pawn) board.getElement(position[0], position[1]);
                if (!board.isEmptyAt(position[0], position[1])) {
                    if (pawn instanceof King) return true;
                    break;
                }
                position[0] += vect[0];
                position[1] += vect[1];
            }
        }
        return false;
    }

    public boolean wouldCapture(int x, int y, Team team) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutBoard board = ((TablutStageModel) this.getGameStage()).getBoard();
        TablutStageModel gameStage = (TablutStageModel) gameStageModel;
        Model model = gameStage.getModel();


        for (int[] vect : directions) {

            // first check if the next cell is an ennemy pawn
            int[] position = {x + vect[0], y + vect[1]};

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (board.isEmptyAt(position[0], position[1]))
                continue;

            Pawn ennemyPawn = (Pawn) board.getElement(position[0], position[1]);
            if (ennemyPawn.getTeam() == team)
                continue;

            // checks the further cell if it is a ally pawn (capture)
            position[0] += vect[0];
            position[1] += vect[1];

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (board.isEmptyAt(position[0], position[1]) && !gameStage.isFortressAt(position[0], position[1]))
                continue;

            Pawn otherPawn = (Pawn) board.getElement(position[0], position[1]);

            if ((gameStage.isFortressAt(position[0], position[1]) || otherPawn.getTeam() == team) && ennemyPawn.shouldDie()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPawnCapturingAdjcntPawnNextMove(Coordinate coord, Team team) {
        TablutBoard board = (TablutBoard) getContainer();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        if (this.countEnemyNieghbors(this.getNeighbors(coord.getX(), coord.getY()), team) > 0) {
            for (int[] vect : directions) {
                int[] position = {coord.getX() + vect[0], coord.getY() + vect[1]};

                if (position[0] < 0 || position[0] > 8) continue;
                if (position[1] < 0 || position[1] > 8) continue;

                if (!board.isEmptyAt(position[0], position[1]) && ((Pawn) board.getElement(position[0], position[1])).getTeam() != team) {
                    // jump accross the enemy pawn
                    position[0] += vect[0];
                    position[1] += vect[1];
                    for (int[] subVect : directions) {
                        int[] subPosition = {position[0] + subVect[0], position[1] + subVect[1]};

                        // skip the direction where the starting point is
                        if (vect[0] + subVect[0] == 0 && vect[1] + subVect[1] == 0) continue;

                        while (subPosition[0] >= 0 && subPosition[0] <= 8 && subPosition[1] >= 0 && subPosition[1] <= 8) {
                            if (!board.isEmptyAt(subPosition[0], subPosition[1])) {
                                if (((Pawn) board.getElement(subPosition[0], subPosition[1])).getTeam() == team) {
                                    return true;
                                }
                                break;
                            }
                            subPosition[0] += subVect[0];
                            subPosition[1] += subVect[1];
                        }
                    }
                }
            }
        }
        return false;
    }
}
