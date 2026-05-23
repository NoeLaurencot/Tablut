package control;

import java.util.Scanner;

import boardifier.model.GameStageModel;
import model.*;

public class OptionMenu {
    private static final Scanner SCANNER = new Scanner(System.in);

    public OptionMenu() {}

    public void readMenu(GameStageModel gameStageModel) {
        boolean ok = false;
        int prefix;
        int option;

        while (!ok) {
            String line = SCANNER.next().trim();

            if (line.length() == 2) {
                char prefixChar = line.charAt(0);
                char optionChar = line.charAt(1);

                if (optionChar >= 'a' && optionChar <= 'z') {
                    optionChar = Character.toUpperCase(optionChar);
                }

                prefix = (int) prefixChar - '1';
                option = (int) optionChar - 'A';

                if (prefix < 0 || prefix > 2) continue;
                if (option < 0 || option > 2) continue;

                setOption(gameStageModel, prefix, option);

                ok = true;
            }
        }
    }

    private void setOption(GameStageModel gameStageModel, int prefix, int option) {
        if (prefix == 0) {
            ((TablutStageModel) gameStageModel).setKingMoveMode(model.OptionMenu.KING_MOVE_MODE[option]);
            System.out.println("King move mode set to " + model.OptionMenu.KING_MOVE_MODE[option]);
        } else if (prefix == 1) {
            ((TablutStageModel) gameStageModel).setWinMode(model.OptionMenu.WIN_MODE[option]);
            System.out.println("Game win mode set to " + model.OptionMenu.WIN_MODE[option]);
        }
    }
}
