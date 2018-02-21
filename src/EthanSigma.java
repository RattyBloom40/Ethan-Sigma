import java.util.ArrayList;
import java.util.Collections;

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

    int turn = 0;
    State state;

    /**
     * Returns a valid move.
     *
     * @param board - the board that a move should be made on
     * @return a valid move, null if non can be found
     */
    public Move getMove(BlokusBoard board) {
        //Update zones
        zones = board.getBoard();
        for (int r = 0; r < 14; r++)
            for (int c = 0; c < 14; c++) {
                //if()
            }

        SigmoidMove end = null;
        turn++;

        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        ArrayList<Integer> usableShapePositions = new ArrayList<>();

        boolean[] used = (getColor() == BlokusBoard.ORANGE) ? board.getOrangeUsedShapes() : board.getPurpleUsedShapes();
        for (int x = 0; x < used.length; x++)
            if (!used[x])
                usableShapePositions.add(x);
        if (usableShapePositions.isEmpty() || avaiableMoves.isEmpty())
            return null;

        switch (state) {
            case Start:
                end = Math.random() < .5 ? new SigmoidMove(P5, (Math.random() < .5 ? true : false), 0, avaiableMoves.get(0)) : new SigmoidMove(O4, (false), 0, avaiableMoves.get(0));
                break;
            case Invade:
                for(int spot=0;spot<avaiableMoves.size();spot++){
                    for(int shape=0;shape<usableShapePositions.size();shape++){
                        for(int rotNo=0;rotNo<4;rotNo++){
                            for(int flip=0;flip<2;flip++){
                                SigmoidMove curr = new SigmoidMove(usableShapePositions.get(shape),flip==0,rotNo,avaiableMoves.get(spot));
                                if(board.isValidMove(curr,getColor())){
                                    end = (end==null||end.getScore(State.Invade)<curr.getScore(State.Invade))?curr:end;
                                    if(Math.random()<.5&&end.getScore(State.Invade)==curr.getScore(State.Invade)){
                                        end = curr;
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

        return end;
        /*
        //System.out.println("my color is "+getColor() + " the turn is "+board.getTurn());
        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        Collections.shuffle(avaiableMoves);
        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
                usableShapePositions.add(x);
        //System.out.println("usable pieces "+ Arrays.toString(used));
        Collections.shuffle(usableShapePositions);
        if(usableShapePositions.isEmpty() ||avaiableMoves.isEmpty())
            return null;
        else
        {
            //System.out.println("hi");
            Move move = null;
            for(IntPoint movLoc: avaiableMoves)
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

    /**
     * Returns a clone of the player
     *
     * @return a clone of this player
     */
    public Player freshCopy() {
        return new EthanSigma(getColor(), getName());
    }
}
