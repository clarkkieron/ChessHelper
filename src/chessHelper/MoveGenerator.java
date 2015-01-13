package chessHelper;

import java.util.ArrayList;

public class MoveGenerator implements Definitions {
	public Move[] moves = new Move[256];

	
	
	
	public static ArrayList<Piece> getBLKPieces(Board board) { //locates all black pieces on board. 
		ArrayList<Piece> Bpieces = new ArrayList<Piece>(); 
	   
		
		int index = 112;
		
		int piece;
		while(index >= 0) {
			if (Board.onBoard(index)) {
				piece = board.boardArray[index];
				 
				if (piece*-1 > 0) {
					if (piece == B_KING) {
						board.bKingPos = index;
					}
					//System.out.println(Board.convertToType(piece));
					Piece p = new Piece(piece, index);
					
					Bpieces.add(p);
					//System.out.println(index);
						
				} 
				
				
				index++;
				
			} else {
				index -= 24;
				
			}
		}
		
		return Bpieces;
	}
	// UPDATE FOR ARRAYLISTS
	public static ArrayList<Piece> getWHTPieces(Board board) { //locates all white pieces on board. 
		ArrayList<Piece> whitePieces = new ArrayList<Piece>(); //  the second row contains the piece's index
	   
		
		int index = 112;
		int piece;
		while(index >= 0) {
			if ((index & 0x88) == 0) {
				piece = board.boardArray[index];
				if (piece > 0)  {
					if (piece == W_KING) {
						board.wKingPos = index;
					}
					Piece p = new Piece(piece, index);
					
					whitePieces.add(p);
					
				}
				
				index++;
			} else {
				index -= 24;
			}
		}
		
		return whitePieces;
	}
	
	
	public ArrayList<Move> getMoves(Board b, int pieceIndex, int color) {
		ArrayList<Move> moves =  new ArrayList<Move>();
		
		int testIndex = 0;
		int delta;
		String fen;
		
		int fromIndex = pieceIndex;
		int toIndex;
		int piece = b.boardArray[pieceIndex];
		int moveType = 0;
		
		int castlingRights;
		
		switch(b.toMove) {
		case 1: castlingRights = b.wCastle;
			break;
		case -1: castlingRights = b.bCastle;
			break;
		default: 
			System.out.println("error");
			castlingRights = 0;
		}
		
			
			if (color == WHITE && piece == W_KING &&((b.wCastle == 3 || b.wCastle == 1) && b.boardArray[F1] == EMPTY_SQUARE && b.boardArray[G1] == EMPTY_SQUARE)) {
				Move mK = new Move((piece), SHORT_CASTLE, G1, E1, castlingRights, WHITE);
				moves.add(mK);
			}
			if (color == WHITE && piece == W_KING && ((b.wCastle == 3 || b.wCastle == 2) && b.boardArray[B1] == EMPTY_SQUARE && b.boardArray[C1] == EMPTY_SQUARE && b.boardArray[D1] == EMPTY_SQUARE)) {
				
				Move mK = new Move((piece), LONG_CASTLE, B1, E1, castlingRights, WHITE);
				
				moves.add(mK);
			}
			if (color == BLACK && piece == B_KING && ((b.bCastle == 3 || b.bCastle == 1) && b.boardArray[F8] == EMPTY_SQUARE && b.boardArray[G8] == EMPTY_SQUARE)) {
				
				Move mK = new Move((piece), SHORT_CASTLE, G8, E8, castlingRights, BLACK);
				
				moves.add(mK);
			}
			if (color == BLACK && piece == B_KING && ((b.wCastle == 3 || b.wCastle == 2) && b.boardArray[B8] == EMPTY_SQUARE && b.boardArray[C8] == EMPTY_SQUARE && b.boardArray[D8] == EMPTY_SQUARE)) {
				
				Move mK = new Move((piece), LONG_CASTLE, B8, E8, castlingRights, BLACK);
				
				moves.add(mK);
			}
			
		
		int[] deltaArray = new int[1];
		switch (piece) {// get the correct delta array to check moves | PAWNS WILL BREAK THIS FUNCTION
			case B_ROOK:
			case W_ROOK:
				deltaArray = rook_delta;
				break;
			case W_BISHOP:
			case B_BISHOP:
				deltaArray = bishop_delta;
				break;
			case W_KNIGHT:
			case B_KNIGHT:
				deltaArray = knight_delta;
				break;
			case W_KING:
			case B_KING:
				deltaArray = king_delta;
				break;
			case W_QUEEN:
			case B_QUEEN:
				deltaArray = queen_delta;			
		}
		
		fen = b.getFEN();
		for (int i : deltaArray) {
			delta = i;
			testIndex = pieceIndex + delta;
			
			while ((testIndex & 0x88) == 0) {
				
				
				if (b.boardArray[testIndex] == 0) { //empty sq, valid move
					
					Move m = new Move(piece, moveType, testIndex,  pieceIndex, castlingRights, color);
					m.addFEN(b.getFEN());
					
					moves.add(m);
					
				} else if ((piece > 0 && b.boardArray[testIndex] < 0) || (piece < 0 && b.boardArray[testIndex] > 0)) { //capture available
					Move m2 = new Move(piece, moveType, testIndex,  pieceIndex, castlingRights, color);
					m2.addFEN(b.getFEN());
					m2.capture = b.boardArray[testIndex];
					moves.add(m2);
					break;
					
				} else break;
					
				if (piece == W_KNIGHT || piece == B_KNIGHT || piece == B_KING || piece == W_KING) { //keeps non sliding pieces from continuing
					break;
				}
				testIndex += delta;	
				}
			
			}
		    
			return moves;
		}	
		
	public ArrayList<Move> getPawnMoves(Board b, int pawnSq, int color) {
		ArrayList<Move> pawns = new ArrayList<Move>();
		int[] pdelta = pawn_delta;
		int testIndex = 0;
		int delta;
		String fen;
		
		int fromIndex = pawnSq;
		int toIndex;
		int piece = b.boardArray[pawnSq];
		int moveType = 0;
		int castlingRights;
		
		switch(b.toMove) {
		case 1: castlingRights = b.wCastle;
			break;
		case -1: castlingRights = b.bCastle;
			break;
		default: 
			System.out.println("error");
			castlingRights = 0;
		}	
		//caps
		for (int i = 2; i<4; i++) {
			delta = pdelta[i];
			int target = pawnSq + (delta)*color;
			if ((target & 0x88) ==0) {//cant attack offboard!
				int targetPiece = b.boardArray[target];
				if (targetPiece != 0) {
					if ((piece > 0 && targetPiece < 0) || (piece < 0 && targetPiece > 0)) { //there is a piece to attack!
						
						//pawns can attack AND get promoted in one move
						if ((piece > 0 && target > 112) || (piece < 0 && target < 7 )) {//then a promotion can be made!
							Move m = new Move(piece, PROMOTION_QUEEN, target, pawnSq, castlingRights, color);
							m.capture = targetPiece;
							m.addFEN(b.getFEN());
							pawns.add(m);
							
							Move m2 = new Move(piece, PROMOTION_ROOK, target, pawnSq, castlingRights, color);
							m2.capture = targetPiece;
							m2.addFEN(b.getFEN());
							pawns.add(m2);
							
							Move m3 = new Move(piece, PROMOTION_BISHOP, target, pawnSq, castlingRights, color);
							m3.capture = targetPiece;
							pawns.add(m3);
							m3.addFEN(b.getFEN());
							
							Move m4 = new Move(piece, PROMOTION_KNIGHT, target, pawnSq, castlingRights, color);
							m4.capture = targetPiece;
							m4.addFEN(b.getFEN());
							pawns.add(m4);
							
						} else { //it's just a capture
							Move mc = new Move(piece, ORDINARY_MOVE, target, pawnSq, castlingRights, color);
							mc.capture = targetPiece;
							pawns.add(mc);
						
						}
					}
				} //EN PASSANT HERE!
			}
		}
	
		//non-capturing moves!
	
		int target = pawnSq + pdelta[0]*color;
		if ((target & 0x88) == 0 && b.boardArray[target] == 0) {
			if ((piece > 0 && target > 112) || (piece < 0 && target < 7 )) {//then a promotion can be made!
				Move m = new Move(piece, PROMOTION_QUEEN, target, pawnSq, castlingRights, color);
				pawns.add(m);
				Move m2 = new Move(piece, PROMOTION_ROOK, target, pawnSq, castlingRights, color);
				pawns.add(m2);
				Move m3 = new Move(piece, PROMOTION_BISHOP, target, pawnSq, castlingRights, color);
				pawns.add(m3);
				Move m4 = new Move(piece, PROMOTION_KNIGHT, target, pawnSq, castlingRights, color);
				pawns.add(m4);
			} else { //it's just a capture
				
				Move mc = new Move(piece, ORDINARY_MOVE, target, pawnSq, castlingRights, color);
				pawns.add(mc);
				if ((Board.rank(pawnSq) == 1 && color ==1) ||(Board.rank(pawnSq) == 6 && color ==-1) ) {
				target += pdelta[0]*color;
				if ((target & 0x88) == 0 && b.boardArray[target] == 0){
					Move mv = new Move(piece, ORDINARY_MOVE, target, pawnSq, castlingRights, color);
					pawns.add(mv);
				}
				}
			}
		}
	
	
	return pawns;
	}
	
	public ArrayList<Move> getAllBLKMoves(Board b) {
		ArrayList<Piece> blackPieces = getBLKPieces(b);
		
		
		
		ArrayList<Move> allMoves = new ArrayList<Move>();
		
		
		
		for (int i = 0; i < blackPieces.size(); i++) {
			if (blackPieces.get(i).getPiece() == B_PAWN) {
				ArrayList<Move> temp = getPawnMoves(b, blackPieces.get(i).getIndex(), -1);
				allMoves.addAll(temp);
				temp.clear();
			} else {
				ArrayList<Move> temp2 = getMoves(b, blackPieces.get(i).getIndex(), -1);
				allMoves.addAll(temp2);
				temp2.clear();
			}
		}
		allMoves = sort(allMoves);
		ArrayList<Move> sortedMoves = new ArrayList<Move>();
		
		for (Move m: allMoves) {
			if (m.capture == W_KING) {
				continue;
			}
			b.makeMove(m);
			if (b.isInCheck(BLACK)) {
				b.unMakeMove(m);
				continue;
			}
			b.unMakeMove(m);
			sortedMoves.add(m);
			
		}
		return sortedMoves;
		
	}
	
	public ArrayList<Move> getAllWHTMoves(Board b) {
		ArrayList<Piece> whitePieces = getWHTPieces(b);
		
		
		
		ArrayList<Move> allMoves = new ArrayList<Move>();
		ArrayList<Move> sortedMoves = new ArrayList<Move>();
		
		
		for (int i = 0; i < whitePieces.size(); i++) {
			if (whitePieces.get(i).getPiece() == W_PAWN) {
				ArrayList<Move> temp = getPawnMoves(b, whitePieces.get(i).getIndex(), 1);
				allMoves.addAll(temp);
				temp.clear();
			} else {
				ArrayList<Move> temp2 = getMoves(b, whitePieces.get(i).getIndex(), 1);
				allMoves.addAll(temp2);
				temp2.clear();
			}
		}
		
		allMoves = sort(allMoves);
		
		
		
		for (Move m: allMoves) {
			if (m.capture == B_KING) {
				continue;
			}
			b.makeMove(m);
			
			if (b.isInCheck(WHITE)) {
				b.unMakeMove(m);
				continue;
			}
			b.unMakeMove(m);
			sortedMoves.add(m);
			
		}
		return sortedMoves;
		
	}
	
	
	public ArrayList<Move> getCaps(Board b, int pieceIndex, int color) {
		ArrayList<Move> moves =  new ArrayList<Move>();
		
		int testIndex = 0;
		int delta;
		String fen;
		
		int fromIndex = pieceIndex;
		int toIndex;
		int piece = b.boardArray[pieceIndex];
		int moveType = 0;
		
		int castlingRights;
		
		switch(b.toMove) {
		case 1: castlingRights = b.wCastle;
			break;
		case -1: castlingRights = b.bCastle;
			break;
		default: 
			System.out.println("error");
			castlingRights = 0;
		}
		int[] deltaArray = new int[1];
		switch (piece) {// get the correct delta array to check moves | PAWNS WILL BREAK THIS FUNCTION
			case B_ROOK:
			case W_ROOK:
				deltaArray = rook_delta;
				break;
			case W_BISHOP:
			case B_BISHOP:
				deltaArray = bishop_delta;
				break;
			case W_KNIGHT:
			case B_KNIGHT:
				deltaArray = knight_delta;
				break;
			case W_KING:
			case B_KING:
				deltaArray = king_delta;
				break;
			case W_QUEEN:
			case B_QUEEN:
				deltaArray = queen_delta;			
		}
		
		fen = b.getFEN();
		for (int i : deltaArray) {
			delta = i;
			testIndex = pieceIndex + delta;
			
			while ((testIndex & 0x88) == 0) {
				
				
				 if ((piece > 0 && b.boardArray[testIndex] < 0) || (piece < 0 && b.boardArray[testIndex] > 0)) { //capture available
					
					Move m2 = new Move(piece, moveType, testIndex,  pieceIndex, castlingRights, color);
					m2.addFEN(b.getFEN());
					m2.capture = b.boardArray[testIndex];
					moves.add(m2);
					break;
					
				} else if (piece == W_KNIGHT || piece == B_KNIGHT || piece == B_KING || piece == W_KING) { //keeps non sliding pieces from continuing
					break;
				}
				testIndex += delta;	
				}
			
			}
		    
			return moves;
		}

	
	public ArrayList<Move> sort(ArrayList<Move> moves) {
		
		
		ArrayList<Move> newm = new ArrayList<Move>();
		for (int i = 0; i < moves.size(); i++) {
			
			if (moves.get(i).capture != 0) {
				newm.add(moves.get(i));
				moves.remove(moves.indexOf(moves.get(i)));
				
			}
		}
		
		for (int i = 0; i < moves.size(); i++) {
			
			newm.add(moves.get(i));
		}
		return newm;
	}
	
	}
	/*  for (int i = 0; i < knight_delta.length; i++) {
	 * 		for (int i = 0; i <
	 * 
	 * }
	 * 
	 * 
	 */
	
