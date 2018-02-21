package core;

import display.*;
import display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    private static final int SCALE = 3;
    private static final int WIDTH = 320 * SCALE;
    private static final int HEIGHT = WIDTH * 9 / 12;
    private static final String TITLE = "Tetris";
    private static Random RANDOM = new Random();
    private int score = 0;
    private int lines = 0;
    private int bestScore = 0;
    private int bestLines = 0;
    private static List<Integer> lastFiftyScores = new ArrayList<>();
    private int averageScore = 0;
    private boolean running = false;
    private Thread thread;
    private Window window;
    private AI bot = new AI();
    private int[][] playfield = new int[10][20];

    public Game() {
        window = new display.Window(WIDTH, HEIGHT, TITLE, this);
    }

    @Override
    public void run() {
        while (running) {
            tick();
            render();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        while (running) {
//            long lastTime = System.nanoTime();
//            double amountOfTicks = 20.0;
//            double ns = 1000000000 / amountOfTicks;
//            double delta = 0;
//            long timer = System.currentTimeMillis();
//            int frames = 0;
//            while (running) {
//
//                long now = System.nanoTime();
//                delta += (now - lastTime) / ns;
//                lastTime = now;
//
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                while (delta >= 1) {
//                    tick();
//                    delta--;
//                }
//                if (running) {
//                    render();
//                }
//                frames++;
//                if (System.currentTimeMillis() - timer > 1000) {
//                    timer += 1000;
//                    frames = 0;
//                }
//            }
//            stop();
//        }
    }

    private void tick() {
//        generateRandomPiecePlacement();
        String piece = getRandomPiece();
        bot.generateAnswer(playfield, piece, this);
        checkBoard();
//        debugBoard();
    }

    private void debugBoard() {
        for (int i = 0; i < playfield[0].length; i++) {
            for (int j = 0; j < playfield.length; j++) { //For debug
                System.out.print(playfield[j][i]);
            }
            System.out.println();
        }
    }


    private void checkBoard() {
        int squaresFilled;
        for (int recheck = 0; recheck < 2; recheck++) {
            for (int y = playfield[0].length - 1; y >= 0; y--) {
                squaresFilled = 0;
                for (int x = playfield.length - 1; x >= 0; x--) {
                    if (playfield[x][y] != 0) {
                        squaresFilled++;
                    }
                }
                if (squaresFilled == 10) {
                    for (int row = y; row >= 0; row--) {
                        for (int x = playfield.length - 1; x >= 0; x--) {
                            if (row != 0) {
                                playfield[x][row] = playfield[x][row - 1];
                            } else {
                                playfield[x][row] = 0;
                            }
                        }
                    }
                    score += 100;
                    lines += 1;
                }
            }
        }
        for (int x = 0; x < playfield.length; x++) {
            if (playfield[x][0] != 0) {
                boolean pos = false;
                boolean big = false;

                if (score > averageScore+10 && score > 50) {
                    if (score > averageScore + 100) {
                        pos = true;
                        big = true;
                    } else {
                        pos = true;
                        big = false;
                    }
                }
                this.bot.learn(pos, big);
                clearBoard();
                break;
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(Color.BLACK);
        for (int i = 0; i <= playfield.length; i++) {

            g.drawLine(32 * i, 0, 32 * i, 20 * 32);
        }
        for (int j = 0; j <= playfield[0].length; j++) {

            g.drawLine(0, 32 * j, 10 * 32, 32 * j);
        }

        for (int i = 0; i < playfield.length; i++) {
            for (int j = 0; j < playfield[0].length; j++) {
                switch (playfield[i][j]) {
                    case 1:
                        g.setColor(Color.cyan);
                        break;
                    case 2:
                        g.setColor(Color.orange);
                        break;
                    case 3:
                        g.setColor(Color.blue);
                        break;
                    case 4:
                        g.setColor(Color.green);
                        break;
                    case 5:
                        g.setColor(Color.red);
                        break;
                    case 6:
                        g.setColor(Color.yellow);
                        break;
                    case 7:
                        g.setColor(Color.magenta);
                        break;


                }
                if (playfield[i][j] != 0) {
                    g.fillRect(i * 32 + 1, j * 32 + 1, 31, 31);
                }
            }
        }

        g.setFont(new Font("Ariel", 0, 25));
        g.setColor(Color.black);
        g.drawString(String.format("Score: %s", score), 350, 30);
        g.drawString(String.format("Lines: %s", lines), 350, 60);
        g.drawString(String.format("Best Score: %s", bestScore), 350, 90);
        g.drawString(String.format("Best Lines: %s", bestLines), 350, 120);
        g.drawString(String.format("Average Score: %s", averageScore), 350, 150);
        g.drawString("Last 50:", 350, 230);

        g.setFont(new Font("Ariel", 0, 20));

        for (int i = 0; i < lastFiftyScores.size(); i++) {
            int curScore = lastFiftyScores.get(i);
            if (curScore > this.averageScore + 100) {
                g.setColor(Color.yellow);
            } else if (curScore > this.averageScore + 10 && curScore > 50) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.drawString(String.format("%s, ", curScore), 350 + ((i % 10) * 50), 260 + ((i / 10) * 40));
        }

        g.dispose();
        bs.show();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void placePiece(String type, int rotation, int column) {
        score += 5;
        try {
            switch (type) {
                case "I":
                    switch (rotation) {
                        case 1:
                        case 2:
                            for (int i = 0; i < playfield[0].length; i++) {

                                if (playfield[column][i] != 0) {
                                    for (int j = i - 1; j > i - 1 - 4; j--) {
                                        playfield[column][j] = 1;
                                    }
                                    return;
                                }
                            }
                            if (playfield[column][19] == 0) {
                                for (int j = 19; j > 19 - 4; j--) {
                                    playfield[column][j] = 1;
                                }
                                return;
                            }
                            break;
                        case 3:
                        case 4:
                            if (column > 6) column = 6;
                            for (int i = 0; i < playfield[0].length; i++) {
                                for (int k = column; k < column + 4; k++) {
                                    if (playfield[k][i] != 0) {
                                        for (int j = column; j < column + 4; j++) {
                                            playfield[j][i - 1] = 1;
                                        }
                                        return;
                                    }
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0 && playfield[column + 2][19] == 0 && playfield[column + 3][19] == 0) {
                                for (int j = column; j < column + 4; j++) {
                                    playfield[j][19] = 1;
                                }
                                break;
                            }
                            break;
                    }
                    break;
                case "L":
                    switch (rotation) {
                        case 1:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                for (int k = column; k < column + 2; k++) {
                                    if (playfield[k][i] != 0) {
                                        playfield[column][i - 1] = 2;
                                        playfield[column + 1][i - 1] = 2;
                                        playfield[column][i - 2] = 2;
                                        playfield[column][i - 3] = 2;
                                        return;
                                    }
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0) {
                                playfield[column][19] = 2;
                                playfield[column + 1][19] = 2;
                                playfield[column][18] = 2;
                                playfield[column][17] = 2;
                                break;
                            }
                            break;
                        case 2:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                for (int k = column; k < column + 3; k++) {
                                    if (playfield[k][i] != 0) {
                                        playfield[column][i - 1] = 2;
                                        playfield[column + 1][i - 1] = 2;
                                        playfield[column + 2][i - 1] = 2;
                                        playfield[column + 2][i - 2] = 2;
                                        return;
                                    }
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0 && playfield[column + 2][19] == 0) {
                                playfield[column][19] = 2;
                                playfield[column + 1][19] = 2;
                                playfield[column + 2][19] = 2;
                                playfield[column + 2][18] = 2;
                                break;
                            }
                            break;
                        case 3:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i + 2] != 0) {
                                        playfield[column][i - 1] = 2;
                                        playfield[column + 1][i - 1] = 2;
                                        playfield[column + 1][i] = 2;
                                        playfield[column + 1][i + 1] = 2;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column + 1][19] == 0) {
                                playfield[column + 1][19] = 2;
                                playfield[column + 1][18] = 2;
                                playfield[column + 1][17] = 2;
                                playfield[column][17] = 2;
                                break;
                            }
                            break;
                        case 4:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i - 1] != 0 || playfield[column + 2][i - 1] != 0) {
                                        playfield[column][i - 1] = 2;
                                        playfield[column][i - 2] = 2;
                                        playfield[column + 1][i - 2] = 2;
                                        playfield[column + 2][i - 2] = 2;
                                        return;
                                    }
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                            if (playfield[column][19] == 0) {
                                playfield[column][19] = 2;
                                playfield[column][18] = 2;
                                playfield[column + 1][18] = 2;
                                playfield[column + 2][18] = 2;
                                break;
                            }
                            break;
                    }
                    break;
                case "RL":
                    switch (rotation) {
                        case 1:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                for (int k = column; k < column + 2; k++) {
                                    if (playfield[k][i] != 0) {
                                        playfield[column][i - 1] = 3;
                                        playfield[column + 1][i - 1] = 3;
                                        playfield[column + 1][i - 2] = 3;
                                        playfield[column + 1][i - 3] = 3;
                                        return;
                                    }
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0) {
                                playfield[column][19] = 3;
                                playfield[column + 1][19] = 3;
                                playfield[column + 1][18] = 3;
                                playfield[column + 1][17] = 3;
                                break;
                            }
                            break;
                        case 2:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i] != 0 || playfield[column + 2][i + 1] != 0) {
                                        playfield[column][i - 1] = 3;
                                        playfield[column + 1][i - 1] = 3;
                                        playfield[column + 2][i - 1] = 3;
                                        playfield[column + 2][i] = 3;
                                        return;
                                    }
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                            if (playfield[column + 2][19] == 0) {
                                playfield[column + 2][19] = 3;
                                playfield[column + 2][18] = 3;
                                playfield[column + 1][18] = 3;
                                playfield[column][18] = 3;
                                break;
                            }
                            break;
                        case 3:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i - 2] != 0) {
                                        playfield[column][i - 1] = 3;
                                        playfield[column][i - 2] = 3;
                                        playfield[column][i - 3] = 3;
                                        playfield[column + 1][i - 3] = 3;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column][19] == 0) {
                                playfield[column][19] = 3;
                                playfield[column][18] = 3;
                                playfield[column][17] = 3;
                                playfield[column + 1][17] = 3;
                                break;
                            }
                            break;
                        case 4:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                for (int k = column; k < column + 3; k++) {
                                    if (playfield[k][i] != 0) {
                                        playfield[column][i - 1] = 3;
                                        playfield[column + 1][i - 1] = 3;
                                        playfield[column + 2][i - 1] = 3;
                                        playfield[column][i - 2] = 3;
                                        return;
                                    }
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0 && playfield[column + 2][19] == 0) {
                                playfield[column][19] = 3;
                                playfield[column + 1][19] = 3;
                                playfield[column + 2][19] = 3;
                                playfield[column][18] = 3;
                                break;
                            }
                            break;
                    }
                    break;
                case "S":
                    switch (rotation) {
                        case 1:
                        case 2:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i + 1] != 0) {
                                        playfield[column][i - 1] = 4;
                                        playfield[column][i - 2] = 4;
                                        playfield[column + 1][i - 1] = 4;
                                        playfield[column + 1][i] = 4;
                                        return;

                                    }
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                            if (playfield[column + 1][19] == 0) {
                                playfield[column][18] = 4;
                                playfield[column][17] = 4;
                                playfield[column + 1][18] = 4;
                                playfield[column + 1][19] = 4;
                                break;
                            }
                            break;
                        case 3:
                        case 4:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i] != 0 || playfield[column + 2][i - 1] != 0) {
                                        playfield[column][i - 1] = 4;
                                        playfield[column + 1][i - 1] = 4;
                                        playfield[column + 1][i - 2] = 4;
                                        playfield[column + 2][i - 2] = 4;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0) {
                                playfield[column][19] = 4;
                                playfield[column + 1][19] = 4;
                                playfield[column + 1][18] = 4;
                                playfield[column + 2][18] = 4;
                                break;
                            }
                            break;
                    }
                    break;
                case "Z":
                    switch (rotation) {
                        case 1:
                        case 2:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i - 1] != 0) {
                                        playfield[column][i - 1] = 5;
                                        playfield[column][i - 2] = 5;
                                        playfield[column + 1][i - 2] = 5;
                                        playfield[column + 1][i - 3] = 5;
                                        return;
                                    }
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                            if (playfield[column][19] == 0) {
                                playfield[column][19] = 5;
                                playfield[column][18] = 5;
                                playfield[column + 1][18] = 5;
                                playfield[column + 1][17] = 5;
                                break;
                            }
                            break;
                        case 3:
                        case 4:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i + 1] != 0 || playfield[column + 2][i + 1] != 0) {
                                        playfield[column][i - 1] = 5;
                                        playfield[column + 1][i - 1] = 5;
                                        playfield[column + 1][i] = 5;
                                        playfield[column + 2][i] = 5;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column + 1][19] == 0 && playfield[column + 2][19] == 0) {
                                playfield[column][18] = 5;
                                playfield[column + 1][18] = 5;
                                playfield[column + 1][19] = 5;
                                playfield[column + 2][19] = 5;
                                break;
                            }
                            break;
                    }
                    break;
                case "C":
                    if (column > 8) column = 8;
                    for (int i = 0; i < playfield[0].length; i++) {
                        for (int k = column; k < column + 2; k++) {
                            if (playfield[k][i] != 0) {
                                for (int j = column; j < column + 2; j++) {
                                    playfield[j][i - 1] = 6;
                                    playfield[j][i - 2] = 6;
                                }
                                return;
                            }
                        }
                    }
                    if (playfield[column][19] == 0 && playfield[column + 1][19] == 0) {
                        for (int j = column; j < column + 2; j++) {
                            playfield[j][19] = 6;
                            playfield[j][18] = 6;
                        }
                        break;
                    }
                    break;
                case "T":
                    switch (rotation) {
                        case 1:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                if (playfield[column][i] != 0 || playfield[column + 1][i] != 0 || playfield[column + 2][i] != 0) {
                                    playfield[column][i - 1] = 7;
                                    playfield[column + 1][i - 1] = 7;
                                    playfield[column + 1][i - 2] = 7;
                                    playfield[column + 2][i - 1] = 7;
                                    return;
                                }
                            }
                            if (playfield[column][19] == 0 && playfield[column + 1][19] == 0 && playfield[column + 2][19] == 0) {
                                playfield[column][19] = 7;
                                playfield[column + 1][19] = 7;
                                playfield[column + 1][18] = 7;
                                playfield[column + 2][19] = 7;
                                break;
                            }
                            break;
                        case 2:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i - 1] != 0) {
                                        playfield[column][i - 1] = 7;
                                        playfield[column][i - 2] = 7;
                                        playfield[column][i - 3] = 7;
                                        playfield[column + 1][i - 2] = 7;
                                        return;
                                    }
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                            if (playfield[column][19] == 0) {
                                playfield[column][19] = 7;
                                playfield[column][18] = 7;
                                playfield[column][17] = 7;
                                playfield[column + 1][18] = 7;
                                break;
                            }
                            break;
                        case 3:
                            if (column > 8) column = 8;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i + 1] != 0) {
                                        playfield[column][i - 1] = 7;
                                        playfield[column + 1][i - 2] = 7;
                                        playfield[column + 1][i - 1] = 7;
                                        playfield[column + 1][i] = 7;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column + 1][19] == 0) {
                                playfield[column][18] = 7;
                                playfield[column + 1][19] = 7;
                                playfield[column + 1][18] = 7;
                                playfield[column + 1][17] = 7;
                                break;
                            }
                            break;
                        case 4:
                            if (column > 7) column = 7;
                            for (int i = 0; i < playfield[0].length; i++) {
                                try {
                                    if (playfield[column][i] != 0 || playfield[column + 1][i + 1] != 0 || playfield[column + 2][i] != 0) {
                                        playfield[column][i - 1] = 7;
                                        playfield[column + 1][i - 1] = 7;
                                        playfield[column + 2][i - 1] = 7;
                                        playfield[column + 1][i] = 7;
                                        return;
                                    }
                                } catch (Exception e) {
                                    //do nothing
                                }
                            }
                            if (playfield[column + 1][19] == 0) {
                                playfield[column][18] = 7;
                                playfield[column + 1][18] = 7;
                                playfield[column + 2][18] = 7;
                                playfield[column + 1][19] = 7;
                                break;
                            }
                            break;
                    }
                    break;
                default:
                    break;

            }

        } catch (Exception e) {
            clearBoard();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < playfield.length; i++) {
            for (int j = 0; j < playfield[0].length; j++) {
                playfield[i][j] = 0;
            }
        }
        if (score > bestScore) {
            bestScore = score;
        }
        if (lines > bestLines) {
            bestLines = lines;
        }
        if (score > 50) {
            lastFiftyScores.add(score);
        }
        if (lastFiftyScores.size() > 50) {
            lastFiftyScores.remove(0);
        }
        if (lastFiftyScores.size() > 0) {
            averageScore = lastFiftyScores.stream().mapToInt(Integer::intValue).sum() / lastFiftyScores.size();
        }
        score = 0;
        lines = 0;
    }

    private void generateRandomPiecePlacement() {
        String type = getRandomPiece();
        int rot = RANDOM.nextInt(4) + 1;
        int col = RANDOM.nextInt(10);
        placePiece(type, rot, col);
    }

    private String getRandomPiece() {
        return getPieceTypeByIndex(RANDOM.nextInt(7));
    }

    private String getPieceTypeByIndex(int index) {
        switch (index) {
            case 0:
                return "I";
            case 1:
                return "L";
            case 2:
                return "RL";
            case 3:
                return "S";
            case 4:
                return "Z";
            case 5:
                return "C";
            case 6:
                return "T";

        }
        return null;
    }
}
