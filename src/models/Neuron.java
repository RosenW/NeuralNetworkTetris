package models;

import core.Functions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Neuron {
    private Map<Neuron, Double> connections;
    private static Random random = new Random();
    private double bias;

    public Neuron() {
        this.setConnections(new HashMap<>());
        this.setBias((random.nextInt(2) + 1) * -1);
    }

    public Map<Neuron, Double> getConnections() {
        return connections;
    }

    public void setConnections(Map<Neuron, Double> connections) {
        this.connections = connections;
    }

    public double getValue() {
        double sumOfAllConnections = 0;
        for (Map.Entry<Neuron, Double> neuronWeight : connections.entrySet()) {
            sumOfAllConnections += neuronWeight.getKey().getValue() * neuronWeight.getValue();
        }
        return Functions.sigmoid(sumOfAllConnections + bias);
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
