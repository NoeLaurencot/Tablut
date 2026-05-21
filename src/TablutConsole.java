import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import control.TablutController;
import model.AIMode;
import model.TablutStageModel;

import java.util.Arrays;

public class TablutConsole {

    public static void main(String[] args) {
        final AIMode[] AI_MODES = {AIMode.MINMAX, AIMode.SIMPLE_EVAL, AIMode.IDK_YET};
        // Debug
        //Logger.setLevel(Logger.LOGGER_TRACE);
        //Logger.setVerbosity(Logger.VERBOSE_HIGH);
        int mode = 0;
        int aiModeSingleComp = 0;
        int[] aiModeMultipleComp = {0, 0};
        if (args.length >= 1 && args.length <= 3) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode < 0) || (mode > 3)) mode = 0;
            } catch (NumberFormatException e) {
            }
        }
        if (args.length == 2) {
            if ((mode == 1 || mode == 2)) {
                try {
                    aiModeSingleComp = Integer.parseInt(args[1]);
                    if ((aiModeSingleComp < 0) || (aiModeSingleComp > AI_MODES.length - 1)) aiModeSingleComp = 0;
                } catch (NumberFormatException e) {
                }
            } else if (mode == 3) {
                try {
                    aiModeMultipleComp[0] = Integer.parseInt(args[1]);
                    aiModeMultipleComp[1] = Integer.parseInt(args[1]);
                    if ((aiModeMultipleComp[0] < 0) || (aiModeMultipleComp[0] > AI_MODES.length - 1)) {
                        aiModeMultipleComp[0] = 0;
                        aiModeMultipleComp[1] = 0;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        if (args.length == 3) {
            try {
                aiModeMultipleComp[0] = Integer.parseInt(args[1]);
                aiModeMultipleComp[1] = Integer.parseInt(args[2]);
                if ((aiModeMultipleComp[0] < 0) || (aiModeMultipleComp[0] > AI_MODES.length - 1)) {
                    aiModeMultipleComp[0] = 0;
                }
                if ((aiModeMultipleComp[1] < 0) || (aiModeMultipleComp[1] > AI_MODES.length - 1)) {
                    aiModeMultipleComp[1] = 0;
                }
            } catch (NumberFormatException e) {
            }
        }

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
        View tablutView = new View(model);
        TablutController control = new TablutController(model, tablutView);
        control.setFirstStageName("tablut");

        try {
            control.startGame();

            ((TablutStageModel) model.getGameStage()).setMode(mode);

            if (mode >= 1 && mode <= 2) {
                ((TablutStageModel) model.getGameStage()).setAiModeSingle(AI_MODES[aiModeSingleComp]);
            } else if (mode == 3) {
                ((TablutStageModel) model.getGameStage()).setAiModeMultiple(new AIMode[]{AI_MODES[aiModeMultipleComp[0]], AI_MODES[aiModeMultipleComp[1]]});
            }

            control.stageLoop(System.in);
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}
