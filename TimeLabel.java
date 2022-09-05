import javafx.scene.control.Label;
import java.io.InputStream;
import javafx.scene.text.Font;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class TimeLabel extends Label
{
    private Timer gameTimer;
    private int timeElapsed;
    private boolean overtime = false;

    public TimeLabel(String value) {
        super(value);
        timeElapsed = 0;

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
        startTimer();
    }

    public void startTimer() {
        gameTimer = new Timer();
        TimerTask incrementClock = new TimerTask() {
                public void run() {
                    timeElapsed++;
                    if(timeElapsed == Integer.MAX_VALUE) {
                        timeElapsed = 0;
                        overtime=true;
                    }
                    Platform.runLater(new Runnable() {
                            @Override public void run() {
                                int minutes = timeElapsed/60;
                                int seconds = timeElapsed%60;
                                if(!overtime) {
                                    setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                                }
                                else {
                                    setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + " E");
                                }
                            }
                        });
                } 
            };
        gameTimer.schedule(incrementClock,0, 1000);
    }
}
