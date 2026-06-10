import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import control.TablutController;
import model.AIMode;
import model.TablutStageModel;

import view.TablutView;
import view.TablutStageView;
import view.TablutRootPane;

import javafx.application.Application;
import javafx.stage.Stage;


import java.util.Arrays;

public class Tablut extends Application {

    private static int mode = 0;

    public static void main(String[] args) {
        // Debug
        //Logger.setLevel(Logger.LOGGER_TRACE);
        //Logger.setVerbosity(Logger.VERBOSE_HIGH);
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode < 0) || (mode > 3)) mode = 0;
            } catch (NumberFormatException e) {
            }
        }
        if (args.length > 1) {
            System.err.print("Usage: java Tablut [mode]");
            System.exit(1);
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Model model = new Model();

        if (mode == 0) {
            model.addHumanPlayer("swedish player");
            model.addHumanPlayer("muscovite player");
        } else if (mode == 1) {
            model.addHumanPlayer("swedish player");
            model.addComputerPlayer("muscovite computer");
        } else if (mode == 2) {
            model.addComputerPlayer("swedish computer");
            model.addHumanPlayer("muscovite player");
        } else if (mode == 3) {
            model.addComputerPlayer("swedish computer");
            model.addComputerPlayer("muscovite computer");
        }

        StageFactory.registerModelAndView("tablut", "model.TablutStageModel", "view.TablutStageView");
        TablutRootPane rootPane = new TablutRootPane();
        TablutView tablutView = new TablutView(model, stage, rootPane);
        TablutController control = new TablutController(model, tablutView);
        control.setFirstStageName("tablut");

        stage.setTitle("Tablut");
        stage.show();
    }
}
