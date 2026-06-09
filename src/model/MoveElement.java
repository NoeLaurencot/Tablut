package model;

import boardifier.model.ContainerElement;
import boardifier.model.TextElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;

public class MoveElement extends TextElement {

    private Move move;

    public MoveElement(Move move, GameStageModel gameStageModel) {
        super(move.toStringHistory(), gameStageModel);
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
