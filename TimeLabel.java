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
    private TimerTask incrementClock;
    private int timeElapsed;
    private boolean overtime;
    private boolean running;

    public TimeLabel(String value) {
        super(value);
        timeElapsed = 0;
        running = false;
        overtime = false;

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

    public void startTimer() {
        running = true;
        gameTimer = new Timer();
        incrementClock = new TimerTask() {
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
    
    public void stopTimer() {
        running = false;
        incrementClock.cancel();
    }
    
    public void resetTimer() {
        timeElapsed = 0;
        setText("00:00");
    }
    
    public boolean isRunning() {
        return running;
    }
     
}
