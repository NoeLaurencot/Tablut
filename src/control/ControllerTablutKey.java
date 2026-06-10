package control;

import boardifier.control.*;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.*;

import java.security.Key;
import java.util.List;


public class ControllerTablutKey extends ControllerKey implements EventHandler<KeyEvent> {
    private static boolean paused = false;

    public ControllerTablutKey(Model model, View view, Controller control) {
        super(model, view, control);
        view.getRootPane().setOnKeyReleased(null);
    }

    public void handle(KeyEvent event) {
        // to pause, buggy
//        if (event.getCode().equals(KeyCode.SPACE)) {
//            if (!paused) {
//                model.pauseGame();
//                System.out.println("Paused");
//                paused = true;
//            } else {
//                model.resumeGame();
//                System.out.println("Resumed");
//                paused = false;
//            }
//        }
    }
}

