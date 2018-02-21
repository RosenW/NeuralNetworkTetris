package models;

import misc.Decision;
import misc.Output;

import java.util.*;

public class Network {
    private static Random random = new Random();
    private List<InputNeuron> inputNeurons = new ArrayList<>();
    private List<Neuron> firstHiddenNeuronLayer = new ArrayList<>();
    private List<Neuron> secondHiddenNeuronLayer = new ArrayList<>();
    private List<Neuron> outputNeurons = new ArrayList<>();
    private List<Decision> decisionsToLearnFrom = new ArrayList<>();

    public Network(int outputNeuronNumber) {
        for (int i = 0; i < 200; i++) {
            inputNeurons.add(new InputNeuron(0)); // ADDING INPUT NEURONS
        }
        for (int i = 0; i < 14; i++) {
            Neuron curNeuron = new Neuron();
            for (InputNeuron inputNeuron : inputNeurons) {
                curNeuron.getConnections().put(inputNeuron, random.nextDouble()); // ADDING FIRST LAYER
            }
            firstHiddenNeuronLayer.add(curNeuron);
        }
        for (int i = 0; i < 14; i++) {
            Neuron curNeuron = new Neuron();
            for (Neuron neuron : firstHiddenNeuronLayer) {
                curNeuron.getConnections().put(neuron, random.nextDouble()); // ADDING SECOND LAYER
            }
            secondHiddenNeuronLayer.add(curNeuron);
        }

        for (int i = 0; i < outputNeuronNumber; i++) {
            Neuron curNeuron = new Neuron();
            for (Neuron neuron : secondHiddenNeuronLayer) {
                curNeuron.getConnections().put(neuron, random.nextDouble()); // ADDING OUTPUT LAYER
            }
            outputNeurons.add(curNeuron);
        }
    }

    public List<InputNeuron> getInputNeurons() {
        List<InputNeuron> inputNeuronsCopy = new ArrayList<>();
        for (InputNeuron inputNeuron : inputNeurons) {
            inputNeuronsCopy.add(new InputNeuron(inputNeuron.getV()));
        }
        return inputNeuronsCopy;
    }

    public void setInputNeurons(List<InputNeuron> inputNeurons) {
        this.inputNeurons = inputNeurons;
    }

    public List<Neuron> getFirstHiddenNeuronLayer() {
        return firstHiddenNeuronLayer;
    }

    public void setFirstHiddenNeuronLayer(List<Neuron> firstHiddenNeuronLayer) {
        this.firstHiddenNeuronLayer = firstHiddenNeuronLayer;
    }

    public List<Neuron> getSecondHiddenNeuronLayer() {
        return secondHiddenNeuronLayer;
    }

    public void setSecondHiddenNeuronLayer(List<Neuron> secondHiddenNeuronLayer) {
        this.secondHiddenNeuronLayer = secondHiddenNeuronLayer;
    }

    public List<Neuron> getOutputNeurons() {
        return outputNeurons;
    }

    public void setOutputNeurons(List<Neuron> outputNeurons) {
        this.outputNeurons = outputNeurons;
    }

    public Output generateOutput(int[][] playfield, String piece) {
        for (int y = 1; y <= playfield[0].length; y++) {
            for (int x = 1; x <= playfield.length; x++) {
                if (playfield[x - 1][y - 1] != 0) {
                    inputNeurons.get(((y - 1) * playfield.length) + x - 1).setV(1);
                } else {
                    inputNeurons.get(((y - 1) * playfield.length) + x - 1).setV(0);
                }
            }
        }
//        for (Neuron neuron : firstHiddenNeuronLayer) {
//            System.out.println("First: " + neuron.getValue());
//        }
//        System.out.println();
//
//        for (Neuron neuron : secondHiddenNeuronLayer) {
//            System.out.println("Second: " + neuron.getValue());
//        }
//        System.out.println();
//
//        for (Neuron outputNeuron : outputNeurons) {
//            System.out.println("Output: " + outputNeuron.getValue()); // FOR DEBUG
//        }
//        System.out.println();

//        for (Neuron outputNeuron : outputNeurons) {
//            for (Map.Entry<Neuron, Double> entry : outputNeuron.getConnections().entrySet()) {
//                System.out.println("Weight: " + entry.getValue());
//            }
//        }

        double bestNeuronActivation = 0;
        int bestNeuronIndex = 0;
        for (int i = 0; i < outputNeurons.size(); i++) {
            double curNeuronValue = outputNeurons.get(i).getValue();
            if (curNeuronValue > bestNeuronActivation) {
                bestNeuronActivation = curNeuronValue;
                bestNeuronIndex = i;
            }
        }
        int chosenCol = -1;
        int chosenRot = -1;
        switch (piece) {
            case "I":
                if (bestNeuronIndex < 10) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else {
                    chosenCol = bestNeuronIndex - 10;
                    chosenRot = 3;
                }
                break;
            case "L":
                if (bestNeuronIndex < 9) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else if (bestNeuronIndex < 17) {
                    chosenCol = bestNeuronIndex - 9;
                    chosenRot = 2;
                } else if (bestNeuronIndex < 26) {
                    chosenCol = bestNeuronIndex - 17;
                    chosenRot = 3;
                } else {
                    chosenCol = bestNeuronIndex - 26;
                    chosenRot = 4;
                }
                break;
            case "RL":
                if (bestNeuronIndex < 9) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else if (bestNeuronIndex < 17) {
                    chosenCol = bestNeuronIndex - 9;
                    chosenRot = 2;
                } else if (bestNeuronIndex < 26) {
                    chosenCol = bestNeuronIndex - 17;
                    chosenRot = 3;
                } else {
                    chosenCol = bestNeuronIndex - 26;
                    chosenRot = 4;
                }
                break;
            case "S":
                if (bestNeuronIndex < 9) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else {
                    chosenCol = bestNeuronIndex - 9;
                    chosenRot = 3;
                }
                break;
            case "Z":
                if (bestNeuronIndex < 9) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else {
                    chosenCol = bestNeuronIndex - 9;
                    chosenRot = 3;
                }
                break;
            case "C":
                chosenCol = bestNeuronIndex;
                chosenRot = 1;
                break;
            case "T":
                if (bestNeuronIndex < 8) {
                    chosenCol = bestNeuronIndex;
                    chosenRot = 1;
                } else if (bestNeuronIndex < 17) {
                    chosenCol = bestNeuronIndex - 8;
                    chosenRot = 2;
                } else if (bestNeuronIndex < 25) {
                    chosenCol = bestNeuronIndex - 16;
                    chosenRot = 3;
                } else {
                    chosenCol = bestNeuronIndex - 26;
                    chosenRot = 4;
                }
                break;
        }

        int randomChance = random.nextInt(10);
        Decision decision = null;
        if (randomChance == 0) {
            chosenRot = random.nextInt(4) + 1;
            chosenCol = random.nextInt(7);
        }
        decision = new Decision(this.getInputNeurons(), piece, chosenRot, chosenCol);
        decisionsToLearnFrom.add(decision);
        return new Output(chosenRot, chosenCol);
    }

    public void learn(boolean positiveChange, boolean bigChange) {
//        System.out.println(decisionsToLearnFrom.size());
//        System.out.println(inputNeurons.size());
//        System.out.println(firstHiddenNeuronLayer.size());
//        System.out.println(secondHiddenNeuronLayer.size());
//        System.out.println(outputNeurons.size());
//        System.out.println();

//        decisionsToLearnFrom = new ArrayList<>();
        for (Decision decision : decisionsToLearnFrom) {
            int outputNeuronIndex = getNeuronIndexByPieceColRot(decision.getPiece(), decision.getRot(), decision.getCol());
            Neuron outputNeuron = outputNeurons.get(outputNeuronIndex);

            List<Neuron> orderedNeurons = new ArrayList<>(outputNeuron.getConnections().keySet()); // list of second layer neurons
            orderedNeurons.sort(Comparator.comparingDouble(Neuron::getValue));

//            ///////////////////////////////////////TODO FIND PROBLEM !!!!!!!/////////////////////////////////////////////////////////////
//
//            for (int i = 7; i < orderedNeurons.size(); i++) { // most active second layer neurons
//                Neuron curSecondLayerNeuron = orderedNeurons.get(i); // second layer neuron
//                outputNeuron.getConnections().put(curSecondLayerNeuron, curSecondLayerNeuron.getValue() - 0.2); // set output neuron weights
//
//                List<Neuron> orderedFirstLayerNeurons = new ArrayList<>(curSecondLayerNeuron.getConnections().keySet());
//                orderedFirstLayerNeurons.sort(Comparator.comparingDouble(Neuron::getValue));
//                for (int j = 7; j < orderedFirstLayerNeurons.size(); j++) { // most active first layer neurons
//                    Neuron curFirstLayerNeuron = orderedFirstLayerNeurons.get(i); // first layer neuron
//                    curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curFirstLayerNeuron.getValue() - 0.2); // set cur second layer neuron weights
//
////                    for (Neuron inputNeuron : decision.getInputs()) {
////                        if (inputNeuron.getValue() == 1) {
////                            curFirstLayerNeuron.getConnections().put(inputNeuron, inputNeuron.getValue() - 0.2); // set cur first layer neuron weights
////                        }
////                    }
//                }
//            }
//            //////////////////////////////////////TODO FIND PROBLEM !!!!!!!//////////////////////////////////////////////////////////////

            if (positiveChange) {
///////////////////////////////////////////////
////                for (Neuron outputNeuron : outputNeurons) {
////                    System.out.println(outputNeuron.getValue());
////                }
////                System.out.println();
////                for (Neuron orderedNeuron : orderedNeurons) {
////                    System.out.println(orderedNeuron.getValue()); // DEBUG
////                }
////                System.out.println();
////
////                for (int i = 5; i < orderedNeurons.size(); i++) {
////                    System.out.println(orderedNeurons.get(i).getValue());
////                }
////                System.out.println();
////
////
////                for (Neuron outputNeuron : outputNeurons) {
////                    System.out.println(outputNeuron.getValue());
////                }
////                System.out.println();
///////////////////////////////////////////////
                if (bigChange) {
                    outputNeuron.setBias(outputNeuron.getBias() + 1); // not sure
                    for (int i = 0; i < orderedNeurons.size(); i++) { // most active second layer neurons
                        Neuron curSecondLayerNeuron = orderedNeurons.get(i); // second layer neuron
                        if (i < 9) {
                            outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) - 0.09); // set output neuron weights
                            curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() - 0.5); // adjusting bias if ai does good
                        } else {
                            outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) + 0.15); // set output neuron weights
                            curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() + 1); // adjusting bias if ai does good
                        }


                        List<Neuron> orderedFirstLayerNeurons = new ArrayList<>(curSecondLayerNeuron.getConnections().keySet());
                        orderedFirstLayerNeurons.sort(Comparator.comparingDouble(Neuron::getValue));
                        for (int j = 0; j < orderedFirstLayerNeurons.size(); j++) { // most active first layer neurons
                            Neuron curFirstLayerNeuron = orderedFirstLayerNeurons.get(i); // first layer neuron
                            if (j < 9) {
                                curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) - 0.09); // set cur second layer neuron weights
                                curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() - 0.5);
                            } else {
                                curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) + 0.15); // set cur second layer neuron weights
                                curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() + 1);
                            }

                            for (int k = 0; k < decision.getInputs().size(); k++) {
                                InputNeuron inputNeuron = decision.getInputs().get(k);
                                InputNeuron realNeuron = inputNeurons.get(k);
                                if (inputNeuron.getValue() == 1) {
                                    curFirstLayerNeuron.getConnections().put(realNeuron, curFirstLayerNeuron.getConnections().get(realNeuron) + 0.21); // set cur first layer neuron weights
                                }
                            }
                        }
                    }
                } else {
                    outputNeuron.setBias(outputNeuron.getBias() + 0.2);
                    for (int i = 0; i < orderedNeurons.size(); i++) { // most active second layer neurons
                        Neuron curSecondLayerNeuron = orderedNeurons.get(i); // second layer neuron
                        if (i < 9) {
                            outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) - 0.02); // set output neuron weights
                            curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() - 0.1);
                        } else {
                            outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) + 0.05); // set output neuron weights
                            curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() + 0.2);
                        }

                        List<Neuron> orderedFirstLayerNeurons = new ArrayList<>(curSecondLayerNeuron.getConnections().keySet());
                        orderedFirstLayerNeurons.sort(Comparator.comparingDouble(Neuron::getValue));
                        for (int j = 0; j < orderedFirstLayerNeurons.size(); j++) { // most active first layer neurons
                            Neuron curFirstLayerNeuron = orderedFirstLayerNeurons.get(i); // first layer neuron
                            if (j < 9) {
                                curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) - 0.02); // set cur second layer neuron weights
                                curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() - 0.1);
                            } else {
                                curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) + 0.05); // set cur second layer neuron weights
                                curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() + 0.2);
                            }

                            for (int k = 0; k < decision.getInputs().size(); k++) {
                                InputNeuron inputNeuron = decision.getInputs().get(k);
                                InputNeuron realNeuron = inputNeurons.get(k);
                                if (inputNeuron.getValue() == 1) {
                                    curFirstLayerNeuron.getConnections().put(realNeuron, curFirstLayerNeuron.getConnections().get(realNeuron) + 0.1); // set cur first layer neuron weights
                                }
                            }
                        }
                    }
                }
            } else {
                outputNeuron.setBias(outputNeuron.getBias() - 0.2);
                for (int i = 0; i < orderedNeurons.size(); i++) { // most active second layer neurons
                    Neuron curSecondLayerNeuron = orderedNeurons.get(i); // second layer neuron
//                    System.out.println("Output weight: " + outputNeuron.getConnections().get(curSecondLayerNeuron) + " ---> " + (outputNeuron.getConnections().get(curSecondLayerNeuron) - 0.1)); //debug weights
                    if (i < 9) {
                        outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) + 0.02); // set output neuron weights
                        curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() + 0.1);
                    } else {
                        outputNeuron.getConnections().put(curSecondLayerNeuron, outputNeuron.getConnections().get(curSecondLayerNeuron) - 0.05); // set output neuron weights
                        curSecondLayerNeuron.setBias(curSecondLayerNeuron.getBias() - 0.2);
                    }

                    List<Neuron> orderedFirstLayerNeurons = new ArrayList<>(curSecondLayerNeuron.getConnections().keySet());
                    orderedFirstLayerNeurons.sort(Comparator.comparingDouble(Neuron::getValue));
                    for (int j = 0; j < orderedFirstLayerNeurons.size(); j++) { // most active first layer neurons
                        Neuron curFirstLayerNeuron = orderedFirstLayerNeurons.get(i); // first layer neuron
                        if (j < 9) {
                            curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) + 0.02); // set cur second layer neuron weights
                            curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() + 0.1);
                        } else {
                            curSecondLayerNeuron.getConnections().put(curFirstLayerNeuron, curSecondLayerNeuron.getConnections().get(curFirstLayerNeuron) - 0.05); // set cur second layer neuron weights
                            curFirstLayerNeuron.setBias(curFirstLayerNeuron.getBias() - 0.2);
                        }


                        for (int k = 0; k < decision.getInputs().size(); k++) {
                            InputNeuron inputNeuron = decision.getInputs().get(k);
                            InputNeuron realNeuron = inputNeurons.get(k);
                            if (inputNeuron.getValue() == 1) {
                                curFirstLayerNeuron.getConnections().put(realNeuron, curFirstLayerNeuron.getConnections().get(realNeuron) - 0.1); // set cur first layer neuron weights
                            }
                        }
                    }
                }
            }
            /////////////////////////////////////////
//            for (Neuron neuron : firstHiddenNeuronLayer) {
//                for (Map.Entry<Neuron,Double> entry : neuron.getConnections().entrySet()) {
//                    System.out.println("First Layer Weights: " + entry.getValue());
//                }
//            }
//            for (Neuron neuron : secondHiddenNeuronLayer) {
//                for (Map.Entry<Neuron,Double> entry : neuron.getConnections().entrySet()) {
//                    System.out.println("Second Layer Weights: " + entry.getValue());
//                }
//            }
//            for (Neuron neuron : outputNeurons) {
//                for (Map.Entry<Neuron,Double> entry : neuron.getConnections().entrySet()) {
//                    System.out.println("Output Layer Weights: " + entry.getValue());
//                }
//            }
            ///////////////////////////////
        }
        this.decisionsToLearnFrom = new ArrayList<>();
    }

    private int getNeuronIndexByPieceColRot(String piece, int rot, int col) {
        switch (piece) {
            case "I":
                switch (rot) {
                    case 1:
                    case 2:
                        return col;
                    case 3:
                    case 4:
                        return col + 10;
                }
                break;
            case "L":
                switch (rot) {
                    case 1:
                        return col;
                    case 2:
                        return col + 9;
                    case 3:
                        return col + 17;
                    case 4:
                        return col + 26;
                }
                break;
            case "RL":
                switch (rot) {
                    case 1:
                        return col;
                    case 2:
                        return col + 9;
                    case 3:
                        return col + 17;
                    case 4:
                        return col + 26;
                }
                break;
            case "S":
                switch (rot) {
                    case 1:
                    case 2:
                        return col;
                    case 3:
                    case 4:
                        return col + 9;
                }
                break;
            case "Z":
                switch (rot) {
                    case 1:
                    case 2:
                        return col;
                    case 3:
                    case 4:
                        return col + 9;
                }
                break;
            case "C":
                return col;
            case "T":
                switch (rot) {
                    case 1:
                        return col;
                    case 2:
                        return col + 8;
                    case 3:
                        return col + 17;
                    case 4:
                        return col + 26;
                }
                break;
        }
        return 0;
    }

    public List<Decision> getDecisionsToLearnFrom() {
        return decisionsToLearnFrom;
    }

    public void setDecisionsToLearnFrom(List<Decision> decisionsToLearnFrom) {
        this.decisionsToLearnFrom = decisionsToLearnFrom;
    }
}
