public class SigmoidMove extends Move {
    int score;

    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point, int s) {
        super(pieceNumber, flip, rotation, point);
        score = s;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
