import java.util.List;
import java.util.Random;

public class Computer {
    private int mode;
    private int depth;
    private long seconds;
    private boolean prune;

    public Computer(int mode, int depth, long seconds, boolean prune) {
        this.mode = mode;
        this.depth = depth;
        this.seconds = seconds;
        this.prune = prune;
    }

    public Board move(Board currentBoard) {
        List<NeighBoard> possibleMoves = currentBoard.getNeigh();
        return possibleMoves.get(0).getBoard();
    }
}
