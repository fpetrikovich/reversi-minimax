import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GUI {
    //    UI ELEMENTS
    private JFrame frame;
    private JPanel combined, board, buttons;
    private Game game;
    private int frameWidth;
    private int frameHeight;
    private int boardSize;
    private int cellSize;
    private int chipSize;
    private Chip[][] currentBoard;
    private BoardCell[][] boardCells;

    public static void main(String[] args){
        GUI example = new GUI(6, 10,100, 2);
        example.init();
    }

    GUI(int boardSize, int maxDepth, long maxTime, int ai){
        this.game = new Game(boardSize, maxDepth, maxTime, ai);
        this.boardSize = boardSize;
        this.boardCells = new BoardCell[boardSize][boardSize];

        //Calculating the size of the boardCell, the chip, and the frame width and height depending on the given size
        if(boardSize <= 6){
            cellSize = 105;
        }
        else if (boardSize <= 8){
            cellSize = 70;
        }
        else {
            cellSize = 50;
        }
        chipSize = cellSize - (cellSize / 8);
        frameWidth = cellSize * boardSize + 5 * boardSize + 5;
        frameHeight = frameWidth + 60;
    }

    public void init(){
        createFrame();
        createBoard();
        setButtons();
        frame.setVisible(true);

    }

    public void createFrame(){
        frame = new JFrame("Reversi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        combined = new JPanel();
        combined.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void createBoard(){
        board = new JPanel();
        board.setBackground(Color.GRAY);
        board.setSize(boardSize*cellSize,boardSize*cellSize);
        GridLayout layout = new GridLayout(0,boardSize);
        layout.setHgap(5);
        layout.setVgap(5);

        board.setLayout(layout);

        for(int row = 0 ; row < boardSize; row++){
            for(int col = 0 ; col < boardSize; col++){
                BoardCell cell = new BoardCell(row,col);
                boardCells[row][col] = cell;
                board.add(cell);
            }
        }
        combined.add(board);
        frame.add(combined);
    }

    public void setButtons() {
        buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        //new GridLayout(1,5, frameWidth/4 - 4 * 40, 20)


        // COMPUTER TURN BUTTON
        JButton computerBtn = new JButton("Play Computer");
        computerBtn.addActionListener(e -> {
            game.play(0,0);
            refreshBoard();
        });

        // UNDO BUTTON
        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(e -> {
            game.undo();
            refreshBoard();
        });

        // PASS BUTTON
        JButton passBtn = new JButton("Pass");
        undoBtn.addActionListener(e -> {
            if(game.getCurrentPlayer() != Role.AI && game.getMoves().isEmpty()){
                game.pass();
            }
        });

        // RESTART BUTTON
        JButton restartBtn = new JButton("Restart");
        restartBtn.addActionListener(e -> {
            game.restart();
            refreshBoard();
        });

        // SAVE BUTTON
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            game.save();
        });

        //Adding buttons to the button panel
        buttons.add(computerBtn);
        buttons.add(undoBtn);
        buttons.add(passBtn);
        buttons.add(restartBtn);
        buttons.add(saveBtn);

        combined.add(buttons);
    }

    private void refreshBoard(){
        for(int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                boardCells[row][col].repaint();
            }
        }
    }

    class BoardCell extends JComponent {
        Graphics2D graphics2D;
        int row, col;

        public BoardCell(int row, int col) {
            this.row = row;
            this.col = col;

            setPreferredSize(new Dimension(cellSize, cellSize));

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if(game.getCurrentPlayer() != Role.AI) {
                        game.play(row, col);
                        refreshBoard();
                    }
                }
            });
        }
        @Override
        public void paintComponent(Graphics g) {
            Chip currentPlayer = game.getCurrentColor();
            List<Move> validCells = game.getMoves();
            currentBoard = game.getCurrentBoard();

            graphics2D = (Graphics2D) g;
            Rectangle cell = new Rectangle(0,0, cellSize, cellSize);
            graphics2D.draw(cell);
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fill(cell);

            // If the cell is a valid position paint it green
            for (Move coordinate : validCells){
                if(coordinate.getX() == this.row && coordinate.getY() == this.col){
                    graphics2D.draw(cell);
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.fill(cell);
                    return;
                }
            }
            // if the Black player played put a black chip
            if (currentBoard[this.row][this.col] == Chip.BLACK) {
                Ellipse2D.Double chip = new Ellipse2D.Double((cellSize - chipSize)/2,(cellSize - chipSize)/2, chipSize, chipSize);
                graphics2D.setColor(Color.BLACK);
                graphics2D.fill(chip);
                graphics2D.draw(chip);
            }
            // if the White player played put a white chip
            else if (currentBoard[this.row][this.col] == Chip.WHITE) {
                Ellipse2D.Double chip = new Ellipse2D.Double((cellSize - chipSize)/2,(cellSize - chipSize)/2, chipSize, chipSize);
                graphics2D.setColor(Color.WHITE);
                graphics2D.fill(chip);
                graphics2D.draw(chip);
            }
        }
    }
}