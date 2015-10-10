package org.ledalizer;

/**
 * Created by david on 2015-09-30.
 */
public class MatrixPoint {

    private int x, y;
    private int matrixId; //If you have more than one matrix

    public MatrixPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MatrixPoint(int x, int y, int matrixId) {
        this.x = x;
        this.y = y;
        this.matrixId = matrixId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(int matrixId) {
        this.matrixId = matrixId;
    }
}
