package com.anywherecreative.intellisweeper;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;

import java.util.Objects;

public class MineButton extends Button
{
    private int row;
    private int col;
    private boolean isMine; //is it a mine or not?
    private int mineCount; //how many mines touch this one
    private boolean isFlagged;
    private boolean exploded;
    
    public MineButton(String value) {
        super(value);
        setup();
    }
    
    public MineButton() {
        super();
        setup();
        
    }
    
    /**
     * used to setup the inital game from the constructors.
     */
    private void setup() {
        isMine = false;
        row = 0;
        col = 0;
        mineCount = 0;
        exploded = false;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public void setCol(int col) {
        this.col = col;
    }
    
    public int getRow() {
         return row;   
    }
    
    public int getCol() {
        return col;
    }
    
    public void setIsMine(boolean value) {
        this.isMine = value;
    }
    
    public boolean isNotAMine() {
        return !this.isMine;
    }
    
     public int getMineCount() {
         return mineCount;   
    }
    
    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }
    
    public void addNeighbourMine() {
        this.mineCount++;
    }
    
    public void toggleFlag() {
        isFlagged = !isFlagged;
        
        if(isFlagged) {
            ImageView view = new ImageView(new Image(MineButton.class.getResourceAsStream("/assets/flag-solid.png")));
            view.setFitHeight(Board.SQUARE_SIZE/3);
            view.setPreserveRatio(true);
            
            setGraphic(view);
            setContentDisplay(ContentDisplay.TOP);
        }
        else {
            setGraphic(null);
        }
    }
    
    public boolean isFlagged() {        
         return isFlagged;   
    }
    
    public void setExploded(boolean exploded) {
        if(exploded) {
            ImageView view = new ImageView(new Image(Objects.requireNonNull(MineButton.class.getResourceAsStream("/assets/explosion-solid.png"))));
            view.setFitHeight(Board.SQUARE_SIZE/3);
            view.setPreserveRatio(true);
            
            setGraphic(view);
            setContentDisplay(ContentDisplay.TOP);
        }
        else {
            setGraphic(null);
        }
        
        this.exploded = exploded;
    }
    
    public boolean isExploded() {
         return exploded;   
    }
    
    public void revealMine() {
        if(isMine && !exploded) {
            ImageView view = new ImageView(new Image(Objects.requireNonNull(MineButton.class.getResourceAsStream("/assets/bomb-solid.png"))));
            view.setFitHeight(Board.SQUARE_SIZE/3);
            view.setPreserveRatio(true);
            
            setGraphic(view);
            setContentDisplay(ContentDisplay.TOP);
        }
    }
    
    /**
     * reset the square to beginning game state, note the absence of row and col resets.
     */
    public void reset() {
        isMine = false;
        mineCount = 0;
        isFlagged = false;
        super.setDisable(false);
        super.setText("");
        exploded = false;
        setGraphic(null);
        
    }

}
