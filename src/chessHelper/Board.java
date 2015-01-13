package chessHelper;

import java.io.*;
import java.util.*;
/*
 * TO DO:
 * 	-rewrite isLegal and playerMove to not be so co-dependent
 * 	-implement checks at the end of makeMove to update castling rights and the en passant sq ++CASTLING WORKS
 *  -implement move_generate_capture and move_generate to generate pseudo-legal moves of each type
 *  	-make sure that Move.capture is defined in each move as it is generated
 *  	-be able to generate to a specific depth
 *  -finish JUnit tests up & make sure each scenario is accounted for
 *  -implement pieceLists as either a separate class or an in-board Class
 *  -write isAttacked and isCheck moves 
 *  -implement MVV
 *  
 *  -(LATER) update Move class and unMakeMove so that FEN strings are not necessary
 * 
 */
public class Board implements Definitions {
	
	public int[] boardArray = new int[128];
	public int toMove;
	public int enPassant;
	public int bCastle;
	public int wCastle;
	public int halfMove;
	public int fullMove;
	public Move[] history;
	public int historyIndex;
	public int wKingPos;
	public int bKingPos;
	public ArrayList<Piece> wPieces;
	public ArrayList<Piece> bPieces;
	public boolean firstTurn;
	public boolean onBook;
	
	//change this to check if the move is in the MoveList of possible white moves
	public boolean isLegal(Move m) { //checks if a proposed move is legal (only in terms of piece movement, not check). this is meant to be used for player moves
		int temp = 0;
		boolean traveled;
		boolean valid = false;
		if (this.boardArray[m.fromIndex] == 0)
			return false;
		/*if (toMove != m.color || (m.color < 0 && m.piece > 0) || (m.color > 0 && m.piece < 0)) {
			return false;
		} else {*/
			switch (m.color) {
				case WHITE:
					if (this.boardArray[m.toIndex] > 0)
						return false;
					switch (m.moveType) {
						case ORDINARY_MOVE:
							if (this.boardArray[m.toIndex] <= 0) {
                                                                    
								switch (m.piece) {
									case W_KING:
											valid = false;
											for (int i = 0; i<8; i++) {
												if ((king_delta[i] + m.fromIndex) == m.toIndex) 
													valid = true;
											}
											return valid;
										
									case W_QUEEN:
										valid = false;
										
										temp = m.fromIndex;
										
										for (int i = 0; i<8; i++) {
											temp = m.fromIndex;
											traveled = false;
											while((temp & 0x88) == 0) {
												if (this.boardArray[temp] > 0 && temp != m.fromIndex)
													traveled = true;
												temp += queen_delta[i];
												if (temp == m.toIndex && !traveled)
													return true;
												
											}
										}
											return valid;
										
									case W_ROOK:
										valid = false;
										
										for (int i = 0; i<8; i++) {
											traveled = false;
											temp = m.fromIndex;
											if (rook_delta[i] == 0)
												continue;
											while((temp & 0x88) == 0) {
												if (this.boardArray[temp] > 0 && temp != m.fromIndex)
													traveled = true;
												temp += rook_delta[i];
												if (temp == m.toIndex && !traveled)
													return true;
											}
										}
											return valid;
										
									case W_BISHOP:
										valid = false;
										temp = m.fromIndex;
										
										for (int i = 0; i<8; i++) {
											temp = m.fromIndex;
											traveled = false;
											if (bishop_delta[i] == 0)
												continue;
											while((temp & 0x88) == 0) {
												if (this.boardArray[temp] > 0 && temp != m.fromIndex)
													traveled = true;
												temp += bishop_delta[i];
												if (temp == m.toIndex && !traveled)
													return true;
											}
										}
											return valid;
										
									case W_KNIGHT:
											valid = false;
											for (int i = 0; i<8; i++) {
												if ((knight_delta[i] + m.fromIndex) == m.toIndex) 
													valid = true;
											}
											return valid;
										
									case W_PAWN:
											valid = false;
											
											for (int i = 0; i<8; i++) {
												if(i == 1 && rank(m.fromIndex) != 1) //if Wpawn is not in second rank, cannot move forward 2
													continue;
												if (i == 2 || i == 3) {
													if (this.boardArray[pawn_delta[i] + m.fromIndex] == 0) //pawn cannot move diagonally unless capturing
														continue;
												} 
												if (i ==0 || i ==1) {
													if (this.boardArray[pawn_delta[i] + m.fromIndex] != 0)
														continue;
												}
												
												if ((pawn_delta[i] + m.fromIndex) == m.toIndex) 
													valid = true;
											}
											return valid;
										
								}
							} else {
								return false;
							}
						
							break;
						case SHORT_CASTLE:
							if ((this.wCastle == 3 || this.wCastle == 1) && this.boardArray[F1] == 0 && this.boardArray[G1] == 0)
								return true;
							else return false;
							
						case LONG_CASTLE:
							if ((this.wCastle == 3 || this.wCastle ==2) && this.boardArray[B1] == 0 && this.boardArray[C1] == 0)
								return true;
							else return false;
						default: 
							System.out.println("not yet coded");
									return false;
					}
				
				break;
			//case BLACK:
			default: System.out.println("error");
				return false;
		}
		//}
		return false;
	}
	
	//add a global array of values needed to unmake move
	public void unMakeMove(Move m) { //can only be called after makeMove, or prevFEN will not be initialized.
		
		this.boardArray[m.fromIndex] = m.piece;
		
		this.boardArray[m.toIndex] = m.capture;
		
		if (m.color == 1) {
			this.wCastle = m.prevCastleRights;
		} else {
			this.bCastle = m.prevCastleRights;
		}
		if (m.color == 1 && m.moveType == 1) {//undo short castles
			this.wKingPos = E1;
			this.boardArray[F1] = EMPTY_SQUARE;
			this.boardArray[H1] =  W_ROOK;
		} else if (m.color == 1 && m.moveType == 2) {
			this.wKingPos = E1;
			this.boardArray[C1] = EMPTY_SQUARE;
                        this.boardArray[D1] = EMPTY_SQUARE;
			this.boardArray[A1] = W_ROOK;
		}
		if (m.color == -1 && m.moveType == 1) {//undo short castles
			this.bKingPos = E8;
			this.boardArray[F8] = EMPTY_SQUARE;
			this.boardArray[H8] =  B_ROOK;
		} else if (m.color == -1 && m.moveType == 2) {
			this.bKingPos = E8;
			this.boardArray[C8] = EMPTY_SQUARE;
			this.boardArray[A8] = B_ROOK;
		}
		this.halfMove = m.prevHMoves;
		this.fullMove = m.prevFMoves;
		bPieces = MoveGenerator.getBLKPieces(this);
		wPieces = MoveGenerator.getWHTPieces(this);
		for (Piece p: bPieces) {
			if (p.getPiece() == B_KING) {
				this.bKingPos = p.getIndex();
			}
		}
		for (Piece p: wPieces) {
			if (p.getPiece() == W_KING) {
				this.wKingPos = p.getIndex();
			}
		}
		this.toMove *= -1;
		if (this.halfMove == 0)
			firstTurn = true;
		
		
		if (this.historyIndex > 0)
			this.historyIndex--;
	}
	
	public void makeMove(Move m) {
		m.prevHMoves = this.halfMove;
		m.prevFMoves = this.fullMove;
		if (m.color == 1) {
			m.prevCastleRights = this.wCastle;
		} else {
			m.prevCastleRights = this.bCastle;
		}
		switch (m.color) {
			case WHITE:
				switch(m.moveType) {
					case ORDINARY_MOVE:
						if (this.boardArray[m.toIndex] < 0) { //if it's black, capture!
							m.capture = this.boardArray[m.toIndex];
							//update piecelist and remove captured piece!
						} 
							if (m.piece == W_KING) {
								this.wKingPos = m.toIndex;
							}
							toMove = -toMove;
							this.boardArray[m.toIndex] = m.piece;
							this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						
						break;
					case PROMOTION_QUEEN:
						if (this.boardArray[m.toIndex] < 0) { //if it's negative, capture!
							m.capture = this.boardArray[m.toIndex];
							//update piecelist and remove captured piece!
						} 
						 
						toMove = -toMove;
						this.boardArray[m.toIndex] = W_QUEEN;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
					
						break;
					case PROMOTION_ROOK:
						if (this.boardArray[m.toIndex] < 0) { //if it's negative, capture!
							m.capture = this.boardArray[m.toIndex];
							//update piecelist and remove captured piece!
						} 
						 
						toMove = -toMove;
						this.boardArray[m.toIndex] = W_ROOK;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case PROMOTION_BISHOP:
						if (this.boardArray[m.toIndex] < 0) { //if it's negative, capture!
							m.capture = this.boardArray[m.toIndex];
							//update piecelist and remove captured piece!
						} 
						 
						toMove = -toMove;
						this.boardArray[m.toIndex] = W_BISHOP;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case PROMOTION_KNIGHT:
						if (this.boardArray[m.toIndex] < 0) { //if it's negative, capture!
							m.capture = this.boardArray[m.toIndex];
							//update piecelist and remove captured piece!
						} 
						 
						toMove = -toMove;
						this.boardArray[m.toIndex] = W_KNIGHT;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case EN_PASSANT:
						//update piecelist to remove pawn
						this.boardArray[m.toIndex] = W_PAWN;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[(m.toIndex - 16)] = EMPTY_SQUARE;
						toMove = -toMove;
						 
						//piece captured will be below the square that the capturing pawn moves into
						break;
					case SHORT_CASTLE:
						this.boardArray[m.toIndex] = W_KING;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[H1] = EMPTY_SQUARE;
						this.boardArray[F1] = W_ROOK;
						toMove = -toMove;
						 
						wCastle = 0;
						break;
					case LONG_CASTLE:
						this.boardArray[m.toIndex] = W_KING;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[A1] = EMPTY_SQUARE;
						this.boardArray[D1] = W_ROOK;
						toMove = -toMove;
						 
						wCastle = 0;
						break;
						
					default: System.out.println("not yet coded");
				}
				//update castle rights, en passant sq.
				//if a pawn moves 2 this turn, new en passant sq = behind or above it, depend. on color
				break;
			case BLACK:
				switch(m.moveType) {
					case ORDINARY_MOVE:
						if (this.boardArray[m.toIndex] > 0) { //if it's positive, capture!
							m.capture = this.boardArray[m.toIndex];
							
							//update piecelist and remove captured piece!
						} 
						if (m.piece == B_KING) {
							this.bKingPos = m.toIndex;
						}
							toMove = -toMove;
							this.boardArray[m.toIndex] = m.piece;
							this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						
						break;
					case PROMOTION_QUEEN:
						if (this.boardArray[m.toIndex] > 0) { //if it's positive, capture!
							m.capture = this.boardArray[m.toIndex];
							
							//update piecelist and remove captured piece!
						} 
						
						toMove = -toMove;
						this.boardArray[m.toIndex] = B_QUEEN;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
					
						break;
					case PROMOTION_ROOK:
						if (this.boardArray[m.toIndex] > 0) { //if it's positive, capture!
							m.capture = this.boardArray[m.toIndex];
							
							//update piecelist and remove captured piece!
						} 
						
						toMove = -toMove;
						this.boardArray[m.toIndex] = B_ROOK;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case PROMOTION_BISHOP:
						if (this.boardArray[m.toIndex] > 0) { //if it's positive, capture!
							m.capture = this.boardArray[m.toIndex];
							
							//update piecelist and remove captured piece!
						} 
						
						toMove = -toMove;
						this.boardArray[m.toIndex] = B_BISHOP;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case PROMOTION_KNIGHT:
						if (this.boardArray[m.toIndex] > 0) { //if it's positive, capture!
							m.capture = this.boardArray[m.toIndex];
							
							//update piecelist and remove captured piece!
						} 
						
						
						toMove = -toMove;
						this.boardArray[m.toIndex] = B_KNIGHT;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						//update piecelist and remove captured piece!
						break;
					case EN_PASSANT:
						//update piecelist to remove pawn
						this.boardArray[m.toIndex] = B_PAWN;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[(m.toIndex - 16)] = EMPTY_SQUARE;
						toMove = -toMove;
						
						
						//piece captured will be below the square that the capturing pawn moves into
						break;
					case SHORT_CASTLE:
						this.boardArray[m.toIndex] = B_KING;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[H8] = EMPTY_SQUARE;
						this.boardArray[F8] = B_ROOK;
						toMove = -toMove;
						
						
						break;
					case LONG_CASTLE:
						this.boardArray[m.toIndex] = B_KING;
						this.boardArray[m.fromIndex] = EMPTY_SQUARE;
						this.boardArray[A8] = EMPTY_SQUARE;
						this.boardArray[D8] = B_ROOK;
						toMove = -toMove;
						
						
						
						break;
					default: System.out.println("not yet coded");
					
				}
				//update castle rights, en passant sq.
				//if a pawn moves 2 this turn, new en passant sq = behind or above it, depend. on color
		}
	
		this.halfMove++;
		if (m.color == BLACK && this.halfMove >= 2)
			this.fullMove++;
		if (wCastle == 3) {
			if (this.boardArray[H1] != W_ROOK)
				wCastle--;
			if (this.boardArray[A1] != W_ROOK)
				wCastle -= 2;
			
		}
		else if (wCastle == 2) {
			if (this.boardArray[A1] != W_ROOK)
				wCastle = 0;
		} else if (wCastle == 1) {
			if (this.boardArray[H1] != W_ROOK)
				wCastle = 0;
		}
		
		if (this.boardArray[E1] != W_KING)
			wCastle = 0;
		
		if (bCastle == 3) {
			if (this.boardArray[H8] != B_ROOK)
				
				bCastle--;
			
			if (this.boardArray[A8] != B_ROOK)
				bCastle -= 2;
			
		}
		else if (bCastle == 2) {
			if (this.boardArray[A8] != B_ROOK)
				
				bCastle = 0;
		} else if (bCastle == 1) {
			if (this.boardArray[H8] != B_ROOK)
				bCastle = 0;
		}
		
		if (this.boardArray[E8] != B_KING) {
			
			bCastle = 0;
		}
		if (firstTurn) {
		this.history[historyIndex] = m;
		firstTurn = false;
		} else {
		this.historyIndex++;
		this.history[historyIndex] = m;
		}
		bPieces = MoveGenerator.getBLKPieces(this);
		wPieces = MoveGenerator.getWHTPieces(this);
		for (Piece p: bPieces) {
			if (p.getPiece() == B_KING) {
				this.bKingPos = p.getIndex();
			}
		}
		for (Piece p: wPieces) {
			if (p.getPiece() == W_KING) {
				this.wKingPos = p.getIndex();
			}
		}
	}
	
	public static boolean onBoard(int index) {
		if ((index & 0x88) == 0) 
			return true;
		else return false;
	
	}
	
	public static final int rank(int index) {
		return (index - index % 16) / 	16;
	}
	public static final int file(int index) {
		return index % 16;
	}
	
	 
	
	public void initialize(String fen) {
		inputFEN(fen);
		bPieces = MoveGenerator.getBLKPieces(this);
		wPieces = MoveGenerator.getWHTPieces(this);
		for (Piece p : bPieces) {
			if (p.getPiece() == B_KING)
				wKingPos = p.getIndex();
		}	
		for (Piece piece: wPieces) {
		if (piece.getPiece() == W_KING) 
			bKingPos = piece.getIndex();
		}
			
		if (this.halfMove == 0)
			firstTurn = true;
		this.historyIndex = 0;
		history = new Move[1024];
		this.onBook = true;
	}
		
	
	
	public void printBoard() { 
		String str;
		int i = 112; //starts at A8 so that the representation is right-side up
		System.out.println("\n         +---+---+---+---+---+---+---+---+");
		System.out.print("       8 |");
		while (i >=0) {
			if ((i & 0x88) == 0) {
				str = convertToType(this.boardArray[i]);
				if (str.equals("0")) 
					str = " ";
				System.out.print(" "+ str + " |");
				i++; //iterating through the row
			} else {
				i-=24; //subtracting 24 moves to the first file of the next rank
				System.out.print("\n");
				System.out.print("         +---+---+---+---+---+---+---+---+\n");
				if((i & 0x88)==0) {
				System.out.print("       " + (rank(i)+1) + " |");
				}
			}
		
		}
		System.out.println("           a   b   c   d   e   f   g   h");
	
	
	}
	
	public static String convertToType(int type) { //given a piece's value, returns the appropriate letter
		switch (type) {
			case W_PAWN: return "P";
				
			case B_PAWN: return "p";
				
			case W_KNIGHT: return "N";
				
			case B_KNIGHT: return "n";
				
			case W_BISHOP: return "B";
				
			case B_BISHOP: return "b";
				
			case W_ROOK: return "R";
				
			case B_ROOK: return "r";
				
			case W_QUEEN: return "Q";
				
			case B_QUEEN: return "q";
				
			case W_KING: return "K";
				
			case B_KING: return "k";
				
			case EMPTY_SQUARE: return "0";
		}
		return "";
	}
        public static String convertToName(int type) { //given a piece's value, returns the appropriate letter
		switch (type) {
			case W_PAWN: return "White Pawn";
				
			case B_PAWN: return "Black Pawn";
				
			case W_KNIGHT: return "White Knight";
				
			case B_KNIGHT: return "Black Knight";
				
			case W_BISHOP: return "White Bishop";
				
			case B_BISHOP: return "Black Bishop";
				
			case W_ROOK: return "White Rook";
				
			case B_ROOK: return "Black Rook";
				
			case W_QUEEN: return "White Queen";
				
			case B_QUEEN: return "Black Queen";
				
			case W_KING: return "White King";
				
			case B_KING: return "Black King";
				
			case EMPTY_SQUARE: return "0";
		}
		return "";
	}
	public static String indexToSquare(int index) {
		String move = "";
		int fromRank = Board.rank(index);
		int fromFile = Board.file(index);
		
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
		return move;
	}
	
	public String getFEN() {//returns board's state in FEN notation
		String fen = "";
		int index = 112;
		int emptySq = 0;
		
		while (index >=0) {
			
			if ((index & 0x88) !=0) { //iterator has passed off edge of board
				
				if (emptySq != 0) {
					fen += emptySq;
					emptySq = 0;
				}
				
				index -= 24; //goes to next row
				
				if (index >= 0)
					fen += "/"; //signifies end of row
			} else { //iterator is on the board
				
				if (this.boardArray[index] != 0) { //check to see if there are 0s to add
					
					if (emptySq != 0) {
						fen += emptySq;
					}
					emptySq = 0;
				}
				if (this.boardArray[index] == 0) {
					emptySq++;
				} else {
					fen += convertToType(this.boardArray[index]);
					}
					index++;
				}
		}		
			fen += " ";
			if (this.toMove == WHITE_TO_MOVE) {
				fen += "w";
			} else fen += "b";
			
			fen += " ";
			
			if ((this.bCastle == CASTLE_NONE) && (this.wCastle == CASTLE_NONE)) {
				fen += "-"; //no castle available
			}
			else {
				switch (this.wCastle) {
					case CASTLE_KING: fen += "K"; //kingside castle available
						break;
					case CASTLE_QUEEN: fen += "Q";
						break;
					case CASTLE_BOTH: fen += "KQ";
						break;
				}
				switch (this.bCastle) {
					case CASTLE_KING: fen += "k"; 
						break;
					case CASTLE_QUEEN: fen += "q";
						break;
					case CASTLE_BOTH: fen += "kq";
						break;
				}	
			}
			fen += " ";
			if (this.enPassant == -1)
				fen += "-"; //no enPassant available
			else {
				switch(this.enPassant % 16) { //enPassant available, find the file
					case 0: fen += "a";
						break;
					case 1: fen += "b";
						break;
					case 2: fen += "a";
						break;
					case 3: fen += "b";
						break;
					case 4: fen += "a";
						break;
					case 5: fen += "b";
						break;
					case 6: fen += "a";
						break;
					case 7: fen += "b";
						break;
					default: fen += "EP Error";
				} switch ((this.enPassant - this.enPassant % 16) /16) {
					case 2: fen += "3";
						break;
					case 5: fen += "6";
						break;
					default: fen +="EP Error";
				
				}
			}
			fen = fen + " " + this.halfMove + " " + this.fullMove;
			return fen;
	}
	// rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
	
	public void inputFEN(String fen) { //given a FEN, updates board to match
		fen = fen.trim();
		String currentChar;
		
		int index = 112;
		int step = 0; // 5 steps in a FEN String: pieces, turn, castle rights, enpassant, moves
		int i = 0;
		boolean finished = false;
		while (!finished && (i < fen.length())) {
			currentChar = fen.substring(i, i+1);
			if (currentChar.equals(" ")) { //steps separated by spaces
				i++;
				currentChar = fen.substring(i, i+1);
				step++;
			}
			switch(step) {
				case STEP_PIECES:  //pieces phase
					switch(currentChar.charAt(0)) { 
						case '/': index -= 24;
							break;
						case 'K': this.boardArray[index] = 1;
						    wKingPos = index;
							index++;
							//add to piecelist later
							break;
						case 'Q': this.boardArray[index] = 2;
							index++;
							
							break;
						case 'R': this.boardArray[index] = 3;
							index++;
							
							break;
						case 'B': this.boardArray[index] = 4;
							index++;
							
							break;
						case 'N': this.boardArray[index] = 5;
							index++;
							
							break;
						case 'P': this.boardArray[index] = 6;
							index++;
							
							break;
						case 'k': this.boardArray[index] = -1;
						    bKingPos = index;
							index++;
							//add to piecelist later
							break;
						case 'q': this.boardArray[index] = -2;
							index++;
							
							break;
						case 'r': this.boardArray[index] = -3;
							index++;
							
							break;
						case 'b': this.boardArray[index] = -4;
							index++;
							
							break;
						case 'n': this.boardArray[index] = -5;
							index++;
							
							break;
						case 'p': this.boardArray[index] = -6;
							index++;
							
							break;
						default: //move index forward number of zeros
							int end = index + Integer.parseInt(currentChar);
							for (int j = index; j < end; j++) {
								this.boardArray[j] = 0;
							}
							index += Integer.parseInt(currentChar);
							
					}
					break;
				case STEP_COLOR: //color's turn
					if (currentChar.equals("w")) {
						this.toMove = 1;
					} else this.toMove = -1;
					break;
				case STEP_CASTLE: //castle rights
					switch(currentChar.charAt(0)) {
						case 'K': this.wCastle = 1;
							break;
						case 'Q': if (this.wCastle == 1) {
							this.wCastle = 3;
						} else this.wCastle = 2;
							break;
						case 'k': this.bCastle = 1;
							break;
						case 'q': if (this.bCastle == 1) {
							this.bCastle = 3;
						} else this.bCastle = 2;
							break;
					}
					break;
				case STEP_ENPASSANT: //en Passant
				
					if (currentChar.equals("-")) {
						this.enPassant = -1;
					} else {
						switch (currentChar.charAt(0)) { //get the row
							case 'a': this.enPassant = 0;
								break;
							case 'b': this.enPassant = 1;
								break;
							case 'c': this.enPassant = 2;
								break;
							case 'd': this.enPassant = 3;
								break;
							case 'e': this.enPassant = 4;
								break;
							case 'f': this.enPassant = 5;
								break;
							case 'g': this.enPassant = 6;
								break;
							case 'h': this.enPassant = 7;
								break;
						} //get index 
						i++;
						currentChar = fen.substring(i, i+1);
						if (currentChar.equals("3")) { //options are 3 or 5; 32 moves up 2; 80 up 5
							this.enPassant += 32;
						} else {
							this.enPassant += 80;
						}
						
					}
					break;
				case STEP_MOVES: //Moves
					if ((fen.substring(i +1, i+2)).equals(" ")) { //move is in single digits
						this.halfMove = Integer.parseInt(currentChar);
					} else {
						this.halfMove = Integer.parseInt(fen.substring(i, i+2));
						i++;
					}
					i += 2;
					this.fullMove = Integer.parseInt(fen.substring(i).trim());
					finished = true;
			}
			i++;
		}
		if (this.halfMove == 0) {
			this.history = new Move[1024];
			this.historyIndex = 0;
		}	
	
	}  
	
	
	public Move playerMove(String move) { //format: a2a3 || c4e5
		int piece;
		int type;
		int to = 0;
		int from = 0;
		if (move.equals("0-0")) {//castle kingside
			to = G1;
			from = E1;
			piece = W_KING;
		   	type = 1;
			Move mK = new Move((piece), SHORT_CASTLE, to, from, wCastle, type);
			Move no = new Move(piece, 0, 0, 0, 0, 0);
			System.out.println("castle");
			no.addFEN("no");
			if ((this.wCastle == 3 || this.wCastle == 1) && this.boardArray[F1] == EMPTY_SQUARE && this.boardArray[G1] == EMPTY_SQUARE)
				return mK;
			else return no;
		} else if (move.equals("0-0-0")) { //castle queenside
			to = C1;
			from = E1;
			piece = W_KING;
			type = 1;
			Move mK = new Move((piece), LONG_CASTLE, to, from, wCastle, type);
			Move no = new Move(piece, 0, 0, 0, 0, 0);
			no.addFEN("no");
			if ((this.wCastle == 3 || this.wCastle == 2) && this.boardArray[B1] == EMPTY_SQUARE && this.boardArray[C1] == EMPTY_SQUARE && this.boardArray[D1] == EMPTY_SQUARE)
				return mK;
			else return no;
			
		}
		if (move.length() > 4 || move.length() < 4 ) {
			Move nope = new Move((1), 0, to, from, wCastle, 1);
			nope.addFEN("no");
			return nope;
		}
		 
		String currentChar = move.substring(0, 1);
		
		int castle = wCastle;
		
			switch (currentChar.charAt(0)) {
				case 'a': from = 0;
					break;
				case 'b': from = 1;
					break;
				case 'c': from = 2;
					break;
				case 'd': from = 3;
					break;
				case 'e': from = 4;
					break;
				case 'f': from = 5;
					break;
				case 'g': from = 6;
					break;
				case 'h': from = 7;
					break;
				
					
				default: 
					Move nope = new Move((1), 0, to, from, wCastle, 1);
					nope.addFEN("no");
					return nope;
			
			}
			if (Character.isLetter(move.substring(1, 2).charAt(0))) {
			
				Move nope = new Move((1), 0, to, from, wCastle, 1);
				nope.addFEN("no");
				return nope;
			}
			currentChar = move.substring(1, 2);
			int temp = Integer.parseInt(currentChar);
			from += ((temp-1) *16);
			currentChar = move.substring(2, 3);
			switch (currentChar.charAt(0)) {
				case 'a': to = 0;
					break;
				case 'b': to = 1;
					break;
				case 'c': to = 2;
					break;
				case 'd': to = 3;
					break;
				case 'e': to = 4;
					break;
				case 'f': to = 5;
					break;
				case 'g': to = 6;
					break;
				case 'h': to = 7;
					break;
				default: 
					Move nope = new Move((1), 0, to, from, wCastle, 1);
					nope.addFEN("no");
					return nope;
				
			}
			currentChar = move.substring(3);
			if (Character.isLetter(currentChar.charAt(0))) {
				
				Move nope = new Move((1), 0, to, from, wCastle, 1);
				nope.addFEN("no");
				return nope;
			}
			int temp2 = Integer.parseInt(currentChar);
			to += ((temp2-1) *16);
			type = 1;
			
			piece = this.boardArray[from];
			
			int moveType = 0;
			
			Move m = new Move((piece), 0, to, from, wCastle, type);
			m.addFEN(getFEN());
			moveType = 0;
			Move no = new Move((piece), 0, to, from, wCastle, type);
			no.addFEN("no");
			boolean i = isLegal(m);
			if (i) {
				if (rank(m.toIndex) == 7 && m.piece == W_PAWN) {//handles promotion
					boolean right = false;
					while(!right) {
						Scanner kb = new Scanner(System.in);
						System.out.println("Choose the piece you wish to receive for promotion: q r b n");
						String choice = kb.next();
						switch (choice.charAt(0)) {
							case 'q': moveType = 4;
								right = true;
								break;
							case 'r': moveType = 5;
								right = true;
								break;
							case 'b': moveType = 6;
								right = true;
								break;
							case 'n': moveType = 7;
								right = true;
								break;
						default: 
							Move nope = new Move((1), 0, to, from, wCastle, 1);
							nope.addFEN("no");
							return nope;
					
						}
						
					}
				
				
				}
				m.moveType = moveType;
				return m;
			}
			return no;
			}
	
		
	
		public int getMaterial(int side) {
			int material = 0;
			MoveGenerator m = new MoveGenerator();
			if (side == -1) {
				ArrayList<Piece> pieces = MoveGenerator.getBLKPieces(this);
				for (Piece p : pieces) {
					switch (p.getPiece()) {
					case B_QUEEN:
						material += QUEEN_VALUE;
						break;
					case B_PAWN: 
						material += PAWN_VALUE;
						break;
					case B_KNIGHT: 
						material += KNIGHT_VALUE;
						break;
					case B_BISHOP: 
						material += BISHOP_VALUE;
						break;
					case B_ROOK: 
						material += ROOK_VALUE;
						break;
					case B_KING: 
						material += KING_VALUE;
						
					
					}
					
					
				}
			return material;
			} else {
				ArrayList<Piece> pieces = MoveGenerator.getWHTPieces(this);
				for (Piece p : pieces) {
					switch (p.getPiece()) {
					case W_QUEEN:
						material += QUEEN_VALUE;
						break;
					case W_PAWN: 
						material += PAWN_VALUE;
						break;
					case W_KNIGHT: 
						material += KNIGHT_VALUE;
						break;
					case W_BISHOP: 
						material += BISHOP_VALUE;
						break;
					case W_ROOK: 
						material += ROOK_VALUE;
						break;
					case W_KING: 
						material += KING_VALUE;
					}
			
				}
				return material;
			}
	}
		
		/* 
		 * 
		 * public static final int ATTACK_NONE = 0;
  public static final int ATTACK_KQR = 1;
  public static final int ATTACK_QR = 2;
  public static final int ATTACK_KQBwP = 3;
  public static final int ATTACK_KQBbP = 4;
  public static final int ATTACK_QB = 5;
  public static final int ATTACK_N = 6;
		 * 
		 * 
		 * 
		 */
	public boolean isAttackedBySide(int sqAttacked, int side) {
		ArrayList<Piece> enemyPieces;
		if (side == -1) {
			enemyPieces = MoveGenerator.getBLKPieces(this);
		} else {
			enemyPieces = MoveGenerator.getWHTPieces(this);
		}
		
		boolean attacked = false;
		for (Piece p: enemyPieces) {
			
			if (isAttacked(sqAttacked, p.getIndex())) {
				attacked = true;
			}
		}
		return attacked;
	}
	public boolean isAttacked(int sqAttacked, int attacker) {
		assert(this.boardArray[sqAttacked] != 0);
		
		int piece = this.boardArray[attacker];
		int attTest = sqAttacked - attacker + 128;
		int result = ATTACK_ARRAY[attTest];
		int attPiece = this.boardArray[sqAttacked];
		
		int attackedColor = (attPiece / Math.abs(attPiece));
		int attackingColor = (piece / Math.abs(piece));
		if (attackedColor == attackingColor) { //cant attack own color
			return false;
		}
			
		boolean canAttack = false;
		switch (result) {
			case ATTACK_NONE:
				return false;
				
			case ATTACK_KQR:
				if (Math.abs(piece) == 1 || Math.abs(piece) == 2 || Math.abs(piece) == 3)
					canAttack = true;
				break;
			case ATTACK_QR:
				if (Math.abs(piece) == 2 || Math.abs(piece) == 3)
					canAttack = true;
				break;
			case ATTACK_KQBwP:
				if (Math.abs(piece) == 1 || Math.abs(piece) == 2 || Math.abs(piece) == 4 || piece == 6)
					canAttack = true;
				break;
			case ATTACK_KQBbP:
				if (Math.abs(piece) == 1 || Math.abs(piece) == 2 || Math.abs(piece) == 4 || piece == -6)
					canAttack = true;
				break;
			case ATTACK_QB:
				if (Math.abs(piece) == 2 || Math.abs(piece) == 4)
					canAttack = true;
				break;
			case ATTACK_N:
				if (Math.abs(piece) == 5)
					canAttack = true;
		}
		
		if (canAttack) {
			int attackerPiece = this.boardArray[attacker];
			int delta = DELTA_ARRAY[attTest];
			
			int index = attacker;
			index += delta;
			while ((index & 0x88) == 0) {
				
				
				if (this.boardArray[index] == attPiece) {
					return true;
				}
				
				if (this.boardArray[index] == 0) {
				index += delta;
				} else return false;
				if (Math.abs(attackerPiece) == 5 || Math.abs(attackerPiece) == 1 || Math.abs(attackerPiece) == 6)
					break;
			}
			
			return false;
		}
		return false;
		
	}
		
	public boolean isInCheck(int side) {
		
		int king_square; //fn will fail if no king on board (need exception throw)
		ArrayList<Piece> pieces;
		if (side == 1) {
			pieces = MoveGenerator.getWHTPieces(this);
			king_square = this.wKingPos;
		} else {
			pieces = MoveGenerator.getBLKPieces(this);
			king_square = this.bKingPos;
		}
		
		
		return isAttackedBySide(king_square, side*-1);
	}
	
	public static void printMoveList(ArrayList<Move> moves) {
		for (int i = 0; i < moves.size(); i++) {
			if (i%4 ==0 && i!=0)
				System.out.println("");
			System.out.print(" | " + moves.get(i).printMove() +" | ");
			
			
		}
	}
	
	public static void printPieceList(ArrayList<Piece> pieces) {
		int i = 0;
		for (Piece p: pieces) {
			if (i%4 ==0 && i!=0)
				System.out.println("");
			System.out.print("{" + Board.convertToType(p.getPiece()) + ", " + indexToSquare(p.getIndex()) + "} ");
			
			i++;
		}
		System.out.println("\n");
	}
		
	public boolean kingOnBoard() {//returns true if both kings are on board, false if not (used in moveGen)
		ArrayList<Piece> wPieces = MoveGenerator.getBLKPieces(this);
		ArrayList<Piece> bPieces = MoveGenerator.getWHTPieces(this);
		boolean blkKing = false;
		boolean whtKing = false;
		for (Piece p: wPieces) {
			if (p.getPiece() == 1) 
				whtKing = true;
		}
		for (Piece p2: bPieces) {
			if (p2.getPiece() == -1)
				blkKing = true;
		}
		if (blkKing && whtKing)
			return true;
		else return false;
	}
		
	public void saveToFile(String name) throws FileNotFoundException, UnsupportedEncodingException {
		String game = "";
		Move currentMove;
		for (int i = 0; i < historyIndex; i++) {
			currentMove = this.history[i];
			game += currentMove.printMoveNoCap().substring(1);
			game += " ";
		}
		currentMove = this.history[historyIndex];
		game += currentMove.printMoveNoCap().substring(1).toLowerCase();
		PrintWriter writer = new PrintWriter(name+".txt", "UTF-8");
		writer.println(game);
		writer.close();
	}
	public static Board loadFromFile(String name) throws IOException {
		Board board = new Board();
		board.initialize(DEFAULT_FEN);
		BufferedReader reader = new BufferedReader(new FileReader(name + ".txt"));
		String line = null;
		String moves = "";
		while ((line = reader.readLine()) != null) {
			 moves = line;
			 
		}
		moves = moves.toLowerCase();
		
		OpeningBook open = new OpeningBook();
		ArrayList<Move> allMoves = open.stringReader(moves);
		for (Move m: allMoves) {
			
			board.makeMove(m);
		}
		return board;
	}
}