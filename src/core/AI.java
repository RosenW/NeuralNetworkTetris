package core;

import misc.Output;
import models.Network;

import java.util.Map;

public class AI {
    private Network INetwork = new Network(17);
    private Network LNetwork = new Network(34);
    private Network RLNetwork = new Network(34);
    private Network SNetwork = new Network(17);
    private Network ZNetwork = new Network(17);
    private Network CNetwork = new Network(9);
    private Network TNetwork = new Network(34);

    public void generateAnswer(int[][] playfield, String piece, Game game) {

        Output output;
        switch (piece) {
            case "I":
                output = INetwork.generateOutput(playfield, "I");
                break;
            case "L":
                output = LNetwork.generateOutput(playfield, "L");
                break;
            case "RL":
                output = RLNetwork.generateOutput(playfield, "RL");
                break;
            case "S":
                output = SNetwork.generateOutput(playfield, "S");
                break;
            case "Z":
                output = ZNetwork.generateOutput(playfield, "Z");
                break;
            case "C":
                output = CNetwork.generateOutput(playfield, "C");
                break;
            case "T":
                output = TNetwork.generateOutput(playfield, "T");
                break;
            default:
                output = new Output(0, 0);
                break;
        }

        int rotation = output.getRotation();
        int column = output.getColumn();
        game.placePiece(piece, rotation, column);
    }

    public void learn(boolean positiveChange, boolean bigChange) {
//        System.out.println(
//                        INetwork.getDecisionsToLearnFrom().size() +
//                        LNetwork.getDecisionsToLearnFrom().size() +
//                        RLNetwork.getDecisionsToLearnFrom().size() +
//                        SNetwork.getDecisionsToLearnFrom().size() +
//                        ZNetwork.getDecisionsToLearnFrom().size() +
//                        CNetwork.getDecisionsToLearnFrom().size() +
//                        TNetwork.getDecisionsToLearnFrom().size());
        INetwork.learn(positiveChange, bigChange);
        LNetwork.learn(positiveChange, bigChange);
        RLNetwork.learn(positiveChange, bigChange);
        SNetwork.learn(positiveChange, bigChange);
        ZNetwork.learn(positiveChange, bigChange);
        CNetwork.learn(positiveChange, bigChange);
        TNetwork.learn(positiveChange, bigChange);
    }
}
