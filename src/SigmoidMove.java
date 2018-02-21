public class SigmoidMove extends Move {
    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point) {
        super(pieceNumber, flip, rotation, point);
    }

    public double getScore(EthanSigma.State state, int[][]zone, int color) {
        switch (state) {
            case Invade:
                //for each spot made available, score+= 196-distance to zone
                break;
        }
        return Integer.MIN_VALUE;
    }

    /*public double distanceToZone(int[][] zone, int color) {

    }*/
}
