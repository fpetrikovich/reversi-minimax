import java.util.List;
import java.util.LinkedList;

public class BoardTree {

    //Positive and Negative infinite as magic numbers
    private static final int PINFINITE = 255;
    private static final int NINFINITE = -255;

    private class BoardNode{
        Board board;
        int score;
        List<BoardNode> children;
        boolean select;
        boolean pruned;

        BoardNode(Board myBoard, int score){
            this.board = myBoard;
            this.score = score;
            this.children = new LinkedList<>();
            this.select = false;
            this.pruned = false;
        }

        void addChild(Board child, int score){
            children.add(new BoardNode(child, score));
        }
        void addChild(BoardNode child){
            children.add(child);
        }

    }

    BoardNode header;
    boolean prune;
    long maxTime;
    int maxDepth;
    boolean timeBased;


    //Constructores, the basic one is private and the other are for depth & time
    private BoardTree(Board board, boolean prune){
        this.header = new BoardNode(board,board.score());
        this.prune = prune;
    }
    BoardTree(Board board, int MaxDepth, boolean prune){
        this(board, prune);
        this.maxDepth = MaxDepth;
        this.timeBased = false;
    }
    BoardTree(Board board, long MaxTime, boolean prune){
        this(board, prune);
        this.maxTime = MaxTime;
        this.timeBased = true;
    }


    public Board minimax(){
        if(header == null)
            throw new NullPointerException();

        BoardNode best = header;

        if(timeBased) {
            if (prune){
                best = minimaxRec(header,0,NINFINITE,PINFINITE);
            }else{
                best = minimaxRec(header,0);
            }
        };//fixme with time
        else{
        };//fixme with depth

        return best.board;

    }


    //ambos algoritmos minimax devuelven un BoardNode con la mejor jugada, si uno quiere board le pide board y si quiere el score pide score

    //no prune minimax, el tree se crea en el momento
    private BoardNode minimaxRec(BoardNode node, int currentDepth){
        BoardNode best = new BoardNode(node.board,0);
        BoardNode value;
        BoardNode aux;

        if (this.maxDepth == currentDepth)
            return node;

        if(node.board.getColor() == Chip.BLACK){
            best.score = NINFINITE;

            for(NeighBoard child : node.board.getNeigh()){
                aux = new BoardNode(child.getBoard(),child.getBoard().score());
                value = minimaxRec(aux,currentDepth+1);
                node.addChild(aux);
                if(value.score >= best.score){
                    best = value;
                }
            }
        }else if(node.board.getColor() == Chip.WHITE){
            best.score = PINFINITE;

            for(NeighBoard child : node.board.getNeigh()){
                aux = new BoardNode(child.getBoard(),child.getBoard().score());
                value = minimaxRec(aux,currentDepth+1);
                if(value.score <= best.score){
                    best = value;
                }
            }
        }else
            throw new IllegalStateException();

        best.select = true;
        return best;
    }

    //alpha-beta prune minimax, the tree is generated in each step
    private BoardNode minimaxRec(BoardNode node, int currentDepth, int alpha, int beta){
        BoardNode best = new BoardNode(node.board,0);
        BoardNode value;
        BoardNode auxNode;
        int auxLimit;

        if (this.maxDepth == currentDepth)
            return node;


        if(node.board.getColor() == Chip.BLACK){
            best.score = NINFINITE;
            auxLimit = alpha;

            for(NeighBoard child : node.board.getNeigh()){

                auxNode = new BoardNode(child.getBoard(),child.getBoard().score());
                value = minimaxRec(auxNode, currentDepth+1, auxLimit, beta);

                if(value.score <= best.score){
                    best = value;
                }

                //refreshes the alpha value
                auxLimit = Integer.max(alpha,best.score);

                if(beta <= auxLimit){
                    node.pruned = true;
                    return best;
                }
            }
        }else if(node.board.getColor() == Chip.WHITE){
            best.score = PINFINITE;
            auxLimit = beta;

            for(NeighBoard child : node.board.getNeigh()){
                auxNode = new BoardNode(child.getBoard(),child.getBoard().score());
                value = minimaxRec(auxNode, currentDepth+1, alpha, auxLimit);

                if(value.score >= best.score){
                    best = value;
                }

                //refreshes the beta value
                auxLimit = Integer.min(best.score,beta);

                if(auxLimit <= alpha){
                    node.pruned = true;
                    return best;
                }
            }
        }else
            throw new IllegalStateException();

        best.select = true;
        return best;
    }
}
