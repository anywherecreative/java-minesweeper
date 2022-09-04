import javafx.scene.control.Button;

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
    
    public boolean isAMine() {
        return this.isMine;
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
    
    public void setFlag(boolean flag) {
        this.isFlagged = flag;
    }
    
    public boolean isFlagged() {
         return isFlagged;   
    }
    
        public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }
    
    public boolean isExploded() {
         return exploded;   
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
