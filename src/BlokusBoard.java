import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores all the game data for a blockus game
 */
public class BlokusBoard
{
    // Values used for status method
    public static final int ORANGE_WINS    = 0;
    public static final int PURPLE_WINS  = 1;
    public static final int TIE         = 2;
    public static final int PLAYING     = 3;
    public static final int SMALL_DIM = 10;
    public static final int LARGE_DIM = 30;

    // Values used for 2D board array.
    // They represent what pieces are at various positions
    public static final int ORANGE         = 4;
    public static final int PURPLE       = 5;
    public static final int EMPTY       = 6;

    // Stores the pieces that are on the board
    private int[][] board = new int[14][14];

    // Stores all the shapes
    private static ArrayList<Shape> shapes = null;

    // stores which shapes ornage has already used
    private boolean[] orangeUsedShapes;
    // stores which shapes purple has already used
    private boolean[] purpleUsedShapes;

    // Stores the font used for drawing text to the screen
    private Font font = new Font("Times New Roman",Font.BOLD,50);

    // Stores the current turn
    private int turn = ORANGE;

    // Stores the status of the game
    private int status= PLAYING;

    // stores if oranged skipped their last turn
    private boolean orangeSkipped = false;
    // stores if purple skipped their last turn
    private boolean purpleSkipped = false;

    // stores the custom color of purple
    private Color purple = new Color(163,73,163);

    /**
     * Creates the initial game board
     */
    public BlokusBoard()
    {

        if(shapes==null)
            loadShapes();

        orangeUsedShapes = new boolean[shapes.size()];
        purpleUsedShapes = new boolean[shapes.size()];

        reset();
    }

     /* *
     * Creates the board to be a copy of the received board.
     * Used to edit a board without changing the true game board.
     * Note: Players only receive copies of the true game board.
     * @param b - Board to be cloned
     */
    public BlokusBoard(BlokusBoard b)
    {
        if(shapes==null)
            loadShapes();

        for(int row=0; row < numRows(); row++)
            for(int col=0; col < numCols(); col++)
            {
                board[row][col] = b.getBoard()[row][col];
            }
        this.turn = b.getTurn();
        this.orangeSkipped=b.getOrangeSkipped();
        this.purpleSkipped=b.getPurpleSkipped();

        orangeUsedShapes = new boolean[shapes.size()];
        purpleUsedShapes = new boolean[shapes.size()];

        for(int i=0; i<orangeUsedShapes.length;i++)
        {
            orangeUsedShapes[i]=b.getOrangeUsedShapes()[i];
            purpleUsedShapes[i]=b.getPurpleUsedShapes()[i];
        }
    }

    /**
     * Returns if oranged skipped their last turn
     * @return true when orange skipped its last turn
     */
    public boolean getOrangeSkipped() {
        return orangeSkipped;
    }

    /**
     * Returns if purple skipped their last turn
     * @return - true when purple skipped its last turn
     */
    public boolean getPurpleSkipped() {
        return purpleSkipped;
    }

    /**
     * Returns a list of all the shapes that exist in Blockus
     * @return - all the shapes that exist in a Bluckus game
     */
    public static ArrayList<Shape> getShapes() {
        return shapes;
    }

    /**
     * returns the 2D array of the board
     * @return - the game board
     */
    public int[][] getBoard()
    {
        return board;
    }

    //

    /**
     * Returns the status of the game (ORANGE_WINS,PURPLE_WINS, TIE, or PLAYING
     * @return - the status of the game
     */
    public int status()
    {
        if(!orangeSkipped || !purpleSkipped)
            return PLAYING;
        else
        {
            int orangeCount = 101;
            int purpleCount = 101;

            for(int row=0; row < numRows(); row++)
                for(int col=0; col < numCols(); col++)
                {
                    if(board[row][col] == ORANGE)
                        orangeCount--;
                    if(board[row][col] == PURPLE)
                        purpleCount--;

                }
            if(purpleCount==orangeCount)
                status = TIE;
            else if(orangeCount<purpleCount)
                status = ORANGE_WINS;
            else
                status = PURPLE_WINS;

            return status;
        }
    }

    /**
     * Returns a text representation of the board
     * @return - text containing the 2d array of pieces on the game board
     */
    public String toString()
    {
        String s = "status "+status+"\n";
        for(int row=0; row < numRows(); row++){
            for(int col=0; col < numCols(); col++) {
                if(board[row][col] == PURPLE)
                    s+="P";
                else if(board[row][col] == ORANGE)
                    s+="B";
                else
                    s+="-";
            }
            s+="\n";
        }
        return s;
    }

    /**
     * Returns the number of rows on the game board.
     * @return - number of rows on the game board
     */
    public int numRows()
    {
        return board.length;
    }

    /**
     * Returns the number of columns on the game board.
     * @return - number of columns on the game board
     */
    public int numCols()
    {
        return board[0].length;
    }

    /**
     * Resets all data to an unplayed state
     */
    public void reset()
    {
        for(int r=0; r < numRows(); r++)
            for(int c=0; c < numCols(); c++)
            {
                board[r][c] = EMPTY;
            }
        turn = ORANGE;
        orangeSkipped=purpleSkipped=false;
        for(int i=0; i<orangeUsedShapes.length;i++)
            orangeUsedShapes[i]=purpleUsedShapes[i]=false;
    }

    /**
     * Returns a list which pieces orange has or has not used. The list is 21 big (1 slot for each peice)
     * When an index has true, that peice number has already been used
     * @return a list of which pieces have or have not been used by orange.
     */
    public boolean[] getOrangeUsedShapes() {
        return orangeUsedShapes;
    }

    /**
     * Returns a list which pieces purple has or has not used. The list is 21 big (1 slot for each peice)
     * When an index has true, that peice number has already been used
     * @return a list of which pieces have or have not been used by purple.
     */
    public boolean[] getPurpleUsedShapes() {
        return purpleUsedShapes;
    }

    /**
     * Draws all game to the given graphics
     * @param g - the graphics of the GUI element to draw the board onto
     */
    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        int offSetX = 0;
        int offSetY = 0;

        //Draw Orange Pieces
        int sideCellWidth=40;
        int sideCellHeight=60;
        for(int x=0; x<shapes.size();x++)
        {
            int gridRow = x%7;
            int gridCol = x/7;
            g.setColor(Color.GRAY);
            g.fillRect(offSetX+gridCol*sideCellWidth,offSetY+gridRow*sideCellHeight,sideCellWidth,sideCellHeight);
            g.setColor(Color.BLACK);
            g.fillRect(offSetX+gridCol*sideCellWidth+2,offSetY+gridRow*sideCellHeight+2,sideCellWidth-4,sideCellHeight-4);
            if(!orangeUsedShapes[x])
            {
                int centerX = offSetX+gridCol*sideCellWidth+sideCellWidth/2;
                int centerY = offSetY+gridRow*sideCellHeight+sideCellHeight/2;

                int drawX = centerX - shapes.get(x).original()[0].length*SMALL_DIM/2;
                int drawY = centerY - shapes.get(x).original().length*SMALL_DIM/2;
                for(int r=0; r<shapes.get(x).original().length;r++)
                    for(int c=0; c<shapes.get(x).original()[0].length;c++) {
                        if(shapes.get(x).original()[r][c]) {
                            g.setColor(Color.ORANGE);
                            g.fillRect(drawX + c * SMALL_DIM, drawY + r * SMALL_DIM, SMALL_DIM, SMALL_DIM);
                            g.setColor(Color.GRAY);
                            g.fillRect(drawX + c * SMALL_DIM + 3, drawY + r * SMALL_DIM + 3, SMALL_DIM - 6, SMALL_DIM - 6);
                        }
                    }
            }
        }
        //Draw Purple Pieces
        offSetX = 580;
        offSetY = 0;
        for(int x=0; x<shapes.size();x++)
        {
            int gridRow = x%7;
            int gridCol = x/7;
            g.setColor(Color.GRAY);
            g.fillRect(offSetX+gridCol*sideCellWidth,offSetY+gridRow*sideCellHeight,sideCellWidth,sideCellHeight);
            g.setColor(Color.BLACK);
            g.fillRect(offSetX+gridCol*sideCellWidth+2,offSetY+gridRow*sideCellHeight+2,sideCellWidth-4,sideCellHeight-4);
            if(!purpleUsedShapes[x])
            {
                int centerX = offSetX+gridCol*sideCellWidth+sideCellWidth/2;
                int centerY = offSetY+gridRow*sideCellHeight+sideCellHeight/2;

                int drawX = centerX - shapes.get(x).original()[0].length*SMALL_DIM/2;
                int drawY = centerY - shapes.get(x).original().length*SMALL_DIM/2;
                for(int r=0; r<shapes.get(x).original().length;r++)
                    for(int c=0; c<shapes.get(x).original()[0].length;c++) {
                        if(shapes.get(x).original()[r][c]) {
                            g.setColor(Color.GRAY);
                            g.fillRect(drawX + c * SMALL_DIM, drawY + r * SMALL_DIM, SMALL_DIM, SMALL_DIM);
                            g.setColor(purple);
                            g.fillRect(drawX + c * SMALL_DIM + 1, drawY + r * SMALL_DIM + 1, SMALL_DIM - 2, SMALL_DIM - 2);
                            g.setColor(Color.GRAY);
                            g.fillRect(drawX + c * SMALL_DIM + 3, drawY + r * SMALL_DIM + 3, SMALL_DIM - 6, SMALL_DIM - 6);
                        }
                    }
            }
        }
        //Draw Board
        offSetX=140;
        offSetY=0;
        for(int r=0; r<board.length;r++)
            for(int c=0; c<board[0].length;c++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(offSetX+c*LARGE_DIM,offSetY+r*LARGE_DIM,LARGE_DIM,LARGE_DIM);
                g.setColor(Color.GRAY);
                g.fillRect(offSetX+c*LARGE_DIM+1,offSetY+r*LARGE_DIM+1,LARGE_DIM-2,LARGE_DIM-2);

                if(board[r][c]==ORANGE)
                {
                    g.setColor(Color.ORANGE);
                    g.fillRect(offSetX+c*LARGE_DIM+2,offSetY+r*LARGE_DIM+2,LARGE_DIM-4,LARGE_DIM-4);
                    g.setColor(Color.GRAY);
                    g.fillRect(offSetX+c*LARGE_DIM+11,offSetY+r*LARGE_DIM+11,LARGE_DIM-22,LARGE_DIM-22);
                }
                else if(board[r][c]==PURPLE)
                {
                    g.setColor(purple);
                    g.fillRect(offSetX+c*LARGE_DIM+2,offSetY+r*LARGE_DIM+2,LARGE_DIM-4,LARGE_DIM-4);
                    g.setColor(Color.GRAY);
                    g.fillRect(offSetX+c*LARGE_DIM+11,offSetY+r*LARGE_DIM+11,LARGE_DIM-22,LARGE_DIM-22);
                }
            }

        // draw move positions for Orange
        if(turn==ORANGE)
            for(IntPoint ip: moveLocations(ORANGE))
            {
                g.setColor(Color.ORANGE);
                g.fillOval(offSetX+ip.getX()*LARGE_DIM+4,offSetY+ip.getY()*LARGE_DIM+4,LARGE_DIM-8,LARGE_DIM-8);
            }
        // draw move positions for Purplue
        if(turn==PURPLE)
            for(IntPoint ip: moveLocations(PURPLE))
            {
                g.setColor(purple);
                g.fillOval(offSetX+ip.getX()*LARGE_DIM+4,offSetY+ip.getY()*LARGE_DIM+4,LARGE_DIM-8,LARGE_DIM-8);
            }

        // draw results
        if(status() != PLAYING) {
            g.setColor(Color.GREEN);
            g.setFont(font);
            if(status()==ORANGE_WINS)
                g.drawString("Orange Wins!!!",150,200);
            else if(status()==PURPLE_WINS)
                g.drawString("Purple Wins!!!",150,200);
            else
                g.drawString("Tie Game!!!",150,200);

        }


    }

    // returns true if the move list is valid

    /**
     * Returns if the provide move is valid or not
     * @param move - the desired move
     * @param color - the color trying to make the move
     * @return - true if the move is valid, false otherwise
     */
    public boolean isValidMove(Move move, int color)
    {
        if(turn==color)
        {
            if(((color==ORANGE)?orangeUsedShapes:purpleUsedShapes)[move.getPieceNumber()]==true) {
                //System.out.println("*Shape already in used for color "+color);
                return false;
            }
            int leftC = move.getPoint().getX();
            int topR = move.getPoint().getY();
            ArrayList<IntPoint> coloredSpots = new ArrayList<>();
            boolean[][] shape = shapes.get(move.getPieceNumber()).manipulatedShape(move.isFlip(),move.getRotation());
            for(int r=0; r<shape.length; r++)
                for(int c=0; c<shape[0].length; c++)
                    if(shape[r][c]) {
                        coloredSpots.add(new IntPoint(leftC + c, topR + r));
                    }
            if(color==ORANGE && coloredSpots.contains(new IntPoint(4,4))&& board[4][4]==EMPTY)
                return true;
            else if(color==PURPLE && coloredSpots.contains(new IntPoint(9,9))&& board[9][9]==EMPTY)
                return true;
            else if(!sharePoint(coloredSpots,moveLocations(color))) {
                //System.out.println("*Not on a valid move location");
                return false;
            }
            else
            {
                for(IntPoint p: coloredSpots)
                    if(!isInGrid(p.getX(),p.getY()) || board[p.getY()][p.getX()]!=EMPTY || !notOrthogonalToSelf(p.getX(),p.getY(),color)) {
                        //System.out.println("*not in grid or not empty or is orthogonal");
                        return false;
                    }
                return true;
            }
        }
        else
        {
            //System.out.println("*wrong turn");
            return false;
        }
    }

    /**
     * Changes which turn it is
     */
    public void changeTurns()
    {
        if(turn==ORANGE) {

            turn = PURPLE;

        }
        else
        {

            turn = ORANGE;
        }
    }

    /**
     * Returns makes the move if it is valid and returns if the move was successful
     * @param move - the desired move
     * @param color - the color trying to make the move
     * @return - true if the move is valid/was made, false otherwise
     */
    public boolean makeMove(Move move, int color)
    {
        if(isValidMove(move, color))
        {
            int leftC = move.getPoint().getX();
            int topR = move.getPoint().getY();

            boolean[][] shape = shapes.get(move.getPieceNumber()).manipulatedShape(move.isFlip(),move.getRotation());
            for(int r=0; r<shape.length; r++)
                for(int c=0; c<shape[0].length; c++)
                    if(shape[r][c])
                        board[topR+r][leftC+c]=color;

            ((color==ORANGE)?orangeUsedShapes:purpleUsedShapes)[move.getPieceNumber()]=true;
            if(color==ORANGE)
                orangeSkipped=false;
            else if(color==PURPLE)
                purpleSkipped=false;
            changeTurns();
            return true;
        }
        else {
            if(color==ORANGE && turn==ORANGE) {
                orangeSkips();
                changeTurns();
            }else if(color==PURPLE && turn==PURPLE) {
                purpleSkips();
                changeTurns();
            }
            return false;
        }
    }

    /**
     * Makes the provided move, for the provided player
     * this method does not change turns, validate the move
     * or mark the piece as used.
     * @param move - the desired moves
     * @param color - the color trying to make the move
     */
    public void placePiece(Move move, int color)
    {

        try {
            int leftC = move.getPoint().getX();
            int topR = move.getPoint().getY();

            boolean[][] shape = shapes.get(move.getPieceNumber()).manipulatedShape(move.isFlip(), move.getRotation());
            for (int r = 0; r < shape.length; r++)
                for (int c = 0; c < shape[0].length; c++)
                    if (shape[r][c])
                        board[topR + r][leftC + c] = color;
        }
        catch(Exception e)
        {
            System.out.println("Error in place piece:");
            e.printStackTrace();
        }

    }

    /**
     * Removes the provided move
     * this method does not change turns, validate the remove
     * or unmark the piece as used.
     * @param move - the desired move
     */
    public void removePiece(Move move)
    {

        try {
            int leftC = move.getPoint().getX();
            int topR = move.getPoint().getY();

            boolean[][] shape = shapes.get(move.getPieceNumber()).manipulatedShape(move.isFlip(), move.getRotation());
            for (int r = 0; r < shape.length; r++)
                for (int c = 0; c < shape[0].length; c++)
                    if (shape[r][c])
                        board[topR + r][leftC + c] = EMPTY;
        }
        catch(Exception e)
        {
            System.out.println("Error in remove place piece:");
            e.printStackTrace();
        }

    }

    /**
     * Returns if the provided location is in the game board grid
     * @param c - column
     * @param r - row
     * @return true if the location is valid, false otherwise
     */
    public boolean isInGrid(int c, int r)
    {
        return (c >=0 && c <board[0].length && r >=0 && r <board.length);
    }

    /**
     * Returns whose turn it is
     * @return which players turn it is
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Load all the blockus shapes into the shapes arraylist. The file this method
     * loads from in shapes.txt
     */
    private static void loadShapes()
    {
        shapes = new ArrayList<>();
        try
        {
            File f = new File("shapes.txt");
            Scanner fromFile = new Scanner(f);

            while(fromFile.hasNextLine())
            {
                ArrayList<String> lines = new ArrayList<>();
                while(fromFile.hasNextLine())
                {
                    String line= fromFile.nextLine();

                    if(line.equals(""))
                        break;
                    lines.add(line);
                }

                boolean[][] data = new boolean[lines.size()][lines.get(0).length()];
                for(int r=0; r<data.length; r++)
                    for(int c=0; c<data[0].length; c++)
                        data[r][c] = lines.get(r).charAt(c)=='*';
                shapes.add(new Shape(data));
            }
        }
        catch(Exception e)
        {
            System.out.println("Error Loading Shapes:");
            e.printStackTrace();
        }
    }

    /**
     * Returns all the locations this player can move to
     * @param color - the color that wants to move
     * @return returns an empty ArrayList when it is not colors's turn
     * or they have no moves, otherwise the list will include all the points where
     * pieces might be able to be placed.
     */
    public ArrayList<IntPoint> moveLocations(int color)
    {
        ArrayList<IntPoint> movePoints = new ArrayList<>();
        if(color==ORANGE && board[4][4]==EMPTY) {
            movePoints.add(new IntPoint(4,4));
            return movePoints;
        }
        else if(color==PURPLE && board[9][9]==EMPTY)
        {
            movePoints.add(new IntPoint(9,9));
            return movePoints;
        }
        else if(color!=turn)
            return movePoints;
        else {
            for (int r = 0; r < board.length; r++)
                for (int c = 0; c < board[0].length; c++) {
                    if (board[r][c] == EMPTY) {
                        for (int cr = -1; cr <= 1; cr += 2)
                            for (int cc = -1; cc <= 1; cc += 2) {
                                if ((isInGrid(c + cc, r + cr) && board[r + cr][c + cc] == color) &&
                                        notOrthogonalToSelf(c, r, color)) {
                                    movePoints.add(new IntPoint(c, r));
                                }
                            }
                    }
                }

            return movePoints;
        }
    }

    /**
     * Returns true if the provide location is does not have any peices of the
     * provided color orthogonal to it
     * @param c - column
     * @param r - row
     * @param color - color to be chacked for
     * @return true when not pieces of the provide color are orthogonal to the provide location,
     * false otherwise
     */
    public boolean notOrthogonalToSelf(int c, int r, int color)
    {
        return (!isInGrid(c-1, r) || board[r][c-1]!=color) &&
                (!isInGrid(c+1, r) || board[r][c+1]!=color) &&
                (!isInGrid(c, r-1) || board[r-1][c]!=color) &&
                (!isInGrid(c, r+1) || board[r+1][c]!=color);
    }

    /**
     * Returns turn if the two ArrayLists share a common point
     * @param a - list 1
     * @param b - list 2
     * @return - true when a and b share a point, false otherwise
     */
    public boolean sharePoint(ArrayList<IntPoint> a, ArrayList<IntPoint> b)
    {
        for(IntPoint p:a)
            if(b.contains(p))
                return true;

        return false;
    }

    /**
     * Skips oranges turn
     */
    public void orangeSkips()
    {
        orangeSkipped=true;
        changeTurns();
    }

    /**
     * Skips purples turn
     */
    public void purpleSkips()
    {
        purpleSkipped=true;
        changeTurns();
    }
}