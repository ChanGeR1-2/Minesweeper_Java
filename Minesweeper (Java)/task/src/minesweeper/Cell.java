package minesweeper;

public class Cell {
    enum State {
        EMPTY, MINED, MINE_ADJACENT;
    }
    enum Cover {
        FLAGGED, COVERED, UNCOVERED;
    }
    private int surroundingMines;
    private State state;
    private Cover cover;

    public Cell(State state) {
        switch (state) {
            case MINED:
              this.state = State.MINED;
              break;
            case EMPTY:
                this.state = State.EMPTY;
                break;
        }
        this.surroundingMines = 0;
        this.cover = Cover.COVERED;
    }

    public int getSurroundingMines() {
        return surroundingMines;
    }

    public void setSurroundingMines(int surroundingMines) {
        this.surroundingMines = surroundingMines;
        setState(State.MINE_ADJACENT);
    }

    public State getState() {
        return state;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public void setState(State state) {
        this.state = state;
    }
}
