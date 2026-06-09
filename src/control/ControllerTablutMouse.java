package control;

import boardifier.control.*;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.PutInContainerAction;
import boardifier.model.action.RemoveFromContainerAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.ElementLook;
import boardifier.view.GridLook;
import boardifier.view.View;

import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import javafx.scene.input.*;

import model.*;

import java.util.List;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class ControllerTablutMouse extends ControllerMouse implements EventHandler<MouseEvent> {
    private Pawn selectedPawn;
    private Coordinate selectedCoords;

    public ControllerTablutMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D click = new Coord2D(event.getSceneX(),event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(click);

        // for debug, uncomment next instructions to display x,y and elements at that postion
        /*
        Logger.debug("click in "+event.getSceneX()+","+event.getSceneY());
        for(GameElement element : list) {
            Logger.debug(element.toString());
        }
         */

        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();

        boolean fortressInList = false;
        for (GameElement ge : list) {
            if (ge instanceof Fortress) {
                fortressInList = true;
                break;
            }
        }

        int elementsClicked = list.size();

        if (fortressInList) {
            if (elementsClicked == 3) {
                handlePawnClick(list);
            } else if (elementsClicked == 2 && stageModel.getState() == TablutStageModel.STATE_SELECT_DEST) {
                handleBoardClick(list, click);
            }  
        } else {
            if (elementsClicked == 2) {
                handlePawnClick(list);
            } else if (elementsClicked == 1 && stageModel.getState() == TablutStageModel.STATE_SELECT_DEST) {
                handleBoardClick(list, click);
            }
        }

    }


    private void handleBoardClick(List<GameElement> list, Coord2D click) {
        TablutBoard board = (TablutBoard) list.getFirst();
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        TablutController tablutController = (TablutController) this.control;

        // Get the board look to convert scene coordinates to cell coordinates
        GridLook boardLook = (GridLook) control.getElementLook(board);
        int[] dest = boardLook.getCellFromSceneLocation(click);
        boolean[][] reach = board.getReachableCells();

        if (dest != null) {
            if (reach[dest[0]][dest[1]]) {
                Move move = new Move(this.selectedCoords.getX(), this.selectedCoords.getY(), dest[0], dest[1]);
                
                tablutController.setLastMove(move);

                ActionList actions = ActionFactory.generateMoveWithinContainer(this.control, model, selectedPawn, dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, 10);

                tablutController.setLastMove(new Move(selectedPawn.getXInBoard(), selectedPawn.getYInBoard(), dest[0], dest[1]));

                stageModel.setState(TablutStageModel.STATE_SELECT_PAWN);
                selectedPawn.unselect();
                stageModel.unselectAll();
                selectedPawn = null;
                actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

                tablutController.addAfterPlayActions(move, actions);


                ActionPlayer play = new ActionPlayer(model, this.control, actions);

                play.start();


            }
        }
        board.resetReachableCells(false);


        // for debug
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                if (reach[i][j]) {
//                    System.out.print("1 ");
//                } else {
//                    System.out.print("0 ");
//                }
//            }
//            System.out.println();
//        }
    }

    private void handlePawnClick(List<GameElement> list) {
        TablutBoard board = (TablutBoard) list.getFirst();
        Pawn pawn = (Pawn) list.getLast();
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();

        Team curTeam;

        if (this.model.getCurrentPlayer().getName().equals("swedish player"))
            curTeam = Team.SWEDISH;
        else
            curTeam = Team.MUSCOVITES;

        if (pawn.getTeam().equals(curTeam)) {
            stageModel.setState(TablutStageModel.STATE_SELECT_DEST);
            pawn.select();
            selectedPawn = pawn;

            int[] coord = board.getElementCell(pawn);
            board.setValidCells(coord[0], coord[1]);

            this.selectedCoords = new Coordinate(coord[0], coord[1]);
        }

//        System.out.println("clicked pawn : " + list.get(1));
    }
}

