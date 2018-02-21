package models;

public class InputNeuron extends Neuron {
    private double v;

    public InputNeuron(double v) {
        this.setV(v);
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Override
    public double getValue() {
        return this.getV();
    }
}
