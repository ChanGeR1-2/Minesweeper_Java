package minesweeper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Field {
    private Cell[][] field;
    private int startingMines;
    private int mines;
    private int flaggedMines;
    private int coveredCells;
    private boolean firstFree;

    public Field() {
        this.field = new Cell[9][9];
        this.mines = 0;
        this.startingMines = 0;
        this.flaggedMines = 0;
        this.coveredCells = 81;
        this.firstFree = true;
    }

    public String claimCell(int x, int y) {
        int i = y - 1;
        int j = x - 1;
        Cell cell = this.field[i][j];
        if (cell.getCover() == Cell.Cover.UNCOVERED || cell.getCover() == Cell.Cover.FLAGGED) {
            return "false";
        }
        if (cell.getState() != Cell.State.MINED) {
            firstFree = false;
            claimFreeCell(i, j);
            System.out.println(this.coveredCells);
            return "true";
        } else {
            if (firstFree) {
                this.generate(this.startingMines + 1, j, i);
                claimCell(x, y);
            } else {
                revealMines();
                return "loss";
            }
        }
        return "true";
    }

    public void revealMines() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                if (this.field[i][j].getState() == Cell.State.MINED) {
                    this.field[i][j].setCover(Cell.Cover.UNCOVERED);
                }
            }
        }
    }

    public void claimFreeCell(int i, int j) {
        Cell thisCell = this.field[i][j];
        thisCell.setCover(Cell.Cover.UNCOVERED);
        this.coveredCells--;
        if (thisCell.getState() == Cell.State.MINE_ADJACENT) {
            return;
        }
        for (int l = 1; l >= -1; l--) {
            for (int p = 1; p >= -1; p--) {
                try {
                    Cell cell = this.field[i - l][j - p];
                    if (cell.getState() == Cell.State.EMPTY && cell.getCover() != Cell.Cover.UNCOVERED) {
                        claimFreeCell((i - l), (j - p));
                    }
                    if (cell.getState() == Cell.State.MINE_ADJACENT && cell.getCover() != Cell.Cover.UNCOVERED) {
                        cell.setCover(Cell.Cover.UNCOVERED);
                        this.coveredCells--;
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }

    }

    public boolean setMark(int x, int y) {
        int i = y - 1;
        int j = x - 1;
        Cell cell = this.field[i][j];
        if (cell.getCover() == Cell.Cover.UNCOVERED) {
            return false;
        }
        switch (cell.getCover()) {
            case COVERED:
                cell.setCover(Cell.Cover.FLAGGED);
                if (cell.getState() == Cell.State.MINED) {
                    this.flaggedMines++;
                }
                break;
            case FLAGGED:
                cell.setCover(Cell.Cover.COVERED);
                if (cell.getState() == Cell.State.MINED) {
                    this.flaggedMines--;
                }
                break;
        }
        return true;
    }

    public boolean calcWin() {
        if (this.flaggedMines == this.mines || this.coveredCells == this.mines) {
            return true;
        }
        return false;
    }

    public void generate(int mines, int x, int y) {
        this.startingMines = mines;
        Random rand = new Random();
        Set<Integer> numbers = new HashSet<>();
        Integer[] coordinates = new Integer[] {x, y};
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                Integer[] thisCoordinates = new Integer[] {j, i};
                int num;
                do {
                    num = rand.nextInt(81);
                } while (!numbers.add(num));
                if (Arrays.equals(coordinates, new Integer[] {-1, -1})) {
                    if (num < mines && (!Arrays.equals(coordinates, thisCoordinates))) {
                        this.field[i][j] = new Cell(Cell.State.MINED);
                        this.mines++;
                    } else {
                        this.field[i][j] = new Cell(Cell.State.EMPTY);
                    }
                } else {
                    if (num < mines && (!Arrays.equals(coordinates, thisCoordinates))) {
                        this.field[i][j].setState(Cell.State.MINED);
                        this.mines++;                  } else {
                        this.field[i][j].setState(Cell.State.EMPTY);
                    }
                }
            }
        }
        calcMines();
    }

    public void generate(int mines) {
        generate(mines, -1, -1);
    }

    private void calcMines() {
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                if (this.field[i][j].getState() == Cell.State.EMPTY) {
                    int count = checkMines(i, j);
                    if (count > 0) {
                        this.field[i][j].setSurroundingMines(count);
                    }
                }
            }
        }
    }

    private int checkMines(int i, int j) {
        int count = 0;
        for (int l = 1; l >= -1; l--) {
            for (int p = 1; p >= -1; p--) {
                try {
                    if (this.field[i - l][j - p].getState() == Cell.State.MINED) {
                        count++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(" |123456789|\n-|---------|");
        for (int i = 0; i < this.field.length; i++) {
            builder.append("\n").append(i + 1).append("|");
            for (int j = 0; j < this.field[i].length; j++) {
                Cell cell = this.field[i][j];
                switch (cell.getCover()) {
                    case COVERED:
                        builder.append(".");
                        break;
                    case FLAGGED:
                        builder.append("*");
                        break;
                    case UNCOVERED:
                        switch (cell.getState()) {
                            case EMPTY:
                                builder.append("/");
                                break;
                            case MINE_ADJACENT:
                                builder.append(cell.getSurroundingMines());
                                break;
                            case MINED:
                                builder.append("X");
                        }
                }
            }
            builder.append("|");
        }
        builder.append("\n-|---------|");
        return builder.toString();
    }
}
