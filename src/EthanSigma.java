import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class EthanSigma extends Player {
    enum State {Start, Invade, Block, Fill}

    private BlokusBoard board;

    public EthanSigma(int color, String name) {
        super(color, name);
        state = State.Start;
    }

    public int[][] zones;

    public static final int I1 = 0;
    public static final int I2 = 1;
    public static final int I3 = 2;
    public static final int V3 = 3;
    public static final int I4 = 4;
    public static final int L4 = 5;
    public static final int T4 = 6;
    public static final int O4 = 7;
    public static final int Z4 = 8;
    public static final int I5 = 9;
    public static final int L5 = 10;
    public static final int N5 = 11;
    public static final int P5 = 12;
    public static final int U5 = 11;
    public static final int Y5 = 12;
    public static final int T5 = 13;
    public static final int V5 = 14;
    public static final int W5 = 15;
    public static final int Z5 = 16;
    public static final int F5 = 17;
    public static final int X5 = 18;

    public static int getSize(int shape) {
        switch (shape) {
            case I1:
                return 1;
            case I2:
                return 2;
            case I3:
            case V3:
                return 3;
            case I4:
            case L4:
            case T4:
            case O4:
            case Z4:
                return 4;
            default:
                return 5;
        }
    }

    State state;

    /**
     * Returns a valid move.
     *
     * @param board - the board that a move should be made on
     * @return a valid move, null if non can be found
     */

    public Move getMove(BlokusBoard board) {
        boolean starting = true;
        for (int[] i : board.getBoard())
            for (int x : i)
                if (x == getColor()) {
                    starting = false;
                    break;
                }
        if (starting)
            state = State.Start;
        UpdateZones(board.getBoard());

        SigmoidMove end = null;

        ArrayList<IntPoint> availableMoves = board.moveLocations(getColor());
        ArrayList<Integer> usableShapePositions = new ArrayList<>();

        boolean[] used = (getColor() == BlokusBoard.ORANGE) ? board.getOrangeUsedShapes() : board.getPurpleUsedShapes();
        for (int x = 0; x < used.length; x++)
            if (!used[x])
                usableShapePositions.add(x);
        if (usableShapePositions.isEmpty() || availableMoves.isEmpty())
            return null;

        switch (state) {
            case Start:
                end = new SigmoidMove(P5, Math.random() < .5, new Random().nextInt(4), availableMoves.get(0));
                state = State.Invade;
                break;
            case Invade:
                for (int spotX = 0; spotX < 14; spotX++) {
                    for (int spotY = 0; spotY < 14; spotY++) {
                        for (int shape = 0; shape < usableShapePositions.size(); shape++) {
                            for (int rotNo = 0; rotNo < 4; rotNo++) {
                                for (int flip = 0; flip < 2; flip++) {
                                    SigmoidMove curr = new SigmoidMove(usableShapePositions.get(shape), flip == 0, rotNo, new IntPoint(spotX, spotY));
                                    if (board.isValidMove(curr, getColor())) {
                                        end = (end == null || end.getScore(State.Invade, zones, getColor(), board) < curr.getScore(State.Invade, zones, getColor(), board)) ? curr : end;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case Fill:
                break;
            case Block:
                break;
        }
        //fail-safe
        if (end == null) {
            for (int spotX = 0; spotX < 14; spotX++) {
                for (int spotY = 0; spotY < 14; spotY++) {
                    for (int shape = usableShapePositions.size() - 1; shape >= 0; shape--) {
                        for (int rotNo = 0; rotNo < 4; rotNo++) {
                            for (int flip = 0; flip < 2; flip++) {
                                SigmoidMove curr = new SigmoidMove(usableShapePositions.get(shape), flip == 0, rotNo, new IntPoint(spotX, spotY));
                                if (board.isValidMove(curr, getColor())) {
                                    end = (end == null || Math.random() < .7) ? curr : end;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (end != null)
            if (!board.isValidMove(end, getColor()))
                System.out.println("BAD MOVE");
        if (end != null)
            System.out.println(end.getPoint());
        return end;
        /*
        //System.out.println("my color is "+getColor() + " the turn is "+board.getTurn());
        ArrayList<IntPoint> availableMoves = board.moveLocations(getColor());
        Collections.shuffle(availableMoves);
        //System.out.println("available move locations "+availableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
                usableShapePositions.add(x);
        //System.out.println("usable pieces "+ Arrays.toString(used));
        Collections.shuffle(usableShapePositions);
        if(usableShapePositions.isEmpty() ||availableMoves.isEmpty())
            return null;
        else
        {
            //System.out.println("hi");
            Move move = null;
            for(IntPoint movLoc: availableMoves)
                for(Integer position: usableShapePositions)
                {
                    for(int i=0; i<8;i++) {
                        boolean flip = i >= 3;
                        int rotation = i % 4;
                        boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                        for (int r = -shape.length+1; r <shape.length;  r++)
                            for (int c = -shape[0].length+1; c < shape[0].length; c++)
                            {
                                IntPoint topLeft = new IntPoint(movLoc.getX()+c,movLoc.getY()+r);
                                Move test = new Move(position,flip,rotation,topLeft);
                                if(board.isValidMove(test,getColor()))
                                    return test;
                            }
                    }
                }
            return null;
        }
        */

    }

    public void UpdateZones(int[][] newBoard) {
        zones = newBoard;
        int[][] copy = new int[14][14];
        for (int r = 0; r < 14; r++)
            for (int c = 0; c < 14; c++)
                copy[r][c] = zones[r][c];
        boolean go = true;
        for (int r = 0; r < 14; r++)
            for (int c = 0; c < 14; c++) {
                if (zones[r][c] == BlokusBoard.EMPTY) {
                    int numO, numP = numO = 0;
                    for (int x = -3; x < 4; x++)
                        for (int y = -3; y < 4; y++)
                            try {
                                if (zones[r + y][c + x] == BlokusBoard.ORANGE)
                                    numO++;
                                else if (zones[r + y][c + x] == BlokusBoard.PURPLE)
                                    numP++;
                            } catch (Exception e) {
                            }
                    copy[r][c] = numO > numP ? BlokusBoard.ORANGE : (numP > 0 ? BlokusBoard.PURPLE : BlokusBoard.EMPTY);
                }
            }
            /*
        for (int[] z : copy) {
            for (int z1 : z)
                System.out.print(z1 == getColor() ? "O" : ".");
            System.out.println("");
        }
        System.out.println("");
        */
        zones = copy;
    }

    /**
     * Returns a clone of the player
     *
     * @return a clone of this player
     */

    public Player freshCopy() {
        return new EthanSigma(getColor(), getName());
    }
}

class SigmoidMove extends Move {
    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point) {
        super(pieceNumber, flip, rotation, point);
    }

    public double getScore(EthanSigma.State state, int[][] zone, int color, BlokusBoard board) {
        if (!board.isValidMove(this, color))
            return 0;
        int end = 0;
        switch (state) {
            case Invade:
                board = new BlokusBoard(board);
                board.makeMove(this, color);
                board.changeTurns();
                ArrayList<IntPoint> availableMoves = color == BlokusBoard.ORANGE ? board.getOrangeMoveLocations() : board.getPurpleMoveLocations();
                for (IntPoint point : availableMoves)
                    end += 196 - distanceToZone(point, zone, color == BlokusBoard.ORANGE ? BlokusBoard.PURPLE : BlokusBoard.ORANGE);
                //for each spot made available, score+= 196-distance to zone
                break;
        }
        end *= EthanSigma.getSize(getPieceNumber());
        end /= ((color == BlokusBoard.PURPLE ? board.getOrangeMoveLocations() : board.getPurpleMoveLocations()).size()) + 1;
        board.undoMovePiece(this, color);
        return end;
    }

    public double distanceToZone(IntPoint point, int[][] zone, int color) {
        double end = 1000;
        for (int r = 0; r < 14; r++)
            for (int c = 0; c < 14; c++) {
                double newEnd = 1000;
                if (zone[r][c] == color) {
                    newEnd = Math.abs(Math.sqrt(Math.pow(c - point.getX(), 2) + Math.pow(r - point.getY(), 2)));
                    if (newEnd < end)
                        end = newEnd;
                }
            }
        return end;
    }
}

