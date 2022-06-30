public class Test {
    public static void main(String[] args) {
        Game newGame = new Game(6,0,0,0);
        newGame.play(4,2);
        newGame.play(4,1);
        newGame.play(1,2);
        newGame.play(5,2);
        newGame.undo();
        newGame.play(1,2);
        //newGame.play(4,3);
    }
}
