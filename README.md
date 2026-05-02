# Team-Manifesting-a-100-Chess-Project
Team Manifesting a 100's Project for Object Oriented Design and Programming
Lincoln Trujillo and Aden Miranda

//Project Overview
This chess game supports local two-player gameplay with full legal move validation, check and checkmate detection, and stalemate detection. The project is structured using object-oriented principles with clearly separated packages for pieces, board logic, game flow, and the GUI layer.

//Features
Core Game Logic

Legal move enforcement — all moves are validated against chess rules; illegal moves are rejected with a message
Check detection — players are notified when their king is in check and must escape before making any other move
Checkmate detection — the game ends automatically when a player has no legal moves and is in check
Stalemate detection — the game ends in a draw when a player has no legal moves but is not in check

//CLI Features

Text-based board rendered in the terminal with labeled ranks (1–8) and files (A–H)
Standard algebraic notation input (E2 E4, e2e4, or E2  E4 — case and whitespace insensitive)
Turn indicator showing the current player and move number
In-check warning displayed prominently on the active player's turn
Resign at any time by typing quit or resign
Play-again prompt after each game ends without restarting the program

//GUI Features

Visual 8×8 board with alternating green and beige squares rendered using piece images
Click-to-move — click a piece to select it, then click the destination square
Legal move highlighting — valid destination squares are highlighted in blue on selection; selected piece highlighted in yellow
Move history panel — a scrollable sidebar logs every move in readable notation (e.g. 1. White Pawn e2 → e4)
Captured pieces display — side panel visually tracks captured white and black pieces with scaled icons
Undo — revert the last move at any time using the Undo button
Save / Load — serialize the full board state to chess_save.dat and reload it from the Game Controls menu
New Game — reset the board at any time from the menu bar

 
Inside /project, run with java chess.ChessBoard.

//Structure
------------------------------------------------------------
Your project folder should look like this before compiling:

project/

├── chess/

│   ├── Main.java

│   ├── ChessBoard.java

│   ├── board/

│   │   └── Board.java

│   ├── game/

│   │   └── Game.java

│   ├── pieces/

│   │   ├── Piece.java

│   │   ├── Pawn.java

│   │   ├── Rook.java

│   │   ├── Knight.java

│   │   ├── Bishop.java

│   │   ├── Queen.java

│   │   └── King.java

│   ├── player/

│   │   └── Player.java

│   ├── position/

│   │   └── Position.java

│   └── utils/

│       ├── Color.java

│       └── Utils.java

└── resources/

   │
    ├── WHITE_King.png
    
   │
    ├── WHITE_Queen.png
    
   │ 
    ├── WHITE_Rook.png
    
   │
    ├── WHITE_Bishop.png
    
   │
    ├── WHITE_Knight.png
    
   │
    ├── WHITE_Pawn.png
    
   │ 
    ├── BLACK_King.png
    
   │
    ├── BLACK_Queen.png
    
   │
    ├── BLACK_Rook.png
    
   │
    ├── BLACK_Bishop.png
    
   │
    ├── BLACK_Knight.png
    
   │
   └── BLACK_Pawn.png

//Requirements

Java 11 or later
The resources/ folder must remain in the same directory as the chess/ package root (required for GUI piece images)


//Legacy Instructions

All commands below should be run from the project root directory — the folder that contains the chess/ package folder and the resources/ folder.
CLI Version
Step 1 — Compile all source files:
bashjavac chess/utils/Color.java chess/utils/Utils.java chess/position/Position.java chess/pieces/Piece.java chess/pieces/Pawn.java chess/pieces/Knight.java chess/pieces/Bishop.java chess/pieces/Rook.java chess/pieces/Queen.java chess/pieces/King.java chess/board/Board.java chess/player/Player.java chess/game/Game.java chess/Main.java
Or compile everything at once using a wildcard (works on macOS/Linux):
bashfind chess -name "*.java" | xargs javac
On Windows (Command Prompt):
batfor /r chess %f in (*.java) do javac "%f"
Step 2 — Run the CLI:
bashjava chess.Main

GUI Version
Step 1 — Compile (same as above):
bashfind chess -name "*.java" | xargs javac
Step 2 — Run the GUI:
bashjava chess.ChessBoard

Note: The resources/ folder must be present in the working directory you run the command from. If piece images are missing, the board will display empty squares.

//How to Play
CLI Controls
Moves are entered as two space-separated squares in standard algebraic notation:
<FROM> <TO>

//Known Limitations
The following standard chess rules are not implemented in this version:

Castling — king-side and queen-side castling are not supported
En passant — the special pawn capture is not available
Pawn promotion — pawns reaching the back rank are not automatically promoted
