package chessHelper;

import java.util.ArrayList;
import java.util.Random;

public class OpeningBook implements Definitions {
	
	public static final String[] openBook = {
								"e2e4 c7c5 c2c3 d7d5 e4d5 d8d5 d2d4 e7e6", 
								"e2e4 c7c5 c2c3 g8f6 e4e5 f6d5 d2d4 c5d4 g1f3 e7e6 c3d4 b7b6 b1c3 d5c3 b2c3 d8c7",
								"e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 f1e2 e7e5 d4b3 f8e7 e1g1 e8g8 a2a4 b7b6",
								"e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 e7e6 c1e3 d7d6 g1e2 c6d4 d1d2",
								"e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 d1d2 f8e7 e1c1 e8g8",
								"e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 g2g3 e7e5 d4e2 b7b5 f1g2 c8b7 e1g1 b8d7",
								"e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 b1c3 f8g7 c1e3 g8f6 f1c4 e8g8",
								"e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2 e7e5 d4b3 f8e7 e1g1 e8g8 c1e3 c8e6",
								"e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2 g7g6 c1e3 f8g7 e1g1 e8g8 d4b3 c8e6",
								//sicilian lines
								
								"e2e4 e7e5 b1c3 g8f6 g1f3 b8c6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 b4c3 b2c3 d8e7 f1e1 c6d8 d3d4 d8e6",
								// 4 knights
								"e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 c1d2 b4d2 b1d2 d7d5",
								"e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d3 d7d6 b2b4 c5b6 a2a4 a7a5 b4b5 c6e7",
								"e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3 g8f6 c2c3",
								//Italian
								"e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2 h7h6 g5f3 e5e4 f3e5 f8d6 f2f4 e4f3 e5f3 e8g8 d2d4 c6c5",
								"e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f6e4 f1e1 d7d5 c4d5 d8d5 b1c3 d5h5 c3e4 c8e6",
								"e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d3 f8e7 e1g1 e8g8 c2c3 d7d6 c4b3",
								//2 knights								
								"e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 g8f6 d4c6 b7c6 e4e5 d8e7 d1e2 f6d5 c2c4 c8a6 g2g3 g7g6 b2b3 f8g7 c1b2 e8g8 f1g2 a8e8 e1g1",
								"e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 g2g3 e8g8 f1g2",
								//Scotch
								"e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 c2c3 e8g8 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7",
								"e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3 g8f6 e1g1 e8g8 d2d4 c5b6 f1e1 d7d6 h2h3 c6e7 b1d2",
								"e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f8c5 f3e5 c6e5 d2d4 c7c6 d4e5 f6e4 b5d3 d7d5 e5d6 e4f6 f1e1",
								"e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6b7 b1c3 e8g8 f3d4",
								"e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 e4c5 b3c2 e6g4",
								"e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 c2c3 e8g8 h2h3 c8b7 d2d4 f8e8 b1d2 e7f8 a2a3 h7h6 b3c2 c6b8 b2b4 b8d7 c1b2 g7g6",
								//Ruy Lopez
								"e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 b8c6 e1g1 c8g4 c2c4 e4f6",
								"e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 b8c6 e1g1 f8e7 c2c4 c6b4",
								"e2e4 e7e5 g1f3 g8f6 d2d4 f6e4 f1d3 d7d5 f3e5 b8d7 e5d7 c8d7 e1g1 f8d6 c2c4 c7c6 b1c3 e4c3 b2c3",
								//Petroff
								"e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 b8c6 g1f3 d8b6 a2a3 c5c4",
								"e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 f2f4 e8g8 d1d2 c7c5 g1f3 b8c6 e1c1 c5c4",
								"e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 g8e7 a2a3 b4c3 b2c3 c7c5 g1f3 b8c6 a3a4 d8a5 d1d2 c8d7",
								"e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 g8e7 a2a3 b4c3 b2c3 c7c5 a3a4 b8c6 g1f3 d8a5 c1d2 c8d7",
								"e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 d1g4 d8c7 g4g7 h8g8 g7h7 c5d4 g1e2 b8c6 f2f4 c8d7",
								"e2e4 e7e6 d2d4 d7d5 b1d2 c7c5 e4d5 e6d5 g1f3 b8c6 f1b5 f8d6 d4c5 d6c5 e1g1 g8e7",
								"e2e4 e7e6 d2d4 d7d5 b1d2 c7c5 g1f3 g8f6 e4d5 e6d5 f1b5 c8d7 b5d7 b8d7 e1g1 f8e7",
								"e2e4 e7e6 d2d4 d7d5 b1d2 g8f6 e4e5 f6d7 f1d3 c7c5 c2c3 b8c6 g1e2 c5d4 c3d4 f7f6 e5f6 d7f6 e1g1 f8d6",
								//Caro-Kann
								"d2d4 d7d6 e2e4 g8f6 b1c3 g7g6 f1c4 c7c6 d1e2 f8g7 g1f3 e8g8 c1g5 b7b5 c4d3 d8c7",
								"e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 c1g5 f8g7 d1d2 b8d7 e1c1 e7e5 d4e5 d6e5 g1f3 h7h6 g5h4 g6g5 h4g3 d8e7",
								//pirc and modern
								"c2c4 e7e6 d2d4 d7d5 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8",
								"c2c4 e7e6 b1c3 d7d5 d2d4 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6",
								"d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 c4d5 e6d5 e2e3 c7c6 f1d3 f8e7 d1c2 e8g8 g1e2 f8e8",
								"d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 f3d2 f8b4 d1c2 e8g8 g5h4 c6c5",
								"d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 f8e7 c4d5 e6d5 c1g5 e8g8",
								//QGD
								"d2d4 d7d5 c2c4 c7c6 b1c3 g8f6 g1f3 d5c4 a2a4 c8f5 f3e5 e7e6 f2f3 f8b4 c1g5 h7h6 g5f6 d8f6 e2e4 f5h7",
								"d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 e7e6 e2e3 b8d7 f1d3 f8d6",
								"d2d4 e7e6 c2c4 d7d5 g2g3 g8f6 g1f3 f8e7 f1g2 e8g8 e1g1 f6d7 d1c2 c7c6 b1d2 b7b6 e2e4 c8b7",
								//catalan
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 c7c5 d4c5 e8g8 a2a3 b4c5 g1f3 b7b6",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 e8g8 a2a3 b4c3 c2c3 b7b6 c1g5 c8b7",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 g1f3 b7b6 g2g3 c8b7 f1g2",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 e8g8 f2f3 d7d5 c4d5 e6d5 e2e3 c8f5 g1e2 b8d7 e2g3 f5g6",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 c1d2 e8g8 e2e3 d7d5 g1f3 c7c5 a2a3 b4c3 d2c3 f6e4 a1c1 e4c3 c1c3 c5d4",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 f1d3 d7d5 g1f3 c7c5 e1g1 b8c6 a2a3 b4c3 b2c3 d5c4 d3c4 d8c7",
								"d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3 b4c3 c2c3 b8c6 g1f3 f6e4 c3b3 c6a5 b3a4 c7c6",
								"d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 e1g1 e8g8 b1c3 f6e4 d1c2 e4c3 c2c3",
								"d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 e2e3 c8b7 f1d3 f8e7 b1c3 d7d5 e1g1 e8g8 d1e2 b8d7",
								"d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 d4d5 f6h5 d1d2 f7f5 e1c1 b8d7",
								"d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4d5 a7a5",
								"d2d4 g8f6 c2c4 g7g6 g2g3 f8g7 f1g2 e8g8 b1c3 d7d6 g1f3 b8d7 e1g1 e7e5 e2e4 c7c6 h2h3 d8b6",
								"d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 c7c5 g1f3 e8g8 d4d5 e7e6 f1d3 e6d5 c4d5 d8b6",
								"d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6 d4d5 c6e7 f3e1 f6e8 f2f3 f7f5",
								"d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 e8g8 c1f4 d7d6 h2h3 b8d7 e2e3 c7c6",
								"d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4 f8g7",
								"d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 c7c5 f1c4 f8g7 g1e2 e8g8 e1g1 c5d4 c3d4 b8c6",
								"d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c8g4 c1e3 f6d7 e1c1 b8c6",
								"d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f1d3 f8g7 g1e2 e8g8 e1g1 a7a6 a2a4 d8c7",
								"d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f1d3 f8g7 g1e2 e8g8 e1g1 a7a6 a2a4 d8c7",
								"c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 e2e4 f8b4 d2d3 d7d6 f1e2 e8g8 e1g1 b4c3 b2c3 d8e7",
								"c2c4 e7e5 b1c3 g8f6 g1f3 b8c6 g2g3 d7d5 c4d5 f6d5 f1g2 d5b6 e1g1 f8e7 d2d3 e8g8 c1e3 f7f5",
								"c2c4 g8f6 b1c3 d7d5 c4d5 f6d5 e2e4 d5f4 f1c4 c8e6 c4e6 f7e6",
								"c2c4 g8f6 b1c3 e7e5 g1f3 b8c6 g2g3 f8c5 f1g2 d7d6 e1g1 e8g8 d2d3 h7h6",
								"c2c4 g8f6 b1c3 e7e5 g1f3 b8c6 g2g3 f8b4 f1g2 e8g8 e1g1 e5e4 f3e1 b4c3 d2c3 h7h6 e1c2 b7b6",
								"c2c4 c7c5 g1f3 b8c6 b1c3 g8f6 g2g3 g7g6 f1g2 f8g7 e1g1 e8g8 d2d4 c5d4 f3d4 c6d4 d1d4 d7d6 d4d3",
								"g1f3 d7d5 g2g3 g8f6 f1g2 g7g6 e1g1 f8g7 d2d3 e8g8 b1d2 b8c6 e2e4 e7e5 c2c3 a7a5 f1e1 d5e4 d3e4",
								"g1f3 d7d5 c2c4 e7e6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8 b2b3 c7c5 c4d5 f6d5 c1b2 b8c6 d2d4 b7b6 b1c3 d5c3",
								"g1f3 d7d5 c2c4 d5c4 e2e3 c7c5 f1c4 e7e6 e1g1 g8f6 b2b3 b8c6 c1b2 a7a6 a2a4 f8e7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 e1g1 e8g8 c2c4 c7c6 f3e5 c8e6 c4d5 c6d5 b1c3 b8c6 e5c6 b7c6 c3a4 f6d7 b2b3 d8a5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 e1g1 e8g8 c2c4 d5c4 b1a3 c4c3 b2c3 c7c5 a3c4 b8c6 c4e5 c8f5 c1b2 f5e4 e2e3 d8c7 ",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 c2c4 d5c4 b1a3 c4c3 b2c3 e8g8 e1g1 c7c5 f3e5 b8c6 a3c4 f6d5 c1b2 c8e6 d1c2 a8c8",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 c2c4 d5c4 b1a3 c4c3 b2c3 e8g8 e1g1 c7c5 f3e5 b8c6 e5c6 b7c6 a3c4 f6d5 c1b2 c8a6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 c2c4 c7c6 c4d5 c6d5 f3e5 e8g8 b1c3 f6d7 c3d5 e7e6 e5d7 d8d7 d5c3 d7d4 e1g1 b8c6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 c2c4 e8g8 e1g1 b8c6 f3e5 d5c4 e5c6 b7c6 b1a3 c8e6 d1c2 f6d5 f1d1 d5b4 c2c3 c6c5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 g8f6 c2c4 e8g8 e1g1 b8c6 f3e5 d5c4 e5c6 b7c6 b1a3 c8e6 d1c2 f6d5 f1d1 d5b4 c2c3 c6c5 ",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 c7c6 e1g1 g8f6 b1d2 c8e6 f3e5 b8d7 e5d3 e6f5 c2c4 f5d3 e2d3 e8g8 f1e1 e7e6 d2f3 d8b6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 c7c5 e1g1 c5d4 f3d4 b8c6 d4b3 e7e6 b1c3 g8e7 e2e4 d5d4 c3e2 e8g8 f2f4 d8b6 g1h1 e6e5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 g1f3 g8f6 e1g1 e8g8 b1c3 b8a6 h2h3 a6c7 a2a4 a8b8 e2e4 a7a6 f1e1 f6d7 c1f4 b7b5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 g1f3 g8f6 e1g1 b7b5 f1e1 c8b7 e2e4 e8g8 a2a4 a7a6 g2f1 e7e6 d5e6 f6e4 e6f7 f8f7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 b1c3 g8f6 e2e4 e8g8 a2a4 e7e6 d5e6 f7e6 g1e2 b8c6 e1g1 c6b4 c3b5 d6d5 c2c3 b4c6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 c2c4 b8a6 b1c3 d8a5 d1d3 g8f6 c1d2 e8g8 g1f3 a5b6 d3b1 e7e6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 c2c3 d8b6 g1f3 g8f6 e1g1 e8g8 d4d5 d7d6 c3c4 e7e6 b1c3 e6d5 c4d5 b8d7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 g1f3 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 e8g8 e1g1 d7d6 e2e4 c8e6 a2a4 d8c8",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 g1f3 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 d7d6 e1g1 e8g8 e2e4 c8g4 f2f3 g4d7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 e1g1 e8g8 c2c4 d7d6 b1c3 b8c6 d4d5 c6e5 f3e5 d6e5 e2e4 e7e6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d5 g1f3 c7c5 e1g1 c5d4 f3d4 b8c6 d4b3 e7e6 b1c3 g8e7 e2e4 d5d4 c3e2 e8g8 f2f4 d8b6 g1h1 e6e5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 g1f3 g8f6 e1g1 e8g8 b1c3 b8a6 h2h3 a6c7 a2a4 a8b8 e2e4 a7a6 f1e1 f6d7 c1f4 b7b5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 g1f3 g8f6 e1g1 b7b5 f1e1 c8b7 e2e4 e8g8 a2a4 a7a6 g2f1 e7e6 d5e6 f6e4 e6f7 f8f7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 b1c3 g8f6 e2e4 e8g8 a2a4 e7e6 d5e6 f7e6 g1e2 b8c6 e1g1 c6b4 c3b5 d6d5 c2c3 b4c6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 d4d5 d7d6 c2c4 b8a6 b1c3 d8a5 d1d3 g8f6 c1d2 e8g8 g1f3 a5b6 d3b1 e7e6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 c2c3 d8b6 g1f3 g8f6 e1g1 e8g8 d4d5 d7d6 c3c4 e7e6 b1c3 e6d5 c4d5 b8d7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 g1f3 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 e8g8 e1g1 d7d6 e2e4 c8e6 a2a4 d8c8",
								"g2g3 g7g6 f1g2 f8g7 d2d4 c7c5 g1f3 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 d7d6 e1g1 e8g8 e2e4 c8g4 f2f3 g4d7",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 e1g1 e8g8 c2c4 d7d6 b1c3 b8c6 d4d5 c6e5 f3e5 d6e5 e2e4 e7e6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 e1g1 e8g8 c2c4 d7d6 b1c3 b8c6 b2b3 f6e4 c1b2 e4c3 b2c3 e7e5",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 e1g1 e8g8 c2c4 d7d6 b1c3 d8e8 d4d5 c7c6 d5c6 b7c6 a1b1 b8a6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 e1g1 e8g8 c2c4 d7d6 b1c3 c7c6 d4d5 e7e5 d5e6 c8e6 b2b3 b8a6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 g1f3 g8f6 c2c4 e8g8 e1g1 d7d6 b1c3 b8c6 d4d5 c6a5 d1d3 e7e5 d5e6 c8e6",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 c2c4 g8f6 b1c3 e8g8 g1h3 d7d6 d4d5 b8a6 e1g1 a6c5 h3f4 g6g5 f4d3 c5e4",
								"g2g3 g7g6 f1g2 f8g7 d2d4 f7f5 c2c4 g8f6 b1c3 e8g8 g1f3 d7d6 e1g1 b8c6 a1b1 e7e5 b2b4 f6e4 c3e4 f5e4",
								"g2g3 g7g6 f1g2 f8g7 d2d4 d7d6 e2e4 g8f6 g1e2 c7c5 e1g1 c5d4 e2d4 e8g8 b1c3 d8b6 d4e2 b8c6 h2h3 c8d7",
								"g2g3 g7g6 f1g2 f8g7 b1c3 c7c5 d2d3 b8c6 a2a3 e7e6 g1f3 d7d6 c1g5 d8d7 e1g1 f7f6 g5d2 g8h6 a1b1 e8g8 b2b4 c5b4 a3b4 h6f7",
								"g2g3 g7g6 f1g2 f8g7 b1c3 c7c5 d2d3 b8c6 a2a3 g8f6 a1b1 a7a5 a3a4 e8g8 g1f3 d7d6 e1g1 f6e8 c1e3 c6d4 e3d4 c5d4 c3b5 e7e5",
								"g2g3 g7g6 f1g2 f8g7 b1c3 c7c5 d2d3 b8c6 f2f4 e7e6 g1f3 g8e7 e1g1 e8g8 a2a3 e7f5 a1b1 a8b8 c3e4 d8b6 e4f2 d7d5 g3g4 f5d4",
								"g2g3 g7g6 f1g2 f8g7 b1c3 c7c5 d2d3 b8c6 f2f4 b7b6 g1f3 c8b7 e1g1 d7d6 e2e4 d8d7 c1e3 c6d4 a2a4 g8h6 h2h3 f7f5 g1h2 e8g8",
								"g2g3 g7g6 f1g2 f8g7 b1c3 e7e5 d2d3 b8c6 f2f4 d7d6 g1f3 g8e7 e1g1 e8g8 e2e4 h7h6 c1e3 c6d4 d1d2 g8h7 a1e1 c8e6 f3h4 e5f4",
								"g2g3 g7g6 f1g2 f8g7 b1c3 e7e5 d2d3 d7d6 f2f4 g8e7 g1f3 b8c6 e2e4 e8g8 e1g1 c8g4 f4e5 c6e5 c1g5 h7h6 g5d2 e7c6 d1c1 e5f3",
								"g2g3 g7g6 f1g2 f8g7 b1c3 d7d6 d2d3 e7e5 e2e4 g8e7 c1e3 c7c5 d1d2 h7h5 f2f4 b8c6 g1f3 c6d4 e1g1 c8g4 a1e1 d8d7 d2f2 g4f3",
								"g2g3 g7g6 f1g2 f8g7 g1f3 c7c5 e1g1 b8c6 c2c3 e7e5 d2d4 c5d4 c3d4 c6d4 f3d4 e5d4 b1a3 g8e7 c1f4 e8g8 a3b5 d7d6 f4d6 a7a6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 c7c5 e1g1 g8f6 d2d4 c5d4 f3d4 d7d5 c2c4 d5c4 b1a3 c4c3 b2c3 e8g8 d1b3 b8d7 f1d1 d7c5 b3b4 c5a6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 c7c5 d2d4 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 d7d6 e1g1 e8g8 f1e1 c8e6 e2e4 a7a5 a2a4 e6g4 f2f3 g4d7",
								"g2g3 g7g6 f1g2 f8g7 g1f3 c7c5 d2d4 c5d4 f3d4 b8c6 d4b3 g8f6 b1c3 e8g8 e2e4 d7d6 h2h3 c8e6 e1g1 a7a5",
								"g2g3 g7g6 f1g2 f8g7 g1f3 c7c5 d2d4 c5d4 f3d4 b8c6 d4b3 d7d6 e1g1 g8f6 b1c3 e8g8 f1e1 c8e6 e2e4 a7a5",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 f1e1 c7c5 e2e4 b8c6 c2c3 d7d6 d2d3 f6e8 a2a4 a7a6 b1d2 a8b8 d2f1 b7b5 a4b5 a6b5",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d6 c2c4 c7c5 b1c3 b8c6 a1b1 a8b8 a2a3 a7a6 b2b4 c5b4 a3b4 b7b5 c4b5 a6b5",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d6 c2c4 e7e5 b1c3 f8e8 c1d2 e5e4 d3e4 f6e4 c3e4 e8e4",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d6 c2c4 e7e5 b1c3 b8c6 a1b1 c6d4 f3d2 c7c6 e2e3 d4e6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d6 c2c4 e7e5 b1c3 b8c6 e2e4 c8g4 h2h3 g4e6 c1g5 h7h6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d6 c2c4 b8c6 b1c3 e7e5 a1b1 a7a5 b2b3 h7h6 a2a3 c8e6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d5 b1c3 b8d7 f3d2 c7c6 e2e4 d5e4 d3e4 d7c5 d2b3 c5a6",
								"g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3 d7d5 b1c3 b8d7 e2e4 d5e4 d3e4 e7e5 b2b3 f8e8 d1e2 c7c6",
								
								};
	
	
	
	public void makeBookMove(Board b) {
		if (b.onBook) {
			int rightLine = correctLine(openBook, b);
			if (rightLine == -1) {//there is no correct next line -- we are out of book now
				b.onBook = false;
				System.out.println("off book");
			} else {
				b.makeMove(stringReader(openBook[rightLine]).get(b.historyIndex+1));
				b.printBoard();
			}
		}
	}
	public int correctLine(String[] lines, Board b) {//given the possible lines, should run through the board's prev moves to see if any lines
		boolean isMatch;							//match. If a line matches the game up to now, continue playing and play the next move
		int randIndex;										//in line (which is the historyIndex + 1 in the current list)
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		ArrayList<Move> moveList;
		Move[] moveHistory = b.history;
		int hIndex = b.historyIndex;
		for (int i = 0; i < lines.length; i++) {//iterates through each line
			moveList = stringReader(lines[i]);
			if (checkLine(moveList, moveHistory, hIndex, 0)) {
				indexes.add(i); //returns the index of the correct list of moves to draw from from String[] lines
			}
		}
		if (indexes.size() > 1) {
			randIndex = randInt(1, indexes.size());
			return indexes.get(randIndex-1);
		} else if (indexes.size() == 1) {
			return indexes.get(0);
		}
		return -1;
	}
	
	public boolean checkLine(ArrayList<Move> line, Move[] history, int historyIndex, int currentIndex) {
		Move lineM = line.get(currentIndex);
		Move histM = history[currentIndex]; 
		
		if (currentIndex == historyIndex) {
			if (lineM.equals(histM)) {
				if (line.size() > historyIndex) {
					return true;
				}
			}
			 return false;
		}
		
		if (lineM.equals(histM)) {
			if (line.size() > historyIndex) {
			
			currentIndex++;	
			
			
			return checkLine(line, history, historyIndex, currentIndex);
			}
		}
		
		return false;
	}
	
	public ArrayList<Move> stringReader(String moves) { //given a string of moves in above notation, returns moveList of the moves
		//will only work with reg moves and castles
		ArrayList<Move> moveList = new ArrayList<Move>();
		String[] movesString = moves.split(" ");
		
		Move whiteMove;
		Move blackMove;
		Board b = new Board();
		b.initialize("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); //each line starts from the beginning of the game
		for (int i = 0; i < movesString.length; i++) {
			if (movesString[i].equals(""))
				continue;
			if (i % 2 == 0) { //even move, player turn
				int from = sqToIndex(movesString[i].substring(0, 2));
				int to = sqToIndex(movesString[i].substring(2, 4));
				if (b.boardArray[from] == W_KING && (from == E1 && to == G1)) {
					 whiteMove = new Move(W_KING, SHORT_CASTLE, G1, E1, 0, WHITE);
				} else if (from == E1 && (to == B1 || to ==C1)) {
					whiteMove = new Move(W_KING, LONG_CASTLE, B1, E1, 0, WHITE);
					
				} else {
				whiteMove = b.playerMove(movesString[i]);
				}
				moveList.add(whiteMove);
				b.makeMove(whiteMove);
				
			} else {//black turn
				String fromSq = movesString[i].substring(0, 2);
				
				String toSq = movesString[i].substring(2, 4);
				
				int from = sqToIndex(fromSq);
				int piece = b.boardArray[from];
				int type = 0;
				int to = sqToIndex(toSq);
				if (from == E8 && to == G8) {
					 blackMove = new Move(B_KING, SHORT_CASTLE, G8, E8, 0, BLACK);
				} else if (from == E8 && to == B8) {
					blackMove = new Move(B_KING, LONG_CASTLE, B8, E8, 0, BLACK);
					
				} else {
				//int piece, int type, int to, int from, int castle, int color
				int castle = b.bCastle;
				int color = BLACK;
				blackMove = new Move(piece, type, to, from, castle, color);
				}
				moveList.add(blackMove);
				b.makeMove(blackMove);
				
			}
		}
		
		return moveList;
	}
	
	public static int sqToIndex(String sq) { //sq must be 2 characters long
		assert (sq.length() == 2);
		sq = sq.toLowerCase();
		int index = 0;
		String currentChar = sq.substring(0, 1);
		switch (currentChar.charAt(0)) {
		case 'a': index = 0;
			break;
		case 'b': index = 1;
			break;
		case 'c': index = 2;
			break;
		case 'd': index = 3;
			break;
		case 'e': index = 4;
			break;
		case 'f': index = 5;
			break;
		case 'g': index = 6;
			break;
		case 'h': index = 7;
			break;
		
	
		}
	
	currentChar = sq.substring(1);
	int temp = Integer.parseInt(currentChar);
	index += ((temp-1) *16);
	return index;
	}
	
	//from stackoverflow
		public static int randInt(int min, int max) {

		    // NOTE: Usually this should be a field rather than a method
		    // variable so that it is not re-seeded every call.
		    Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = rand.nextInt((max - min) + 1) + min;

		    return randomNum;
		}
	
}
