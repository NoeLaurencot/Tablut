import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import control.TablutController;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class TablutConsolePreInput {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Debug
        //Logger.setLevel(Logger.LOGGER_TRACE);
        //Logger.setVerbosity(Logger.VERBOSE_HIGH);
        File ressource;
        File move;
        String strMove;
        try {
            ressource = new File(TablutConsolePreInput.class.getResource("/moves").getFile());
            File[] moves = Objects.requireNonNull(ressource.listFiles());
            for (int i = 1; i <= moves.length; i++) {
                System.out.println(i + ")" + moves[i - 1].getName());
            }
            int input = scanner.nextInt();
            while (input < 0 || input > moves.length) {
                System.out.println("choose a valid option : ");
                input = scanner.nextInt();
            }
            move = moves[input-1];

            strMove = Files.readString(move.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Model model = new Model();
        model.addHumanPlayer("swedish player");
        model.addHumanPlayer("muscovite player");
        StageFactory.registerModelAndView("tablut", "model.TablutStageModel", "view.TablutStageView");
        View tablutView = new View(model);
        TablutController control = new ControllerTablutTest(model, tablutView,strMove);
        control.setFirstStageName("tablut");
        try {
            control.startGame();
            control.stageLoop(System.in);
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}



class ControllerTablutTest extends TablutController {
    private final String preInputMoves;

    public ControllerTablutTest(Model model, View view, String preInputMoves) {
        super(model, view);
        this.preInputMoves = preInputMoves;
    }
    @Override
    public void stageLoop(InputStream in) {
        System.out.println("--- PRE-DETERMINED INPUT ---");;
        ByteArrayInputStream testStream = new ByteArrayInputStream(this.preInputMoves.getBytes());
        try {
            super.stageLoop(testStream);
        } catch (Exception e) {
            System.out.println("--- MANUAL INPUT ---");;
        }
            super.stageLoop(in);

    }

}