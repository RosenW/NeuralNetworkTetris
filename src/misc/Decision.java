package misc;

import models.InputNeuron;

import java.util.ArrayList;
import java.util.List;

public class Decision {
    private List<InputNeuron> inputs;
    private String piece;
    private int rot;
    private int col;

    public Decision(List<InputNeuron> inputs, String piece, int rot, int col) {
        this.setInputs(inputs);
        this.setPiece(piece);
        this.setRot(rot);
        this.setCol(col);
    }

    public List<InputNeuron> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputNeuron> inputs) {
        this.inputs = inputs;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public int getRot() {
        return rot;
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
