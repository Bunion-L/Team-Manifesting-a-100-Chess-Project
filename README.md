# Team-Manifesting-a-100-Chess-Project
Team Manifesting a 100's Project for Object Oriented Design and Programming
Lincoln Trujillo and Aden Miranda

Part 3 Instructions!
Naviagete to /project
Compile with javac -d . chess/utils/Color.java chess/utils/Utils.java chess/position/Position.java chess/pieces/Piece.java chess/pieces/Pawn.java chess/pieces/Rook.java chess/pieces/Knight.java chess/pieces/Bishop.java chess/pieces/Queen.java chess/pieces/King.java chess/board/Board.java chess/player/Player.java chess/game/Game.java chess/ChessBoard.java chess/Main.java

 
Inside /project, run with java chess.ChessBoard.

Structure
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





//Legacy Instructions
This project can be run by compiling and running the Main.java file from inside the folder outside of "chess".
To run the GUI version, run ChessBoard.java from inside the folder outside of "chess".
Castling, en passant, and promotion are not implemented
Game can be played between two players locally
Moves are made through standard chess notation (for example, "A2 A3")
Inputs are not case or whitespace sensative
