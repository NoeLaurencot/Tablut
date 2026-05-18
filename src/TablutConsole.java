import boardifier.control.Logger;
import boardifier.model.GameException;
import boardifier.view.View;
import control.TablutController;

import boardifier.control.StageFactory;
import boardifier.model.Model;

public class TablutConsole {

    public static void main(String[] args) {
        // Debug
        //Logger.setLevel(Logger.LOGGER_TRACE);
        //Logger.setVerbosity(Logger.VERBOSE_HIGH);
        int mode = 0;
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>3)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }

        Model model = new Model();

        if (mode == 0) {
            model.addHumanPlayer("swedish player");
            model.addHumanPlayer("muscovite player");
        }
        else if (mode == 1) {
            model.addHumanPlayer("swedish player");
            model.addComputerPlayer("muscovite computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("swedish computer");
            model.addHumanPlayer("muscovite player");
        }
        else if (mode == 3) {
            model.addComputerPlayer("swedish computer");
            model.addComputerPlayer("muscovite computer");
        }

        StageFactory.registerModelAndView("tablut", "model.TablutStageModel", "view.TablutStageView");
        View tablutView = new View(model);
        TablutController control = new TablutController(model, tablutView);
        control.setFirstStageName("tablut");
        try {
            control.startGame();
            control.stageLoop();
        }
        catch(GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}
