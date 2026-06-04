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
import javafx.scene.input.*;

import model.TablutBoard;
import model.TablutStageModel;
import model.Pawn;

import java.util.List;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class ControllerTablutMouse extends ControllerMouse implements EventHandler<MouseEvent> {

    public ControllerTablutMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(),event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);

        // for debug, uncomment next instructions to display x,y and elements at that postion
        /*
        Logger.debug("click in "+event.getSceneX()+","+event.getSceneY());
        for(GameElement element : list) {
            Logger.debug(element);
        }
         */
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();

        int elementsClicked = list.size();
        if (elementsClicked == 2) {
            handlePawnClick(list);
        } else if (elementsClicked == 1) {
            handleBoardClick(list);
        }   
    }

    private void handleBoardClick(List<GameElement> list) {
        System.out.println("clicked board");
    }

    private void handlePawnClick(List<GameElement> list) {
        Pawn pawn = (Pawn) list.get(1);
        TablutBoard board = (TablutBoard) list.get(0);
        pawn.selected = true;

        int[] coord = board.getElementCell(pawn);
        board.setValidCells(coord[0], coord[1]);

        //System.out.println("clicked pawn : " + list.get(1));
    }
}

