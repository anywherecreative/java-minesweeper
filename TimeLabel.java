import javafx.scene.control.Label;
import java.io.InputStream;
import javafx.scene.text.Font;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;


public class TimeLabel extends Label
{

    public TimeLabel(String value) {
        super(value);
        
        try {
            InputStream fontStream = TimeLabel.class.getResourceAsStream("assets/digitaldream.ttf");
            if (fontStream != null) {
                Font lcdFont = Font.loadFont(fontStream, 20);
                fontStream.close();
                setFont(lcdFont);
            }
        }
        catch (java.io.IOException ioe) {
            // the font isn't critical to operation so we're going to let is fail gracefully and use a sys font.
        }
        
        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        setTextFill(Color.RED);
        setPadding(new Insets(5,10,10,10));
    }
}
