import java.util.ArrayList;

public class SigmoidMove extends Move {
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
                ArrayList<IntPoint> availableMoves = color == BlokusBoard.ORANGE ? board.getOrangeMoveLocations() : board.getPurpleMoveLocations();
                for (IntPoint point : availableMoves)
                    end += 196 - distanceToZone(point, zone, color == BlokusBoard.ORANGE ? BlokusBoard.PURPLE : BlokusBoard.ORANGE);
                //for each spot made available, score+= 196-distance to zone
                break;
        }
        end *= EthanSigma.getSize(getPieceNumber());
        try {
            end /= (color == BlokusBoard.PURPLE ? board.getOrangeMoveLocations() : board.getPurpleMoveLocations()).size();
        } catch (Exception e) {

        }

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
