package view;

import boardifier.model.GameElement;
import boardifier.model.TextElement;
import boardifier.view.ElementLook;

public class MultiLineTextLook extends ElementLook {

    public MultiLineTextLook(GameElement element) {
        super(element);
    }

    @Override
    protected void render() {
        String text = ((TextElement) getElement()).getText();
        if (text == null) {
            text = "";
        }

        String[] lines = text.split("\\R", -1);
        int height = lines.length;
        int width = 0;
        for (String line : lines) {
            if (line.length() > width) {
                width = line.length();
            }
        }

        setSize(width, height);

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                shape[y][x] = String.valueOf(line.charAt(x));
            }
        }
    }
}
