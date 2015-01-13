package chessHelper;

import java.util.ArrayList;

public class Evaluation implements Definitions{

		public static final int MOBILITY_WEIGHT = 80;
		public static final int MATERIAL_WEIGHT = 110;
		public static final int CAPTURE_WEIGHT = 100;
		
		
	
		
		
		
		
		
		
	

	public int evaluate (Board b, int color) {
		MoveGenerator m = new MoveGenerator();
		ArrayList<Move> bMoves = m.getAllBLKMoves(b);
		ArrayList<Move> wMoves = m.getAllWHTMoves(b);
		int oppColor = color*-1;
		int score = 0;
		if ((color < 0 && bMoves.size() == 0) || (color > 0 && wMoves.size() == 0)) {
			return MATE_VALUE;
		}
		if (b.isInCheck(color*-1))
			score += 200;
		
		//material
		int myMaterial = b.getMaterial(color);
		
		int theirMaterial = b.getMaterial(oppColor);
		
		int netMaterial = myMaterial - theirMaterial;
		
		score += netMaterial;
		
		int mobility = mobilityScore(b, color, bMoves, wMoves);
		int captureScore = captureScore(b, color, bMoves, wMoves);
		
		
		
		
		return score + mobility + captureScore;
	}
	
	
	public int mobilityScore(Board b, int color, ArrayList<Move> bMoves, ArrayList<Move> wMoves){
		
		boolean white;
		int score;
		if (color == 1) {
			white = true;
			score = wMoves.size() - bMoves.size();
			
		} else {
			score = bMoves.size() - wMoves.size();
			white = false;
		}
		
		
		return score;
	}
	
	public int captureScore(Board b, int color, ArrayList<Move> bMoves, ArrayList<Move> wMoves) {
		
		int wscore = 0;
		int bscore = 0;
		for (int i = 0; i < wMoves.size(); i++) {
			Move temp = wMoves.get(i);
			if (temp.capture !=0) {
				wscore++;
			}
		}
		for (int j = 0; j < bMoves.size(); j++) {
			Move temp2 = bMoves.get(j);
			if (temp2.capture !=0) {
				bscore++;
			}
				
		
		}
			if (color == WHITE) {
			return (wscore-bscore)*CAPTURE_WEIGHT;
			} else {
				return (bscore-wscore)*CAPTURE_WEIGHT;
			}
	}
}



