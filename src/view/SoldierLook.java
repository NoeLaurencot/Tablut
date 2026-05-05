package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Soldier;
import model.Team;

public class SoldierLook extends ElementLook {

    public SoldierLook(GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element, 1, 1);
    }

    protected void render() {
        Soldier soldier = (Soldier)element;

        if (soldier.getTeam() == Team.SWEDISH) {
            shape[0][0] = ConsoleColor.YELLOW + "O" + ConsoleColor.RESET;
        }
        else {
            shape[0][0] = ConsoleColor.RED + "O" + ConsoleColor.RESET;
        }
    }

}
