package control;

import java.io.InputStream;
import java.util.Scanner;

import boardifier.model.GameStageModel;
import model.*;

public class OptionMenu {

    public OptionMenu() {}

    public boolean readMenu(GameStageModel gameStageModel, InputStream inputStream) {
        boolean ok = false;
        int prefix;
        int option;
        Scanner scanner = new Scanner(inputStream);

        while (!ok) {
            if (!scanner.hasNext()) {
                break;
            }

            String line = scanner.next().trim();


            if (line.equals("save")) {
                System.out.println("Options saved");
                return true;
            }

            if (line.length() == 2) {
                char prefixChar = line.charAt(0);
                char optionChar = line.charAt(1);

                if (optionChar >= 'a' && optionChar <= 'z') {
                    optionChar = Character.toUpperCase(optionChar);
                }

                prefix = (int) prefixChar - '1';
                option = (int) optionChar - 'A';

                if (prefix < 0 || prefix > 2 || option < 0 || option > 2) {
                    System.out.println("Invalid option, try again");
                    continue;
                }

                setOption(gameStageModel, prefix, option);

                ok = true;
            } else {
                System.out.println("Invalid option, try again");
            }
        }
        return false;
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
