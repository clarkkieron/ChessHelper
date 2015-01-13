package gui;
import chessHelper.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 * version date 11-30
 * v1.0
 * @author Kieron Clark
 */
public class ChessGUI implements Definitions{
    private String hintSq;
    private String hintFrom;
    private Color hintColor;
    private Color colorFrom;
    
    private JButton justHovered;
    
    private ActionListener sqAction = squareAction();//this actionlistener makes moves possible when clicking on a white piece
    
    private String moveString = "";
    
    private final ImageIcon transparentIcon = new ImageIcon( new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)); //used to reset squares with pieces on them
    private final Color mahogany =new Color(103,10,10); //chessboard red
    private final Color cream = new Color(255, 255, 178); //chessboard cream
    private boolean isAnalyze = false;
    public boolean highlighted;
    public boolean highlightedRed;
    private ArrayList<Board> boardRecord = new ArrayList<Board>();
    private ArrayList<String> boardList = new ArrayList<String>();
    private int searchDepth; //controls the difficulty of the game
    private Evaluation scorer = new Evaluation();
    public String moveToSend; //the move that is parsed through the gui to be sent to the engine
    public ArrayList<String> possibleMoves; //contains possible sqTo and previous sqTo color, div by space (0 = cream 1 = mahogany)
    public ArrayList<String> engineMove; //contains the squares that highlight the previous engine move
    public ArrayList<String> tempHighlight = new ArrayList<String>();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] squares = new JButton[8][8]; //contains all of the pieces on the board
    private Image[][] pieces = new Image[2][6]; //contains the images of the pieces, organized with the indexes below
    private JPanel board;
    private JPanel backupBoard; ///might be really bad idea
    private JFrame analysis;
    public static final int KING = 0, QUEEN = 1, ROOK = 2, KNIGHT = 3,BISHOP = 4, PAWN = 5; //each piece's index within pieces[][]
    private  DefaultListModel listModel = new DefaultListModel();  //the listmodel that contains the moves displayed in the movelist                                                                     //white pieces[0][x], black pieces[1][x]
    public static final int[] STARTING_ROW = {
        ROOK, KNIGHT, BISHOP,  QUEEN, KING, BISHOP, KNIGHT, ROOK
    }; //order of the starting row for a new game
    public Board modelBoard; //the internal board on which moves are made and search is executed
    private static final String columns = "HGFEDCBA";
    private final JLabel message = new JLabel("Welcome to ChessHelper!");
    private final JLabel suggestedMove = new JLabel("         ");
    private final JLabel yourMove = new JLabel("         ");
    
    
    private final JLabel score = new JLabel("Your Score: 0");
    private final JLabel captured = new JLabel(" ");
    final JFileChooser fc = new JFileChooser();
    
    private  int selectedIndex = 0; 
    private final JList moves = new JList(listModel);
    public final Insets buttonMarg = new Insets(0, 0, 0, 0);
    
    
    
    
    ChessGUI(Frame f) {
        highlighted = false;
        moveToSend = "";
        possibleMoves = new ArrayList<String>();
        engineMove = new ArrayList<String>();
        
        
        int messageType = JOptionPane.QUESTION_MESSAGE;
        String[] options = {"NEW", "LOAD", "ANALYZE PREVIOUS"};
        int answer = JOptionPane.showOptionDialog(f, "Would you like to start a new game, load a previous game, or analyze a game?", 
                                                 "Welcome to Chess Helper!",
                                                 0, messageType, null, options, "NEW GAME");
        
       
        System.out.println("Answer: "+ answer);      
               //an answer of negative one means the window was closed.
                
        switch (answer) {
            case 0: initializeGUI();
                    newGame();
                    
                break;
            case 1: initializeGUI();
                    loadGame();
                    
                break;
            case 2: isAnalyze = true;
                    initializeGUI();
                    moves.addMouseListener(listTrack);
                    loadGame();
                    deactivateButtons();
                    message.setText("Welcome to Analysis!");
                    suggestedMove.setText("Select a move to begin.");
                    score.setText(" ");
                    
                    
                    
                    
                   break;
            case -1: System.exit(0);
            default: System.out.println("analysis not yet implemented");
            newGame();
            }
         

        }
    
public final void setUpTools() {
        
        createImages(); //loads pieces from spritelist
        
        gui.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        gui.setMinimumSize(new Dimension(300, 300));
        JToolBar tools = new JToolBar(); 
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        
        JPanel scoreBar = new JPanel();
        scoreBar.add(score);
        
        scoreBar.setMinimumSize(new Dimension(150, 450));
        JPanel capBar = new JPanel();
        
        capBar.add(captured);
        capBar.setMinimumSize(new Dimension(150, 450));
        gui.add(scoreBar, BorderLayout.WEST);
        if (!isAnalyze) {
        gui.add(capBar, BorderLayout.SOUTH);
        }
        JScrollPane moveList = makeMoveList();
       
        gui.add(moveList, BorderLayout.LINE_END);
        
        Action importGame = new AbstractAction("Import Game") {
          @Override  
          public void actionPerformed(ActionEvent e) {
             
          }      
        };
        Action newGame = new AbstractAction("New Game") {
          @Override  
          public void actionPerformed(ActionEvent e) {
              newGame();
              String scoreString = "Score: " + scorer.evaluate(modelBoard, 1);
              score.setText(scoreString);
          }      
        };
        Action loadGame = new AbstractAction("Load Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
                String scoreString = "Score: " + scorer.evaluate(modelBoard, 1);
                score.setText(scoreString);
            }
        };
        Action saveGame = new AbstractAction("Save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
        
        Action analyze = new AbstractAction("Analysis") {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAnalyze = true;
                
                moves.addMouseListener(listTrack);
                loadGame();
                deactivateButtons();
                message.setText("Welcome to Analysis!");
                suggestedMove.setText("Select a move to begin.");
                score.setText(" ");
                
            }
        };
        
        Action hint = new AbstractAction("Get a Hint!") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                Move move = search.alphaBetaRoot(modelBoard, 2);
                String mmove = move.printMoveNoCap().substring(1);
                hintSq = mmove.substring(2);
                hintFrom = mmove.substring(0, 2);
                colorFrom = findButtonByName(hintFrom).getBackground();
                hintColor = findButtonByName(hintSq).getBackground();
                suggestedMove.setText("Suggested Move: " + mmove);
                
                
                findButtonByName(hintSq).setBackground(Color.GREEN);
                findButtonByName(hintFrom).setBackground(Color.GREEN);
                deactivateButtons();
                Timer timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                    findButtonByName(hintSq).setBackground(hintColor);
                    findButtonByName(hintFrom).setBackground(colorFrom);
                    suggestedMove.setText(" ");
                    activateButtons();
                    }
                });    
                timer.setRepeats(false);
                timer.start();
                
               
                
               
                
            }
        };
        
        
        if (!isAnalyze) {
        tools.add(newGame);
        //tools.add(new JButton("Save Game"));
        //tools.add(new JButton("Load Game"));
        tools.add(saveGame);
        
        tools.add(loadGame);
        tools.addSeparator();
        
        tools.add(hint);
        //tools.add(analyze);
        
        tools.addSeparator();
        tools.addSeparator();
        tools.add(message);
        tools.addSeparator(new Dimension(50, 0));
        tools.add(yourMove);
        tools.addSeparator(new Dimension(50, 0));
        tools.add(suggestedMove);
        
        
    } else {
    JPanel msg = new JPanel();
    JPanel yrmv = new JPanel();
    JPanel sgmv = new JPanel();
    
    msg.add(message); 
    msg.setBackground(Color.RED);
    yrmv.add(yourMove);
    yourMove.setText("        ");
    yrmv.setBackground(Color.CYAN);
    sgmv.add(suggestedMove);
    
    sgmv.setBackground(Color.GREEN);
    
    tools.addSeparator();
    tools.add(msg);
    tools.add(yrmv);
    tools.add(sgmv);
    }
}
    
    
    
   
    public final void initializeGUI() {
        System.out.println("ok");
        
        setUpTools();
        
        board = new JPanel(new GridLayout(0, 9));
        board.setBorder(new CompoundBorder(new EmptyBorder(8,8,8,8), new LineBorder(Color.BLACK)));
       
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.add(board);
        gui.add(boardConstrain);
       
        
        
        //end of actionlistener declaration
        
        
      
        
        //set sq backgrounds -- sets listeners if not analysis
        makeBoard(); 
        
        
        for (int i = 0; i < 8; i++) { //add column heading
            board.add(new JLabel("          " +columns.substring(i, i+1)), SwingConstants.CENTER);
        }
       board.add(new JLabel(""), SwingConstants.CENTER);
        
        for (int i = 0; i < 8; i++) { //row
            for (int j = 0; j < 8; j++) { //column
               switch (j) {
                    case 0:
                        board.add(new JLabel("" + (9-(i + 1)), SwingConstants.CENTER));
                    default:
                        String square = indexesToSquare(j, i);
                        squares[j][i].setToolTipText(square);
                        squares[j][i].setName(square);
                        board.add(squares[j][i]);
                }
            }
        }
        
        
    }
    public final JComponent getGui() {
        return gui;
    }

  
  
  
    private final void createImages() {
        try {
            //System.out.println(getClass().getClassLoader().getResource("img/pieces.png"));
            URL file = this.getClass().getClassLoader().getResource("img/pieces.png");
            
            BufferedImage bi = ImageIO.read(file);
             for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    pieces[i][j] = bi.getSubimage(
                            j * 64, i * 64, 64, 64);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private final void newGame() {
        resetColors();
        for (int i = 0; i < 8; i++) {//reset all sq's
            for (int j = 0; j < 8; j++) {
                squares[i][j].setIcon(transparentIcon);
            }
        }
        //get rid of highlighted squares
        undoHighlight();
        undoHighlightRed();
        listModel.clear();
        //set starting positions
        for (int i = 0; i < STARTING_ROW.length; i++) {
            squares[i][0].setIcon(new ImageIcon(pieces[0][STARTING_ROW[i]]));
            squares[i][7].setIcon(new ImageIcon(pieces[1][STARTING_ROW[i]]));
        }
        for (int i = 0; i < STARTING_ROW.length; i++) {
            squares[i][1].setIcon(new ImageIcon(pieces[0][PAWN]));
            squares[i][6].setIcon(new ImageIcon(pieces[1][PAWN]));
        }
        //re initialize board
        modelBoard = new Board();
        modelBoard.initialize(DEFAULT_FEN);
        message.setText("Welcome to ChessHelper!");
        
    }
    
    
    
    private String indexesToSquare(int column, int row) {
        String answer = "";
        switch(column) {
            case 0: answer += "A";
                break;
            case 1: answer += "B";
                break;
            case 2: answer += "C";
                break;
            case 3: answer += "D";
                break;
            case 4: answer += "E";
                break;
            case 5: answer += "F";
                break;
            case 6: answer += "G";
                break;
            case 7: answer += "H";
                break;
        }
        switch(row) {
            case 0: answer += "8";
                break;
            case 1: answer += "7";
                break;
            case 2: answer += "6";
                break;
            case 3: answer += "5";
                break;
            case 4: answer += "4";
                break;
            case 5: answer += "3";
                break;
            case 6: answer += "2";
                break;
            case 7: answer += "1";
                break;
        }
        return answer;
    }

    private JButton findButtonByName(String name) {
        name = name.toLowerCase();
        String tempName;
        JButton button = squares[0][0];
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                button = squares[i][j];
                tempName = button.getName().toLowerCase();
                if (tempName.equals(name)) {
                    return button;
                }
            }
        }
        return button;
    }
    
    private ArrayList<String> getPossButtons(Board b, int sqIndex) {//given a piece, returns a list of the squares the piece can 
                 ArrayList<Move> pieceMoves;
                                                                    //travel to
                 
        ArrayList<String> possSquares = new ArrayList<String>();
        ArrayList<Move> filteredMoves = new ArrayList<Move>();
        MoveGenerator m = new MoveGenerator();
        if (b.boardArray[sqIndex] == 6) {
            pieceMoves = m.getPawnMoves(b, sqIndex, 1);
        } else {
         pieceMoves= m.getMoves(b, sqIndex, 1);
        }
        for (Move move: pieceMoves) {
			if (move.capture == W_KING) {
				continue;
			}
			b.makeMove(move);
			if (b.isInCheck(1)) {
				b.unMakeMove(move);
				continue;
			}
			b.unMakeMove(move);
			filteredMoves.add(move);
			
		}
        
        if (filteredMoves.size() == 0) {
            System.out.println("no poss moves");
            return possSquares;
        }
        for (Move mo: filteredMoves) {
            String tempMove = mo.printMoveNoCap();
            tempMove = tempMove.substring(3);
            possSquares.add(tempMove);
        }
        return possSquares;
    }
    private ArrayList<String> getPossButtonsBLK(Board b, int sqIndex) {//given a piece, returns a list of the squares the piece can 
                 ArrayList<Move> pieceMoves;
                                                                    //travel to
                 
        ArrayList<String> possSquares = new ArrayList<String>();
        ArrayList<Move> filteredMoves = new ArrayList<Move>();
        MoveGenerator m = new MoveGenerator();
        if (b.boardArray[sqIndex] == -6) {
            pieceMoves = m.getPawnMoves(b, sqIndex, -1);
        } else {
         pieceMoves= m.getMoves(b, sqIndex, -1);
        }
        for (Move move: pieceMoves) {
			if (move.capture == B_KING) {
				continue;
			}
			b.makeMove(move);
			if (b.isInCheck(-1)) {
				b.unMakeMove(move);
				continue;
			}
			b.unMakeMove(move);
			filteredMoves.add(move);
			
		}
        
        if (filteredMoves.size() == 0) {
            System.out.println("no poss moves");
            return possSquares;
        }
        for (Move mo: filteredMoves) {
            String tempMove = mo.printMoveNoCap();
            tempMove = tempMove.substring(3);
            possSquares.add(tempMove);
        }
        return possSquares;
    }
    
    
    
    private void highlightRed(String fromName, String toName) {
        highlightedRed = true;
        String fromEntry = fromName + " ";
        String toEntry = toName + " ";
         if (findButtonByName(fromName).getBackground().equals(cream)) {
                          fromEntry += "0";
                      } else {
                          fromEntry += "1";
                      }
         if (findButtonByName(toName).getBackground().equals(cream)) {
                          toEntry += "0";
                      } else {
                          toEntry += "1";
                      }
         engineMove.add(toEntry);
         engineMove.add(fromEntry);
         findButtonByName(fromName).setBackground(Color.RED);
         findButtonByName(toName).setBackground(Color.RED);
    }
    
    private void highlightTemp(String sq) {
        
        String fromEntry = sq + " ";
        
         if (findButtonByName(sq).getBackground().equals(cream)) {
                          fromEntry += "0";
                      } else {
                          fromEntry += "1";
                      }
         
         
         tempHighlight.add(fromEntry);
         findButtonByName(sq).setBackground(Color.RED);
         
    }
    
    //must be done AFTER undoHighlight or it will leave red sq's
    private void undoHighlightRed() {//undo the red highlight of black's last move
        highlightedRed = false;
        String[] vars;
        if (engineMove.size() == 0) {
            return;
        }
        for (String s: engineMove) {
            vars = s.split(" ");
             if (vars[1].equalsIgnoreCase("0")) {
                      findButtonByName(vars[0]).setBackground(cream);
                     
                  } else {
                     findButtonByName(vars[0]).setBackground(mahogany); 
                  }
        }
        engineMove.clear();
    }
    private void undoHighlight() {//will only undo cyan highlights for poss moves
                                  // undoHighlightRed must be called after this fn
        highlighted = false;
              moveToSend = "";
              String[] vars;
              for (String s: possibleMoves) {
                  vars = s.split(" ");
                  if (vars[1].equalsIgnoreCase("0")) {
                      findButtonByName(vars[0]).setBackground(cream);
                     
                  } else if (vars[1].equalsIgnoreCase("1")){
                     findButtonByName(vars[0]).setBackground(mahogany); 
                  } else { 
                      findButtonByName(vars[0]).setBackground(Color.RED);
                  }
              }
              possibleMoves.clear();
    }
    private void blackMove() {
        undoHighlightRed();
        String moveString = "";
        OpeningBook open = new OpeningBook();
        MoveGenerator moveGen = new MoveGenerator();
        Search search = new Search();
        if (modelBoard.onBook) {
            open.makeBookMove(modelBoard);
            if (modelBoard.onBook) {
            Move justMoved = modelBoard.history[modelBoard.historyIndex];
            Move whiteMove = modelBoard.history[modelBoard.historyIndex-1];
            String wms = whiteMove.printMoveNoCap();
            String ms = justMoved.printMoveNoCap().substring(1);
            String msl = justMoved.printMoveNoCap();
            moveString ="" + (modelBoard.fullMove-1) + ": " + wms + "  |  " + msl;
            
            char pieceChar = justMoved.printMoveNoCap().charAt(0);
            String to1 = ms.substring(2);
            String from1 = ms.substring(0, 2);
           
            message.setText("Black Move: " + pieceChar + to1 + " -> " + from1);
            int fI = OpeningBook.sqToIndex(from1);
            int tI = OpeningBook.sqToIndex(to1);
            findButtonByName(from1).setIcon(transparentIcon);
            int piece = definitionToGUI((modelBoard.boardArray[tI]*-1));
            findButtonByName(to1).setIcon(new ImageIcon(pieces[0][piece]));
            
            
            
            
            listModel.addElement(moveString); //adds to move list
            highlightRed(from1, to1);
                return;
            }
        }
        ArrayList<Move> Bmoves = moveGen.getAllBLKMoves(modelBoard);
        if (Bmoves.size() ==0) {
            message.setText("Checkmate! White wins.");
            saveGame();
            return; //end game
        }
        System.out.println("Black is thinking...");
        message.setText("Black is thinking...");
        Move justMoved = modelBoard.history[modelBoard.historyIndex];
        String prevS = justMoved.printMoveNoCap();
        String m = "" + (modelBoard.fullMove) + ": ";
        //needs to work with at least 3 - takes aroun 30seconds to decide currently
        // 5-6 seconds for search depth of 2
	Move move = search.alphaBetaRoot(modelBoard, SEARCH_DEPTH);
        //boardRecord.add(modelBoard);
        modelBoard.makeMove(move);
        if (move.capture != 0) {
                      String capString = "Black captured a " + Board.convertToName(move.capture);
                      captured.setText(capString);
                  } else {
            captured.setText(" ");
        }
        System.out.println(modelBoard.getFEN());
        moveString = move.printMoveNoCap().substring(1);
        char pieceChar = move.printMoveNoCap().charAt(0);
        System.out.println("\n" + moveString);
        String to = moveString.substring(2);
        String from = moveString.substring(0, 2);
        
        System.out.println(from);
        System.out.println(to);
        int fromIndex = OpeningBook.sqToIndex(from);
        int toIndex = OpeningBook.sqToIndex(to);
        //paint move on GUI board
        
        findButtonByName(from).setIcon(transparentIcon);
        int piece = definitionToGUI((modelBoard.boardArray[toIndex]*-1));
        findButtonByName(to).setIcon(new ImageIcon(pieces[0][piece]));
        
        //check for castles
        if (piece == 0 && from.equalsIgnoreCase("E8")) {
            if (to.equalsIgnoreCase("G8")) {
                findButtonByName("H8").setIcon(transparentIcon);
                findButtonByName("F8").setIcon(new ImageIcon(pieces[0][ROOK]));
            }
            if (to.equalsIgnoreCase("B8")) {
                findButtonByName("A8").setIcon(transparentIcon);
                findButtonByName("C8").setIcon(new ImageIcon(pieces[0][ROOK]));
            }
        }
        message.setText("Black Move: " + pieceChar + to + " -> " + from);
        modelBoard.printBoard();
        //update move list
        moveString = m + prevS + "  |  " + pieceChar + moveString;
        listModel.addElement(moveString);
        highlightRed(from, to);
        ArrayList<Move> wMoves = moveGen.getAllWHTMoves(modelBoard);
        if (wMoves.size() == 0) {
            message.setText("Checkmate! Black wins.");
            saveGame();
        }
        
    }
    
    public int definitionToGUI(int piece) {
        switch(piece) {
            case 1: return 0;
            case 2: return 1;
            case 3: return 2;
            case 4: return 4;
            case 5: return 3;
            case 6: return 5;
                
        }
        return 0;
    }
    
    public void loadGame() {
        resetColors();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(SAVE_FOLDER));
        int result = fileChooser.showOpenDialog(gui);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadFromFile(selectedFile);
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
        
        
            
            
        
    }
    
    public void loadFromFile(File f) {
        undoHighlight();
        undoHighlightRed();
        listModel.clear();
        for (int i = 0; i < 8; i++) {//reset all sq's
            for (int j = 0; j < 8; j++) {
                squares[i][j].setIcon(transparentIcon);
            }
        }//save as FEN
        try {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        br.close();
        if (line.length() == 0) {
            newGame();
            return;
        }
        OpeningBook open = new OpeningBook();
        ArrayList<Move> loadedMoves = open.stringReader(line);
        modelBoard = new Board();
        modelBoard.initialize(DEFAULT_FEN);
        if (loadedMoves.size() == 0) {
            newGame();
            return;
        }
        
        
        
        for (Move s: loadedMoves) {
            
            System.out.println(modelBoard.getFEN());
            
            modelBoard.makeMove(s);
            
            
        }
        
        if (isAnalyze) {
            Board tBoard = new Board();
            tBoard.initialize(DEFAULT_FEN);
            for (int index = 0; index < modelBoard.historyIndex; index++) {
                
                if (tBoard.toMove == 1) {
                boardList.add(tBoard.getFEN());
                }
                tBoard.makeMove(modelBoard.history[index]);
                moveString += tBoard.history[index].printMoveNoCap().substring(1).toLowerCase() + " "; //record the list of moves. In analysis, the board is changed often and the movelist edited
            }
        }
        
        
        updateMoveList(loadedMoves);
        
        modelBoard.printBoard();
        updateGUIBoard();
        
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
    private void updateMoveList(ArrayList<Move> moves) { //adds the next turn on the movelist
        
        String prevMove = "";
        String defaultLine = ". ";
        int turn = 1;
        String line = turn+defaultLine;
        for (int i = 0; i < moves.size(); i++) {
            String move = moves.get(i).printMoveNoCap();
            if ((i+1) % 2 == 0){
                turn++;
                line += move;
                listModel.addElement(line);
                line = turn + defaultLine;
                continue;
            }
            
            line += move + "  |  ";
        }
   
    }
   private void updateGUIBoard() {
        ArrayList<Piece> Bpieces = MoveGenerator.getBLKPieces(modelBoard);
        ArrayList<Piece> Wpieces = MoveGenerator.getWHTPieces(modelBoard);
        for (Piece p: Bpieces) {
            String pLoc = Board.indexToSquare(p.getIndex());
            int piece = definitionToGUI((p.getPiece()*-1));
            findButtonByName(pLoc).setIcon(new ImageIcon(pieces[0][piece]));
   }
        for (Piece p: Wpieces) {
             String pLoc = Board.indexToSquare(p.getIndex());
             int piece = definitionToGUI(p.getPiece());
            findButtonByName(pLoc).setIcon(new ImageIcon(pieces[1][piece]));
        }
    }
   
   private void saveGame() { //saves game as a list of moves
       JFileChooser saveFile = new JFileChooser();
       saveFile.setCurrentDirectory(new File(SAVE_FOLDER));
       
    
       File xyz = new File("");
        if (saveFile.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
        xyz = saveFile.getSelectedFile();
        
        
         }
       ArrayList<Move> moves = new ArrayList<Move>();
       String saveString ="";
       if (modelBoard.halfMove == 0) {
           message.setText("No moves to save!");
           return;
       }
       for (int i = 0; i < modelBoard.historyIndex; i++) {
           Move move = modelBoard.history[i];
           String moveString = move.printMoveNoCap().substring(1).toLowerCase();
           saveString += moveString + " ";
           
       }
       saveString += modelBoard.history[modelBoard.historyIndex].printMoveNoCap().substring(1).toLowerCase();
       System.out.println(saveString);
       
       try {
           BufferedWriter out = new BufferedWriter(new FileWriter(xyz +".txt"));
           out.write(saveString);
           message.setText("Game saved as " +xyz + ".txt");
           out.close();
           
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   
   
   //never used, possible does not work.
   public void createListener() {
       
       
       board = new JPanel(new GridLayout(0, 9));
        board.setBorder(new CompoundBorder(new EmptyBorder(8,8,8,8), new LineBorder(Color.BLACK)));
       
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.add(board);
        gui.add(boardConstrain);
        Insets buttonMarg = new Insets(0, 0, 0, 0);
        
        
        ActionListener actionListener = new ActionListener(){
      public void actionPerformed(ActionEvent actionEvent) {
           String sqClicked = ((JComponent)actionEvent.getSource()).getName();
          int sqIndex = OpeningBook.sqToIndex(sqClicked);
          if (highlighted) {
              
              if (findButtonByName(sqClicked).getBackground().equals(Color.cyan)) {
               //if it's a highlighted button, make the move  
                  
                  String fromSq = moveToSend.trim();
                  int fromIndex = OpeningBook.sqToIndex(fromSq);
                  findButtonByName(fromSq).setIcon(transparentIcon);
                  int piece = definitionToGUI(modelBoard.boardArray[fromIndex]);
                  findButtonByName(sqClicked).setIcon(new ImageIcon(pieces[1][piece]));
                  //handle castling
                  if (piece == 0 && fromSq.equalsIgnoreCase("E1")) {
                     if (sqClicked.equalsIgnoreCase("G1")) {
                         findButtonByName("H1").setIcon(transparentIcon);
                         findButtonByName("F1").setIcon(new ImageIcon(pieces[1][ROOK]));
                 }
                     if (sqClicked.equalsIgnoreCase("B1")) {
                        findButtonByName("A1").setIcon(transparentIcon);
                        findButtonByName("C1").setIcon(new ImageIcon(pieces[1][ROOK]));
                        }
                }
                  moveToSend += sqClicked;
                  moveToSend = moveToSend.toLowerCase();
                  if (moveToSend.equalsIgnoreCase("e1g1") && piece == 0) {
                      moveToSend = "0-0";
                  }
                  if (moveToSend.equalsIgnoreCase("e1b1") && piece == 0) {
                      moveToSend = "0-0-0";
                  }
                  System.out.println(moveToSend);
                  Move move = modelBoard.playerMove(moveToSend);
                  
                  
                  modelBoard.makeMove(move);
                  
                  modelBoard.printBoard();
                  undoHighlight();
                  undoHighlightRed();
                  blackMove();
              }
              undoHighlight();
              
              return;
          }
         
          //highlight appropriate squares
          if (modelBoard.boardArray[sqIndex] > 0) {
              ArrayList<String> possMoves = getPossButtons(modelBoard, sqIndex);
              if (possMoves.size() > 0) {
                  highlighted = true;
                  moveToSend += sqClicked;
                  String temp;
                  for (String sq: possMoves) {
                      temp = "";
                      temp += sq;
                      temp += " ";
                      if (findButtonByName(sq).getBackground().equals(cream)) {
                          temp += "0";
                      } else if (findButtonByName(sq).getBackground().equals(mahogany)) {
                          temp += "1";
                      } else {//handles red highlighted squares so they can correctly be changed back
                          temp += 3;
                      }
                      possibleMoves.add(temp);
                      findButtonByName(sq).setBackground(Color.cyan);
                      //NEED: remove and replace actionListener
                  }
              }
                      
              //System.out.println(Board.convertToType(modelBoard.boardArray[sqIndex]));
          }
          //System.out.println(findButtonByName(sqClicked).getName());
      }
    };
       
   }
   
   public void updateMoveListFromBoard() { //updates the side move jlist from the board's history
       
       String prevMove = "";
       String defaultLine = ". ";
       int turn = 1;
       String line = turn+defaultLine;
       listModel.clear();
       
       for (int i = 0; i < modelBoard.historyIndex; i++) {
           
        
       
            String move = modelBoard.history[modelBoard.historyIndex].printMoveNoCap();
            if ((i+1) % 2 == 0){
                turn++;
                line += move;
                listModel.addElement(line);
                line = turn + defaultLine;
                continue;
            }
            
            line += move + "  |  ";
        }
           
       }
        public JScrollPane makeMoveList() {
            //JList moves = new JList(listModel);
            //selectedIndex = moves.getSelectedIndex(); //selected index here means the selected item in the movelist

        //make the movelist
        JScrollPane moveList = new JScrollPane(moves);
        moveList.setMinimumSize(new Dimension(300, 200));
        moveList.setPreferredSize(new Dimension(300, 200));
        //make and set a title for the move list
        JViewport view = new JViewport();
        view.setName("Move List");
        view.setVisible(true);
        JLabel label = new JLabel("Move List");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setMinimumSize(new Dimension(10, 200));
        label.setPreferredSize(new Dimension(10, 50));
         
        view.add(label);
        
        moveList.setColumnHeader(view);
        
        return moveList;
    }
        
        
        
        public static void highScoreCheck() { //checks for a highscore file and  creates one if none exists
           
            File customDir = new File(HIGHSCORES_PATH);
            if (customDir.exists()) {
                System.out.println("Already exists");
            }
            else if (customDir.mkdirs()) {
                try {
                    File f = new File(HIGHSCORES_PATH + File.separator + "scores.txt");
                    f.mkdirs();
                    f.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("File created at " + HIGHSCORES_PATH);
            } else {
                System.out.println("could not create");
            }
        }
        
        public String newpath = System.getProperty("user.home") + File.separator +"Documents" +File.separator + "ChessHelper High Scores" + File.separator + "HighScores" +File.separator;
        
        
        
        
        
        public static void main(String[] args) {
        
            Runnable r = new Runnable() {

            @Override
            public void run() {
               
                highScoreCheck();
                JFrame f = new JFrame("ChessHelper v1.0");
                 ChessGUI cg = new ChessGUI(f);
               f.setMinimumSize(new Dimension(1000, 700));
               
               
               
               f.add(cg.getGui());
                
                
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.pack();
                f.setLocationRelativeTo(null);
               // f.setMinimumSize(f.getSize());
                f.setVisible(true);
                
            }
        };
         SwingUtilities.invokeLater(r);
    }
        
        //create analysis class
    public void analysisMode() {
        backupBoard = new JPanel();
       deactivateButtons();
        analysis.setVisible(true);
        analysis.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        analysis.setBackground(Color.yellow);
        gui.add(analysis);
    }
    
    public void deactivateButtons() {//removes the actionlisteners from the buttons so that moves cannot be made.
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].removeActionListener(sqAction);
            }
        }
   
        
    }
    public void activateButtons() {
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].addActionListener(sqAction);
            }
        }
   
        
    }
    
    
    public void  renderBoard() {
            
       for (int i = 0; i < 8; i++) { //add column heading
            board.add(new JLabel("          " +columns.substring(i, i+1)), SwingConstants.CENTER);
        }
       board.add(new JLabel(""), SwingConstants.CENTER);
        
        for (int i = 0; i < 8; i++) { //row
            for (int j = 0; j < 8; j++) { //column
               switch (j) {
                    case 0:
                        board.add(new JLabel("" + (9-(i + 1)), SwingConstants.CENTER));
                    default:
                        String square = indexesToSquare(j, i);
                        squares[j][i].setToolTipText(square);
                        squares[j][i].setName(square);
                        board.add(squares[j][i]);
                }
            }
        }
        
    }
    
    public void printRecord() {
       System.out.println("precord");
        if (boardRecord.size() != 0) {
              for (int i = 0; i <boardRecord.size(); i++) {
                  System.out.println(boardRecord.get(i));
              }
              //to be done
                  
                  }
    }
    private void makeBoard() { //makes the board w/ actionlisteners
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                JButton sq = new JButton();
                sq.setMargin(buttonMarg);
                ImageIcon icon = new ImageIcon( new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                sq.setIcon(icon);
                 if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    sq.setBackground(cream);
                } else {
                    sq.setBackground(mahogany);
                }
                 
                 
                 
                     sq.addActionListener(sqAction);
                     //sq.addMouseListener(blackHighlight);
                 
                 
                 
                 
                 squares[j][i] = sq;
            }
        }
    }
    
    public void resetColors() {//resets the cream and mahogany in case of errors
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <8; j++) {
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    squares[i][j].setBackground(cream);
                } else {
                    squares[i][j].setBackground(mahogany);
                }
            }
        }
    }
    private void clearPieces() {//sets all icons transparent
        for (int i = 0; i < 8; i++) {//reset all sq's
            for (int j = 0; j < 8; j++) {
                squares[i][j].setIcon(transparentIcon);
            }
        }
        //get rid of highlighted squares
        undoHighlight();
        undoHighlightRed();
    }
    
    
    public MouseListener blackHighlight = new MouseAdapter() {//highlights the possible moves
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    String square = ((JComponent)e.getSource()).getName();
                    int sqIndex = OpeningBook.sqToIndex(square);
                    if (modelBoard.boardArray[sqIndex] < 0) { //if it's a black piece, getposs
                        justHovered = (JButton)e.getSource();
                        ArrayList<String> possMoves = getPossButtonsBLK(modelBoard, sqIndex);
                         for (String s: possMoves) {
                            highlightTemp(s);
                         }
                        
                    }
                }
                
                 @Override
                public void mouseExited(MouseEvent e) {
                   if (justHovered != (JButton)e.getSource()) {
                       return;
                   }
                    if (tempHighlight.size() == 0) {
                        return;
                    }
                    for (String s: tempHighlight) {
                        String[] squares = s.split(" ");
                        String sq = squares[0].trim();
                        String color = squares[1].trim();
                        if (color.equals("0")) {
                            findButtonByName(sq).setBackground(cream);
                        } else {
                            findButtonByName(sq).setBackground(mahogany);
                        }
                    }
                
                }
                
               
                
            };
    
    public ActionListener squareAction() {
        ActionListener go = new ActionListener(){
      public void actionPerformed(ActionEvent actionEvent) {
           
         
          
          String sqClicked = ((JComponent)actionEvent.getSource()).getName();
          int sqIndex = OpeningBook.sqToIndex(sqClicked);
          if (highlighted) {
              
              if (findButtonByName(sqClicked).getBackground().equals(Color.cyan)) {
               //if it's a highlighted button, make the move  
                  
                  String fromSq = moveToSend.trim();
                  int fromIndex = OpeningBook.sqToIndex(fromSq);
                  findButtonByName(fromSq).setIcon(transparentIcon);
                  int piece = definitionToGUI(modelBoard.boardArray[fromIndex]);
                  
                  if (findButtonByName(sqClicked).getName().equalsIgnoreCase("B1")) {
                      findButtonByName("C1").setIcon(new ImageIcon(pieces[1][piece]));
                  } else {
                  
                  findButtonByName(sqClicked).setIcon(new ImageIcon(pieces[1][piece]));
                  }
                  
                  //handle castling
                  if (piece == 0 && fromSq.equalsIgnoreCase("E1")) {
                     if (sqClicked.equalsIgnoreCase("G1")) {
                         findButtonByName("H1").setIcon(transparentIcon);
                         findButtonByName("F1").setIcon(new ImageIcon(pieces[1][ROOK]));
                 }
                     if (sqClicked.equalsIgnoreCase("B1")) {
                        findButtonByName("A1").setIcon(transparentIcon);
                        findButtonByName("D1").setIcon(new ImageIcon(pieces[1][ROOK]));
                        }
                }
                  moveToSend += sqClicked;
                  moveToSend = moveToSend.toLowerCase();
                  if (moveToSend.equalsIgnoreCase("e1g1") && piece == 0) {
                      moveToSend = "0-0";
                  }
                  if (moveToSend.equalsIgnoreCase("e1b1") && piece == 0) {
                      moveToSend = "0-0-0";
                  }
                  System.out.println(moveToSend);
                  Move move = modelBoard.playerMove(moveToSend);
                  boardRecord.add(modelBoard);
                  
                  modelBoard.makeMove(move);
                  if (move.capture != 0) {
                      String capString = "You captured " + Board.convertToName(move.capture);
                      captured.setText(capString);
                  }
                  String scoreString = "Score: " + scorer.evaluate(modelBoard, 1);
                  score.setText(scoreString);
                  modelBoard.printBoard();
                  
                  undoHighlight();
                  undoHighlightRed();
                  resetColors();
                  
                  blackMove();
              }
              undoHighlight();
              
              return;
          }
         
          //highlight appropriate squares
          if (modelBoard.boardArray[sqIndex] > 0) {
              ArrayList<String> possMoves = getPossButtons(modelBoard, sqIndex);
              if (possMoves.size() > 0) {
                  highlighted = true;
                  moveToSend += sqClicked;
                  String temp;
                  for (String sq: possMoves) {
                      temp = "";
                      temp += sq;
                      temp += " ";
                      if (findButtonByName(sq).getBackground().equals(cream)) {
                          temp += "0";
                      } else if (findButtonByName(sq).getBackground().equals(mahogany)) {
                          temp += "1";
                      } else {//handles red highlighted squares so they can correctly be changed back
                          temp += 3;
                      }
                      possibleMoves.add(temp);
                      findButtonByName(sq).setBackground(Color.cyan);
                      //NEED: remove and replace actionListener
                  }
              }
              
                      
              //System.out.println(Board.convertToType(modelBoard.boardArray[sqIndex]));
          }
          //System.out.println(findButtonByName(sqClicked).getName());
      
      }
    };
        return go;
    }
    
    
            
    
            public MouseListener listTrack = new MouseAdapter() { 
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (boardList.size() == 0) {
                        return;
                    }
                    undoHighlight();
                    undoHighlightRed();
                    resetColors();
                    String selectedMoves = moves.getSelectedValue().toString();
                    int cut = selectedMoves.indexOf('.');
                    selectedMoves = selectedMoves.substring(cut+3);
                    
                    String[] arrMoves = selectedMoves.split("  ");
                    //System.out.println(arrMoves[1]);
                    
                    
                    
                    
                    
                    
                    String move1 = arrMoves[0];
                    String move2 = arrMoves[2].substring(1).trim();
                    System.out.println(move1 + " " + move2);
                    int selection =  moves.getSelectedIndex();
                    
                    
                    String wto = move1.substring(2);
                    String wfrom = move1.substring(0, 2);
                    highlightBlue(wfrom, wto);
                    
                   System.out.println("fire; index : " + selection);
                    String bto = move2.substring(2);
                    String bfrom = move2.substring(0, 2);
                   
                    System.out.println(moveString);
                    modelBoard = new Board();
                    modelBoard.initialize(DEFAULT_FEN);
                    String[] moves2do = moveString.split(" ");
                    int count = 0;
                    while (!moves2do[count].equalsIgnoreCase(move1)) {
                        if (moves2do[count].equalsIgnoreCase("e1g1")) {
                            moves2do[count] = "0-0";
                        } else if (moves2do[count].equalsIgnoreCase("e1c1")) {
                            moves2do[count] = "0-0-0";
                        }
                        Move mm = modelBoard.playerMove(moves2do[count]);
                        modelBoard.makeMove(mm);
                        count++;
                    }
                    clearPieces();
                    updateGUIBoard();
                    highlightBlue(wfrom, wto);
                    highlightRed(bfrom, bto);
                    
                    Search search = new Search();
                    Move move = search.alphaBetaRoot(modelBoard, 2);
                    String recSq = move.printMoveNoCap().substring(1);
                    System.out.println("Suggested Move: " + recSq);
                    String toS = recSq.substring(2);
                    String fromS = recSq.substring(0, 2);
                    if (findButtonByName(toS).getBackground().equals(Color.CYAN)) {
                        findButtonByName(toS).setBackground(Color.MAGENTA);
                    } else {
                        findButtonByName(toS).setBackground(Color.GREEN);
                    }
                    
                    if (findButtonByName(fromS).getBackground().equals(Color.RED) || findButtonByName(toS).getBackground().equals(Color.CYAN)) {
                        findButtonByName(fromS).setBackground(Color.MAGENTA);
                    } else {
                        findButtonByName(fromS).setBackground(Color.GREEN);
                    }        
                   suggestedMove.setText("Suggested move: " + Board.convertToName(move.piece) + " to " + Board.indexToSquare(move.toIndex));
                 
                   suggestedMove.setForeground(Color.BLACK);
                    yourMove.setForeground(Color.BLACK);
                    captured.setForeground(Color.BLACK);
                    
                   yourMove.setText("Your move: " + Board.convertToName(modelBoard.boardArray[OpeningBook.sqToIndex(wfrom)]) + " to " + wto);
                   message.setText("Black move: " + Board.convertToName(modelBoard.boardArray[OpeningBook.sqToIndex(bfrom)]) + " to " + bto);
                   modelBoard.printBoard();
                    
                    
                    
                    
                    
                    
                   
            }
        };
          
           
    private void analysisHighlight(String list, int numMoves) {
        int color;
        String[] sepMoves = list.split(" ");
        int totalMoves = modelBoard.halfMove;
        if (totalMoves == numMoves) {
            return;
        }
        color = 1;
       
        for (int i = totalMoves; i < totalMoves+2; i++) {
           String fromSq = sepMoves[i].substring(0,2);
           String toSq = sepMoves[i].substring(2);
           System.out.println(fromSq + " " + toSq);
           if (color == -1) {
               highlightRed(fromSq, toSq);
               color = 1;
           } else {
               highlightBlue(fromSq, toSq);
               color = -1;
           }
        }
        
    }
    
    private void highlightBlue(String fromName, String toName) {
        fromName = fromName.trim();
        toName = toName.trim();
        highlighted = true;
        String fromEntry = fromName + " ";
        String toEntry = toName + " ";
        
        
        
         if (findButtonByName(fromName).getBackground().equals(cream)) {
                         fromEntry += "0";
                      } else if (findButtonByName(fromName).getBackground().equals(mahogany)) {
                          fromEntry += "1";
                      } else {//handles red highlighted squares so they can correctly be changed back
                          fromEntry += 3;
                      }
         if (findButtonByName(toName).getBackground().equals(cream)) {
                         toEntry += "0";
                      } else if (findButtonByName(toName).getBackground().equals(mahogany)) {
                          toEntry += "1";
                      } else {//handles red highlighted squares so they can correctly be changed back
                          toEntry += 3;
                      }
         possibleMoves.add(toEntry);
         possibleMoves.add(fromEntry);
         findButtonByName(fromName).setBackground(Color.CYAN);
         findButtonByName(toName).setBackground(Color.CYAN);
         
    }
            
            
}


