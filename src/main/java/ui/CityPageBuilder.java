package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

public class CityPageBuilder {
    private static final String FONT;
    private static final double LEFT_ANCHOR;
    private static final double RIGHT_ANCHOR;
    private static final double TOP_ANCHOR;
    private static final int FONT_SIZE;

    static {
        FONT = "Arial";
        LEFT_ANCHOR = 60;
        RIGHT_ANCHOR = LEFT_ANCHOR;
        TOP_ANCHOR = 1;
        FONT_SIZE = 14;
    }

    public static AnchorPane build(Map<String, List<String>> row) {
        Font font = new Font(FONT, FONT_SIZE);

        AnchorPane vBox = new AnchorPane();
        vBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        double top_anchor = TOP_ANCHOR;

        if (row != null) {
            for (Map.Entry<String, List<String>> column : row.entrySet()) {
                Label label = new Label(column.getKey());
                AnchorPane.setTopAnchor(label, top_anchor);
                AnchorPane.setLeftAnchor(label, LEFT_ANCHOR);
                AnchorPane.setRightAnchor(label, RIGHT_ANCHOR);
                label.setFont(font);

                top_anchor += 24;

                String valueString = column.getValue().toString();
                valueString = valueString.replaceAll("\\[", "").
                        replaceAll("]", "").replaceAll(",", ";");

                TextArea textArea = new TextArea(valueString);
                textArea.setMaxHeight(18);
                AnchorPane.setTopAnchor(textArea, top_anchor);
                AnchorPane.setLeftAnchor(textArea, LEFT_ANCHOR);
                AnchorPane.setRightAnchor(textArea, RIGHT_ANCHOR);
                textArea.setFont(font);

                top_anchor += 48;

                vBox.getChildren().addAll(label, textArea);
            }
        }

        return vBox;
    }
}
