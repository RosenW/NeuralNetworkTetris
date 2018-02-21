package misc;

public class Output {
    private int rotation;
    private int column;

    public Output(int rotation, int column) {
        this.setRotation(rotation);
        this.setColumn(column);
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
