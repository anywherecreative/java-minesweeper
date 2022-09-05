import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.stage.Window;
import javafx.stage.Modality;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import javafx.stage.WindowEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;

/**
 * Main Application Interface For MineSweeper
 *
 * @author Jeff Manning
 * @version 1.0.0
 */
public class Board extends Application {
    public static final int BOARD_WIDTH  = 600;
    public static final int BOARD_HEIGHT = 600;
    public static final int SQUARE_SIZE  = 60;
    public static final int SQUARE_SPACE = 10; //padding around board, and between squares
    public static final int COLS         = 10;
    public static final int MINES        = 10; //how many mines should be on the board
    
    ArrayList<MineButton> squares = new ArrayList<MineButton>();
    Stage loseDialog;
    Stage winDialog;
    int cleared = 0;
    boolean gameDone = false;
    TimeLabel gameTime;
    ResetButton reset;
    
    /**
     * Setup the board, add the buttons and assign mines
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        
        VBox mane = new VBox();
        mane.setAlignment(Pos.CENTER);
        mane.setSpacing(0);
        mane.setPadding(new Insets(10,10,10,10));
        
        BorderPane comPanel = new BorderPane();
        comPanel.setPadding(new Insets(0,10,0,10));
        
        gameTime = new TimeLabel("00:00");
        reset = new ResetButton();
        reset.setOnMouseClicked(this::resetButtonClick);
        
        comPanel.setLeft(gameTime);
        comPanel.setRight(reset);
        
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(SQUARE_SPACE, SQUARE_SPACE, SQUARE_SPACE, SQUARE_SPACE));
        stage.setResizable(false);
        
        pane.setVgap(SQUARE_SPACE);
        pane.setHgap(SQUARE_SPACE);
        
        //create the button grid
        for(int a = 0;a < COLS*COLS;a++) {
            int row = a/COLS;
            int col = a%COLS;
            
            MineButton btn = new MineButton();
            
            btn.setPrefWidth(SQUARE_SIZE);
            btn.setPrefHeight(SQUARE_SIZE);
            
            btn.setOnMouseClicked(this::buttonClick);
            btn.setOnMousePressed(this::buttonPressed);
            
            btn.setRow(row);
            btn.setCol(col);
            
            squares.add(btn);
            
            pane.add(squares.get(a),row, col);
        }
        
        mane.getChildren().add(comPanel);
        mane.getChildren().add(pane);
        
        seedMines();

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(mane, BOARD_WIDTH,BOARD_HEIGHT);
        stage.setTitle("Mine Sweeper");
        stage.setScene(scene);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                exitGame();
            }
        });

        // Show the Stage (window)
        stage.show();
    }


    /**
     * Mine has been clicked, check if it's a mine, close to one, or nothing
     */
    private void buttonClick(MouseEvent event) {
        MouseButton button = event.getButton();
        
        MineButton btn = (MineButton)event.getSource();
        if(button == MouseButton.PRIMARY) {
            if(btn.isFlagged() || gameDone) {
                reset.setHappy();
                return;
            }
            
            if(!gameTime.isRunning()) {
                gameTime.startTimer();
            }
            
            btn.setDisable(true);
            if(!btn.isAMine()) {
                reset.setHappy();
                cleared++;
                if(btn.getMineCount() > 0) {  
                    btn.setText(Integer.toString(btn.getMineCount()));
                }
                else {
                    checkNeighboursAreClear(btn); //if it's blank, open any ajacent mines that are blank
                }
                checkWin(btn.getScene().getWindow()); //check if we've checked all but the mine squares
            }
            else {
                reset.setDead();
                gameDone = true;
                gameTime.stopTimer();
                btn.setExploded(true);
                showLose(btn.getScene().getWindow());
            }
        }
        else {
            if(!btn.isDisabled()) {
                btn.toggleFlag();
                reset.setHappy();
            }
            
        }
    }
    
    private void buttonPressed(MouseEvent event) {
        MouseButton button = event.getButton();
         if(button == MouseButton.PRIMARY) {
            reset.setShock();
        }
    }
    
    private ArrayList<MineButton> getNeighbours(MineButton btn) {
        ArrayList<MineButton> neighbours = new ArrayList<MineButton>();
        int row = btn.getRow();
        int col = btn.getCol();
        int index = 0;
        
        //north
        index = getIndexFromRowCol(row-1,col);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //north east
        index = getIndexFromRowCol(row-1,col+1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //north west
        index = getIndexFromRowCol(row-1,col-1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //east
        index = getIndexFromRowCol(row,col+1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //west
        index = getIndexFromRowCol(row,col-1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //south
        index = getIndexFromRowCol(row+1,col);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //south east
        index = getIndexFromRowCol(row+1,col+1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        
        //south west
        index = getIndexFromRowCol(row+1,col-1);
        if(index >= 0 && index < (COLS*COLS)) {
            MineButton sq = squares.get(index);
            neighbours.add(squares.get(index));
        }
        return neighbours;
    }
    
    /**
     * return the correct array index based on the row and col specified
     * @param int row the row
     * @param int col the column
     * @return int the index
     */
    private int getIndexFromRowCol(int row, int col) {
        // multiple rows by COLS and add col
        return row * COLS + col;
    }
    
    
    private void seedMines() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<(COLS*COLS); i++){
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i=0; i<MINES; i++) {
            MineButton btn = squares.get(list.get(i));
            btn.setIsMine(true);
            ArrayList<MineButton> neighbours = getNeighbours(btn);
            Iterator<MineButton> neighboursIterator = neighbours.iterator();
            while(neighboursIterator.hasNext()) {
                MineButton neighbour = neighboursIterator.next();
                neighbour.addNeighbourMine();
            }
        }
    }
    
    private void checkNeighboursAreClear(MineButton btn) {
        ArrayList<MineButton> checkList = getNeighbours(btn);
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        
        int a = 0;
        
        //limiting the while to the total number of squares to prevent an infinite loop
        while(a < COLS*COLS) {
            MineButton check = checkList.get(a);
            indexes.add(getIndexFromRowCol(check.getRow(), check.getCol()));
            
            if(!check.isDisabled() && check.getMineCount() == 0 && !check.isAMine()) {
                check.setDisable(true);
                cleared++;
                ArrayList<MineButton> neighbours = getNeighbours(check);
                Iterator<MineButton> neighboursIterator = neighbours.iterator();
                while(neighboursIterator.hasNext()) {
                    MineButton neighbour = neighboursIterator.next();
                    if(!indexes.contains(getIndexFromRowCol(neighbour.getRow(), neighbour.getCol()))) {
                        indexes.add(getIndexFromRowCol(neighbour.getRow(), neighbour.getCol()));
                        if(!neighbour.isDisabled() && !neighbour.isAMine()) {
                            if(neighbour.getMineCount() == 0) {
                                checkList.add(neighbour);
                            }
                            else {
                                neighbour.setText(Integer.toString(neighbour.getMineCount()));
                                neighbour.setDisable(true);
                                cleared++;
                            }
                        }
                    }
                }
            }
            
            a++;
            if(a == checkList.size()) {
                break;
            }
        }
    }
    
    private void showLose(Window window) {
        
        Iterator<MineButton> btns = squares.iterator();
        while(btns.hasNext()) {
            MineButton btn = btns.next();
            btn.revealMine();
        }
                
        loseDialog = new Stage();
        loseDialog.initModality(Modality.APPLICATION_MODAL);
        loseDialog.initOwner(window);
        
        Button okBtn = new Button("Continue");
        okBtn.setOnMouseClicked(this::resetBoardLose);
        
        Button exitBtn = new Button("Exit");
        exitBtn.setOnMouseClicked(this::exitGameBtn);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(new Text("You Lose! Play Again?"));
        dialogVbox.getChildren().add(okBtn);
        dialogVbox.getChildren().add(exitBtn);
        
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        loseDialog.setScene(dialogScene);
        loseDialog.show();
    }
    
    public void checkWin(Window window) {
        int squaresToClear = COLS*COLS-MINES;
        if(cleared == squaresToClear) {
            reset.setWin();
            gameTime.stopTimer();
            gameDone = true;
            winDialog = new Stage();
            winDialog.initModality(Modality.APPLICATION_MODAL);
            winDialog.initOwner(window);
            
            Button okBtn = new Button("Continue");
            okBtn.setOnMouseClicked(this::resetBoardWin);

            Button exitBtn = new Button("Exit");
            exitBtn.setOnMouseClicked(this::exitGameBtn);
            
            
            VBox dialogVbox = new VBox(20);
            dialogVbox.setAlignment(Pos.CENTER);
            dialogVbox.getChildren().add(new Text("You Win! Play Again?"));
            dialogVbox.getChildren().add(okBtn);
            dialogVbox.getChildren().add(exitBtn);
            
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            winDialog.setScene(dialogScene);
            winDialog.show();
        }
        else {
            System.out.println(Integer.toString(squaresToClear) + " > " + Integer.toString(cleared));
        }
        
    }
    
    private void resetBoardLose(MouseEvent event) {
        loseDialog.hide();
        loseDialog.close();
        resetBoard();
    }
    
    private void resetBoardWin(MouseEvent event) {
        winDialog.hide();
        winDialog.close();
        resetBoard();
    }
    
    private void resetButtonClick(MouseEvent event) {
        resetBoard();
    }
    
    private void resetBoard() {
        cleared = 0;
        gameDone = false;
        gameTime.stopTimer(); //needed for when the reset button is clicked
        gameTime.resetTimer();
        reset.setHappy();
        Iterator<MineButton> resetIterator = squares.iterator();
        while(resetIterator.hasNext()) {
            MineButton btn = resetIterator.next();
            btn.reset();
        }
        seedMines();
    }
    
    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }
    
    /**
     * alias for exitGame handles button event
     */
    private void exitGameBtn(MouseEvent e) {
        exitGame();
    }

}
