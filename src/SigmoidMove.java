import java.util.ArrayList;

public class SigmoidMove extends Move {
    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point) {
        super(pieceNumber, flip, rotation, point);
    }

    public double getScore(EthanSigma.State state, int[][] zone, int color, BlokusBoard board) {
        int end = 0;
        switch (state) {
            case Invade:
                ArrayList<IntPoint> availableMoves = color == BlokusBoard.ORANGE ? board.moveLocations(BlokusBoard.ORANGE) : board.moveLocations(BlokusBoard.PURPLE);
                board.makeMove(this, color);
                ArrayList<IntPoint> newMoves = new ArrayList<>();
                for (IntPoint point : color == BlokusBoard.ORANGE ? board.moveLocations(BlokusBoard.ORANGE) : board.moveLocations(BlokusBoard.PURPLE))
                    if (!availableMoves.contains(point))
                        newMoves.add(point);
                for (IntPoint point : newMoves)
                    end += 196 - distanceToZone(point, zone, color);
                //for each spot made available, score+= 196-distance to zone
                board.undoMovePiece(this, color);
                break;
        }
        return end;
    }

    public double distanceToZone(IntPoint point, int[][] zone, int color) {
        double end = 1000;
        for (int r = 0; r < 14; r++)
            for (int c = 0; c < 14; c++) {
                double newEnd = 2000;
                if (zone[r][c] != color) {
                    newEnd = Math.sqrt(Math.pow(c - point.getX(), 2) + Math.pow(r - point.getY(), 2));
                    if (newEnd < end)
                        end = newEnd;
                }
            }
        return end;
    }
}
