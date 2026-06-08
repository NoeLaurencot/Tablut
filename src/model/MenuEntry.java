package model;

import java.util.ArrayList;

public class MenuEntry {
    private int level;
    private String name;
    private ArrayList<MenuEntry> submenu;
    private int entryQuantity;
    private int prefixLevel;

    public MenuEntry(int level, String name, ArrayList<model.MenuEntry> submenu, int entryQuantity) {
        this.level = level;
        this.name = name;
        this.submenu = submenu;
        this.entryQuantity = entryQuantity;
        this.prefixLevel = level % 3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MenuEntry> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(ArrayList<MenuEntry> submenu) {
        this.submenu = submenu;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrefixLevel() {
        return prefixLevel;
    }

    public void setPrefixLevel(int prefixLevel) {
        this.prefixLevel = prefixLevel;
    }

    public int getEntryQuantity() {
        return entryQuantity;
    }

    public void setEntryQuantity(int entryQuantity) {
        this.entryQuantity = entryQuantity;
    }

    public void addSubmenuEntry(MenuEntry menuEntry) {
        submenu.add(menuEntry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }

        String prefix;
        switch (prefixLevel) {
            case 0:
                prefix = String.valueOf(entryQuantity);
                break;
            case 1:
                prefix = String.valueOf((char) ('A' + entryQuantity - 1));
                break;
            case 2:
                prefix = String.valueOf((char) ('a' + entryQuantity - 1));
                break;
            default:
                prefix = String.valueOf(entryQuantity);
                break;
        }

        sb.append(prefix).append(" - ").append(name).append("\n");

        for (MenuEntry menuEntry : submenu) {
            sb.append(menuEntry.toString());
        }
        return sb.toString();
    }
}
