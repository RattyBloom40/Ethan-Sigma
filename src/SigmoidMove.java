public class SigmoidMove extends Move {
    public SigmoidMove(int pieceNumber, boolean flip, int rotation, IntPoint point) {
        super(pieceNumber, flip, rotation, point);
    }

    public double getScore(EthanSigma.State state, int[][]zone, int color, BlokusBoard board) {
        switch (state) {
            case Invade:
                //for each spot made available, score+= 196-distance to zone
                break;
        }
        return Integer.MIN_VALUE;
    }

    public double distanceToZone(int[][] zone, int color, BlokusBoard board) {
        BlokusBoard copy = board;
        copy.makeMove(this,color);
        double dist=200;
        for(int r=0;r<zone.length;r++){
            for(int c=0;c<zone.length;c++){
                if(color==BlokusBoard.ORANGE?zone[r][c]==BlokusBoard.PURPLE:zone[r][c]==BlokusBoard.ORANGE){
                    if(color==board.ORANGE){
                        for(int aM=0;aM<copy.orangeMoveLocations.size();aM++){
                            dist = Math.sqrt(Math.pow(r-(double)copy.orangeMoveLocations.get(aM).getY(),2)+Math.pow(c-(double)copy.orangeMoveLocations.get(aM).getX(),2))<dist?Math.sqrt(Math.pow(r-(double)copy.orangeMoveLocations.get(aM).getY(),2)+Math.pow(c-(double)copy.orangeMoveLocations.get(aM).getX(),2)):dist;
                        }
                    }
                    else{
                        for(int aM=0;aM<copy.purpleMoveLocations.size();aM++){
                            dist = Math.sqrt(Math.pow(r-(double)copy.purpleMoveLocations.get(aM).getY(),2)+Math.pow(c-(double)copy.purpleMoveLocations.get(aM).getX(),2))<dist?Math.sqrt(Math.pow(r-(double)copy.purpleMoveLocations.get(aM).getY(),2)+Math.pow(c-(double)copy.purpleMoveLocations.get(aM).getX(),2)):dist;
                        }
                    }
                }
            }
        }
        return dist;
    }
}
