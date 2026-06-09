package model;

import boardifier.model.ContainerElement;
import boardifier.model.TextElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;

public class MoveElement extends TextElement {

    private Move move;

    public MoveElement(Move move, GameStageModel gameStageModel) {
        this.move = move;
        super(move.toStringHistory(), gameStageModel);
    }

    public Move getMove() {
        return move;
    }
}
