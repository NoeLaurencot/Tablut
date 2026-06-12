package model;

import boardifier.model.GameStageModel;
import boardifier.model.TextElement;

public class PlayerName extends TextElement {
    public PlayerName(double x, double y, String text, GameStageModel gameStageModel) {
        super(text, gameStageModel);
        setLocation(x, y);
    }
}
