package chessHelper;

import java.util.ArrayList;

public class Search implements Definitions {
	private final MoveGenerator[] moveGenerators = new MoveGenerator[MAX_PLY];
	private ArrayList<Move> rootMoves = new ArrayList<Move>();
	private int initialDepth = 1;
	public Move bestMove;
	public Board board;
	private Evaluation evaluate = new Evaluation();
	
	public void run(Board b) {
		
		
		
		
	}
        
	
	public Move alphaBetaRoot(Board b, int ply) {
		for (int i = 0; i < moveGenerators.length; i++) {
			moveGenerators[i] = new MoveGenerator();
		}
		Move currentMove;
		int rootAlpha = -INFINITY;
		int rootBeta = INFINITY;
		int rootValue = -INFINITY;
		if (b.toMove == 1) {
			rootMoves = moveGenerators[0].getAllWHTMoves(b);
		} else {
			rootMoves = moveGenerators[0].getAllBLKMoves(b);
		}
		for (int i = 0; i < rootMoves.size(); i++) {
			  
                        currentMove = rootMoves.get(i);
			b.makeMove(currentMove);
			int eval = -alphaBeta(b, ply-1, -rootBeta, -rootAlpha);
			b.unMakeMove(currentMove);
			currentMove.score = eval;
			if (eval > rootValue) {
				bestMove = currentMove;
				rootValue = eval;
			}
			
		}
		for (int i = 0; i < rootMoves.size(); i++) {
			System.out.print(rootMoves.get(i).printMove() + " " + rootMoves.get(i).score + " | ");
			if (i % 4 == 0 && i != 0)
				System.out.println("");
		}
		return bestMove; 
		
		
	}
	private int alphaBeta(Board b, int ply, int alpha, int beta) {
		int eval;
		
		ArrayList<Move> moves;
		
		if (b.toMove == 1) {
			moves = moveGenerators[0].getAllWHTMoves(b);
		} else {
			moves = moveGenerators[0].getAllBLKMoves(b);
		}
		
		
		if (ply == 0  || moves.size() == 0) {
			int score = evaluate.evaluate(b, b.toMove);
			
			return score;
		}
		for (int i = 0; i < moves.size(); i++) {
			
			
			b.makeMove(moves.get(i));
			eval = -alphaBeta(b, ply-1, -beta, -alpha);
			b.unMakeMove(moves.get(i));
			if (eval >= beta) {
				return beta;
			}
			if (eval > alpha) {
				alpha = eval;
			}
			
		}
		return alpha;
		
	}
	
	public int perft(int depth, Board b) {
		MoveGenerator m = new MoveGenerator();
		int nodes = 0;
		ArrayList<Move> possMoves;
		
		if (b.toMove == 1) {
			possMoves = m.getAllWHTMoves(b);
		} else {
			possMoves = m.getAllBLKMoves(b);
		}
		if (depth == 1) {
			
			return possMoves.size();
		}
		for (int i = 0; i < possMoves.size(); i++) {
			b.makeMove(possMoves.get(i));
			nodes += perft(depth-1, b);
			b.unMakeMove(possMoves.get(i));
		}
		return nodes;
	}
	
}

