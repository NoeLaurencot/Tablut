package model;

import boardifier.control.ActionFactory;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.action.ActionList;

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

    public int countEnemyNeighbors(ArrayList<GameElement> neighborList, Team team) {
        int counter = 0;
        for (GameElement gameElement : neighborList) {
            if (((Pawn) gameElement).getTeam() != team) {
                counter++;
            }
        }
        return counter;
    }

    public abstract boolean isSurrounded();


    /**
     * checks if would be surrounded, and thus captured if there was a pawn at coord
     * @param coord The coordinates the pawn would be at
     * @return true if would be surrounded
     */
    public abstract boolean wouldBeSurrounded(Coordinate coord);
}
