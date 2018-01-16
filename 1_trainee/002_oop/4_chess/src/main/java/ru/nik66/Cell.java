package ru.nik66;

public class Cell {

    private int x;
    private int y;

    public Cell(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void setX(int x) {
        if (x < 0) {
            x = 0;
        }
        if (x > Board.ROWS - 1) {
            x = Board.ROWS - 1;
        }
        this.x = x;
    }

    private void setY(int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > Board.COLUMNS - 1) {
            y = Board.COLUMNS - 1;
        }
        this.y = y;
    }

}
