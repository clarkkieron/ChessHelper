package chessHelper;

import java.util.ArrayList;


public class Move implements Definitions {
	public int moveType;
	public int toIndex;
	public int fromIndex;
	public int castlingRights;
	public int piece;
	public int color;
	public String prevFEN; //only determines move validity
	public int capture;
	public int score;
	public int prevHMoves;
	public int prevCastleRights;
	public int prevFMoves;
	
	public Move(int piece, int type, int to, int from, int castle, int color) {
		this.moveType = type;
		this.toIndex = to;
		this.fromIndex = from;
		this.castlingRights = castle;
		this.piece = piece;
		this.color = color;
		this.prevFEN = "";
		this.capture = 0;
	}
	public void addFEN(String fen) {
		prevFEN = fen;
	}
	public String getPrev() {
		return prevFEN;
	}

	public String printMove() {
		String move = "";
		int fromRank = Board.rank(fromIndex);
		int fromFile = Board.file(fromIndex);
		move += Board.convertToType(piece);
		switch (fromFile) {
		case 0: move += "A";
				break;
		case 1: move += "B";
			break;
		case 2: move += "C";
			break;
		case 3: move += "D";
			break;
		case 4: move += "E";
			break;
		case 5: move += "F";
			break;
		case 6: move += "G";
			break;
		case 7: move += "H";
		
		}
		switch (fromRank) {
		case 0: move += "1";
				break;
		case 1: move += "2";
			break;
		case 2: move += "3";
			break;
		case 3: move += "4";
			break;
		case 4: move += "5";
			break;
		case 5: move += "6";
			break;
		case 6: move += "7";
			break;
		case 7: move += "8";
		
		}
		if (this.capture != 0)
				move+="x";
		else move += " ";
		int toFile = Board.file(toIndex);
		int toRank = Board.rank(toIndex);
		switch (toFile) {
		case 0: move += "A";
				break;
		case 1: move += "B";
			break;
		case 2: move += "C";
			break;
		case 3: move += "D";
			break;
		case 4: move += "E";
			break;
		case 5: move += "F";
			break;
		case 6: move += "G";
			break;
		case 7: move += "H";
		
		}
		switch (toRank) {
		case 0: move += "1";
				break;
		case 1: move += "2";
			break;
		case 2: move += "3";
			break;
		case 3: move += "4";
			break;
		case 4: move += "5";
			break;
		case 5: move += "6";
			break;
		case 6: move += "7";
			break;
		case 7: move += "8";
		
		}
		return move;
	}
	
	public String printMoveNoCap() {
		String move = "";
		int fromRank = Board.rank(fromIndex);
		int fromFile = Board.file(fromIndex);
		move += Board.convertToType(piece);
		switch (fromFile) {
		case 0: move += "A";
				break;
		case 1: move += "B";
			break;
		case 2: move += "C";
			break;
		case 3: move += "D";
			break;
		case 4: move += "E";
			break;
		case 5: move += "F";
			break;
		case 6: move += "G";
			break;
		case 7: move += "H";
		
		}
		switch (fromRank) {
		case 0: move += "1";
				break;
		case 1: move += "2";
			break;
		case 2: move += "3";
			break;
		case 3: move += "4";
			break;
		case 4: move += "5";
			break;
		case 5: move += "6";
			break;
		case 6: move += "7";
			break;
		case 7: move += "8";
		
		}
		
		int toFile = Board.file(toIndex);
		int toRank = Board.rank(toIndex);
		switch (toFile) {
		case 0: move += "A";
				break;
		case 1: move += "B";
			break;
		case 2: move += "C";
			break;
		case 3: move += "D";
			break;
		case 4: move += "E";
			break;
		case 5: move += "F";
			break;
		case 6: move += "G";
			break;
		case 7: move += "H";
		
		}
		switch (toRank) {
		case 0: move += "1";
				break;
		case 1: move += "2";
			break;
		case 2: move += "3";
			break;
		case 3: move += "4";
			break;
		case 4: move += "5";
			break;
		case 5: move += "6";
			break;
		case 6: move += "7";
			break;
		case 7: move += "8";
		
		}
		return move;
	}
	public static String printMoveList(ArrayList<Move> moves) {
		int i = 0;
		String moveString ="";
		for (Move m : moves) {
			if (i % 4 == 0 && i !=0) 
				moveString += "|\n";
			moveString += "| " + m.printMove() + " " + m.score;
			
			i++;
		}
		moveString += " |";
		return moveString;
	}
	
	public boolean isValid() { //invalid moves have their FEN set to "no"
		if (prevFEN.equals("no"))
			return false;
		else return true;
	}
	
	public boolean equals(Move m) {
		if ((this.fromIndex == m.fromIndex) &&
			(this.toIndex == m.toIndex)	&&
			(this.piece == m.piece) &&
			(this.prevFEN.equals(m.getPrev())) &&
			(this.moveType == m.moveType) &&
			(this.color == m.color) &&
			(this.castlingRights == m.castlingRights) ) {
			return true;
		} else return false;
		
	}
}