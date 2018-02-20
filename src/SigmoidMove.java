public class SigmoidMove extends Move {
    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point, int s) {
        super(pieceNumber, flip, rotation, point);
    }

    public int getScore(State state) {
        switch (state) {
            default:
                return Integer.MIN_VALUE;
        }
    }
}
