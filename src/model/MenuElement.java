package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.TextElement;

import java.util.ArrayList;

public class MenuElement extends TextElement {
    private ArrayList<MenuEntry> menuEntries;
    private String prompt;

    public MenuElement(GameStageModel gameStageModel, String string) {
        super(string, gameStageModel);
        this.menuEntries = new ArrayList<>();
        this.prompt = "";
        ElementTypes.register("menu", 300);
    }

    public ArrayList<MenuEntry> getMenuEntries() {
        return menuEntries;
    }

    public void setMenuEntries(ArrayList<MenuEntry> menuEntries) {
        this.menuEntries = menuEntries;
    }

    public void addEntry(MenuEntry menuEntry) {
        this.menuEntries.add(menuEntry);
    }

    public void addPrompt(String message) {
        if (message == null) {
            this.prompt = "";
        } else {
            this.prompt = message;
        }
    }

    public void clearEntries() {
        this.menuEntries.clear();
    }

    public void setMenuString() {
        StringBuilder sb = new StringBuilder();

        for (MenuEntry menuEntry : menuEntries) {
            sb.append(menuEntry.toString());
        }

        if (!prompt.isEmpty()) {
            if ((sb.length() > 0) && (sb.charAt(sb.length() - 1) != '\n')) {
                sb.append("\n");
            }
            sb.append("\n").append(prompt);
        }

        setText(sb.toString());
    }

    public String getMenuString() {
        return this.text;
    }
}
