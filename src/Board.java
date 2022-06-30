import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//TODO implement scores
public class Board {
    private int size;
    private Chip[][] cells;
    private Chip currentColor;
    private Set<Move> possibleMoves = new TreeSet<>();

    public Board(int size) {
        if (size != 4 && size != 6 && size!= 8 && size != 10) throw new IllegalArgumentException();
        this.size = size;
        this.cells = new Chip[size][size];
        this.currentColor = Chip.WHITE;
        //Load cell with EMPTY
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.cells[i][j] = Chip.EMPTY;
            }
        }
        //Load first chips and possibleMoves
        int x = size/2;
        for (int i = x - 2; i < x + 2; i++) {
            for (int j = x - 2; j < x + 2; j++) {
                if (((i == x) && (j == x)) || ((i == (x - 1)) && (j == (x - 1)))) {
                    this.cells[i][j] = Chip.WHITE;
                } else if (((i == (x - 1)) && (j == x)) || ((i == x) && (j == (x - 1)))) {
                    this.cells[i][j] = Chip.BLACK;
                } else {
                    this.possibleMoves.add(new Move(i, j));
                }
            }
        }
    }

    //It creates a carbon copy of the board passed by arguments
    private Board(Board board) {
        this.size = board.size;
        //Copy of cells, todo see if i can se System.arraycopy()
        this.cells = new Chip[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.cells[i][j] = board.cells[i][j];
            }
        }
        this.currentColor = board.currentColor;
        this.possibleMoves.addAll(board.possibleMoves);
    }

    //Changes the color to the one of the next turn
    public void changeColor() {
        if (this.currentColor == Chip.WHITE) {
            this.currentColor = Chip.BLACK;
        } else {
            this.currentColor = Chip.WHITE;
        }
    }

    //Updates the perimeter, removing the one sent by parameter and adding those surrounding it
    private void updatePossible(int x, int y) {
        //We remove the position of were we just played
        this.possibleMoves.remove(new Move(x, y));
        //We check around for empty spaces and we add those to our perimeter
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if(inBounds(i,j) && this.cells[i][j] == Chip.EMPTY) {
                    this.possibleMoves.add(new Move(i,j));
                }
            }
        }
    }

    //Gives a score to the board based on how good it is to the currentColor
    public int score() {
        //todo implement score
        return 0;
    }

    //Returns a list of neighbor boards
    public List<NeighBoard> getNeigh() {
        List<NeighBoard> validBoards = new LinkedList<>();
        for(Move m : possibleMoves) {
            Board auxBoard = this.move(m.getX(), m.getY());
            if(auxBoard != null) {
                validBoards.add(new NeighBoard(auxBoard, m));
            }
        }
        return validBoards;
    }

    //We return a Board because we want to make a stack of Boards for the undo
    public Board move(int x, int y) {
        //I create a copy of the current board to work with
        Board auxBoard = new Board(this);

        if(!auxBoard.attemptMove(x, y)) return null;

        //If the move was done then we change color of board, and we update possibleMoves
        auxBoard.changeColor();
        auxBoard.updatePossible(x, y);
        return auxBoard;
    }

    //It tries to do the move, if something is changed then the move is valid
    private boolean attemptMove(int x, int y) {
        //If x and y are not within bounds then invalid move
        if (!inBounds(x, y)) return false;
        //If the cell is full then invalid move
        if (cells[x][y] != Chip.EMPTY) return false;
        //Now i have to check if any flip will be made, else its an invalid move
        //I check all 8 directions looking for another chip of the same color as currentColor and chips of different color in-between.
        boolean validMove = false;
        for (int i = -1; i <= 1 ; i++) {
            for (int j = -1; j <= 1 ; j++) {
                //Inside a direction, we want to check the same direction until currentColor or end of board or empty space
                int step = 1;
                while(inBounds(x + step * i, y + step * j) && cells[x + step * i][y + step * j] != currentColor && cells[x + step * i][y + step * j] != Chip.EMPTY) {
                    //Inside mean we are going through chips of the contrary color
                    step++;
                }
                //At this point we know that if step = 1 then we didnt flipped anything, we encountered an empty space or
                //a chip of our same color or the end of the board. If step > 1 then we need to see if our final stop was
                //a chip of our same color (in which case we got a valid move) or and empty space or EOB (invalid move)
                if (step > 1 && inBounds(x + step * i, y + step * j) && cells[x + step * i][y + step * j] == currentColor) {
                    //Now lets flip all the chips we encountered (k starts at 0 because we need to place the chip we tried to place)
                    for (int k = 0; k < step ; k++) {
                        cells[x + k * i][y + k * j] = currentColor;
                    }
                    //Valid move
                    validMove = true;
                }
            }
        }
        //If it managed to do a flip in some direction then validMove should be true, else it stays false.
        return validMove;
    }

    //This is just made to save some code
    private boolean inBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < size) && (y < size));
    }

    //Returns the color of the player about to make a move
    public Chip getCurrentColor() {
        return currentColor;
    }

    //Returns the cells so the GUI can use it
    public Chip[][] getCells() { return cells; }

    //Return the size of the board
    public int getSize() { return size; }

    //fixme this is just to check
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Chip[] col : cells) {
            for(Chip c : col) {
                if(c != Chip.EMPTY) {
                    ret.append(c);
                    ret.append("|");
                } else {
                    ret.append("     |");
                }
            }
            ret.append("\n");
        }
        return ret.toString();
    }
}
