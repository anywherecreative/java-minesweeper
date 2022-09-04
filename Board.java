

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import javafx.stage.WindowEvent;

/**
 * Write a description of JavaFX class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board extends Application {
    private final int BOARD_WIDTH  = 600;
    private final int BOARD_HEIGHT = 600;
    private final int SQUARE_SIZE  = 60;
    private final int SQUARE_SPACE = 10; //padding around board, and between squares
    private final int COLS         = 10;
    private final int MINES        = 10; //how many mines should be on the board
    
    ArrayList<MineButton> squares = new ArrayList<MineButton>();
    
    
    /**
     * Setup the board, add the buttons and assign mines
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        
        int mines = 0;
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(SQUARE_SPACE, SQUARE_SPACE, SQUARE_SPACE, SQUARE_SPACE));
        pane.setMinSize(BOARD_WIDTH, BOARD_HEIGHT);
        pane.setVgap(SQUARE_SPACE);
        pane.setHgap(SQUARE_SPACE);
        
        //create the button grid
        for(int a = 0;a < COLS*COLS;a++) {
            MineButton btn = new MineButton("");
            btn.setPrefWidth(SQUARE_SIZE);
            btn.setPrefHeight(SQUARE_SIZE);
            btn.setOnAction(this::buttonClick);
            squares.add(btn);
            int row = a/COLS;
            int col = a%COLS;
            btn.setRow(row);
            btn.setCol(col);
            pane.add(squares.get(a),row, col);
        }
        
        seedMines();
        
 
        
        

      
        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(pane, BOARD_WIDTH,BOARD_HEIGHT);
        stage.setTitle("Mine Sweeper");
        stage.setScene(scene);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });



        // Show the Stage (window)
        stage.show();
    }


    /**
     * This will be executed when the button is clicked
     * It increments the count by 1
     */
    private void buttonClick(ActionEvent event) {
        MineButton btn = (MineButton)event.getSource();
        btn.setDisable(true);
        if(!btn.isAMine()) {
            if(btn.getMineCount() > 0) {  
                btn.setText(Integer.toString(btn.getMineCount()));
            }
            else {
                checkNeighboursAreClear(btn); //if it's blank, open any ajacent mines that are blank
            }
        }
        else {
            btn.setText("M");
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
        
        int a =0;
        while(a < 100) {
            MineButton check = checkList.get(a);
            indexes.add(getIndexFromRowCol(check.getRow(), check.getCol()));
            
            if(!check.isDisabled() && check.getMineCount() == 0 && !check.isAMine()) {
                check.setDisable(true);
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
}
