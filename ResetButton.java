import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;

public class ResetButton extends Button {
    
    public ResetButton() {
        super();
        setup();
    }

    public ResetButton(String value) {
        super(value);
        setup();
    }
    
    private void setup() {
        setHappy();
    }
    
    /**
     * set the image to the shocked state
     */
    public void setShock() {
        ImageView view = new ImageView(new Image("assets/face-surprise-solid.png"));
        view.setFitHeight(Board.SQUARE_SIZE/3);
        view.setPreserveRatio(true);
        
        setGraphic(view);
        setContentDisplay(ContentDisplay.TOP);
    }
    
    /**
     * set the icon to the default happy state for the game
     */
    public void setHappy() {
        ImageView view = new ImageView(new Image("assets/face-smile-solid.png"));
        view.setFitHeight(Board.SQUARE_SIZE/3);
        view.setPreserveRatio(true);
        
        setGraphic(view);
        setContentDisplay(ContentDisplay.TOP);
    }
    
    /**
     * set the icon to the dead state on game loss
     */
    public void setDead() {
        ImageView view = new ImageView(new Image("assets/face-dizzy-solid.png"));
        view.setFitHeight(Board.SQUARE_SIZE/3);
        view.setPreserveRatio(true);
        
        setGraphic(view);
        setContentDisplay(ContentDisplay.TOP);
    }
    
}