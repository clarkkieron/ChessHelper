package chessHelper;

public class Piece {
	
	private int index;
	private int piece;
	
	public Piece(int p, int i) {
		index = i;
		piece = p;
	}
	public int getIndex() {
		return index;
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int p, int i){
		index = i;
		piece = p;
	}
}
