import java.util.*;

public class Game {
    private Board currentBoard;
    private Deque<Board> boardStack = new LinkedList<>();
    private Map<Chip, Role> players = new EnumMap<Chip, Role>(Chip.class);
    private Computer computer;

    public Game(int size, int maxDepth, long maxTime, int ai) {
        this.currentBoard = new Board(size);
        this.computer = new Computer(0,0,0,false);
        this.players.put(Chip.WHITE, Role.HUMAN);
        this.players.put(Chip.BLACK, Role.AI);
    }

    //Returns a list of all the possible moves that can be done on this turn
    public List<Move> getMoves() {
        List<Move> ret = new LinkedList<>();
        List<NeighBoard> neighs = this.currentBoard.getNeigh();
        for (NeighBoard n: neighs) {
            ret.add(n.getMove());
        }
        return ret;
    }

    //Makes a play, parameters are ignored if AI turn, so pass whatever.
    public boolean play(int x, int y) {
       Role currentPlayer = players.get(currentBoard.getCurrentColor());
       if (currentPlayer == Role.AI) {
           return moveAI();
       } else {
           return movePlayer(x, y);
       }
    }

    //Makes a move on currentBoard based on x and y
    public boolean movePlayer(int x, int y) {
        //We try to make the move
        Board nextBoard = currentBoard.move(x, y);
        if (nextBoard == null) return false;

        //If we got a board back then the move was valid, we save previous board and update current
        this.boardStack.push(currentBoard);
        currentBoard = nextBoard;

        //todo remove this line
        print();
        return true;
    }

    //Makes a move on currentBoard based on AI
    public boolean moveAI() {
        //We try to make the move
        Board nextBoard = computer.move(currentBoard);
        if (nextBoard == null) {
            //We skip computers turn
            currentBoard.changeColor();
            return false;
        }
        //If we got a board back then the AI made a move, we just update current we dont need to save AI move
        currentBoard = nextBoard;

        //todo remove this line
        print();
        return true;
    }

    public Chip getCurrentColor() {
        return currentBoard.getCurrentColor();
    }

    public Chip[][] getCurrentBoard() {
        return this.currentBoard.getCells();
    }

    public void save() {

    }

    //Undo last human player move, AI moves are not saved
    public void undo() {
        this.currentBoard = boardStack.pop();
        //fixme delete this line
        print();
    }

    //Restarts board
    public void restart() {
        boardStack.clear();
        this.currentBoard = new Board(currentBoard.getSize());
    }

    //fixme this is just to check
    private void print() {
        System.out.println(currentBoard.toString());
    }
}
