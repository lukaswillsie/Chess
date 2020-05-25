# Chess
Provides a system for simulating chess in code. In particular, the Board class is the one that does most of the heavy lifting. Simply create a Board object and initialize it from a source file with the initialize() method. Then, moves can be entered with the move() and promote() methods, and the board can be visualized with its toString(). If you want to save the state of the board, use the save() method.

The PieceTest class provides a main method that provides a command-line interface for loading games, making moves, and viewing the squares a piece can move to, among other operations.

The format of board source files is described in the class Javadoc, with example text files in the repo.

Note: I created this repository so that I could use it as a submodule in multiple other projects (my Chess Server and the app to go with that server), the idea being that if I found there was a bug in my chess logic while working on those projects, I would only have to fix it here.
