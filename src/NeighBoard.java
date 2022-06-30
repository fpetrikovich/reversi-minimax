public class NeighBoard {
    private Board board;
    private Move move;

    NeighBoard(Board board, Move move) {
        this.board = board;
        this.move = move;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }
}
