package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import model.*;

import java.util.List;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a Hole pawn is fixed.
 */
public class Fortress extends GameElement {

    public Fortress(double x, double y, GameStageModel gameStageModel) {
        super(gameStageModel);
        setLocation(x, y);
    }

    public Fortress(GameStageModel gameStageModel) {
        this(0, 0, gameStageModel);
    }
}
