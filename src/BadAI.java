import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class BadAI extends Player
{

    public BadAI(int color,String name)
    {
        super(color, name);
    }


    public Move getMove(BlokusBoard board)
    {

       return new Move(0,false,0,new IntPoint(0,0));
    }


    private Move moveFromList(BlokusBoard board,ArrayList<IntPoint> avaialbeMoves, ArrayList<Integer> shapeLocs)
    {
        Move move = null;
        for(Integer position: shapeLocs)
            for(IntPoint movLoc: avaialbeMoves)
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

    /**
     * Returns a clone of the player
     * @return a clone of this player
     */
    public Player freshCopy()
    {
        return new BadAI(getColor(),getName());
    }
}
