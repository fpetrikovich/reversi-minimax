public class Move implements Comparable<Move> {
    private Integer x;
    private Integer y;

    Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Integer getX() {
        return this.x;
    }

    Integer getY() {
        return this.y;
    }

    @Override
    public int compareTo(Move o) {
        //I compare the X
        int aux = x.compareTo(o.getX());
        //If they are equal i compare the Y
        if(aux == 0) {
            return y.compareTo(o.getY());
        } else {
            //Otherwise i return the first comparison
            return aux;
        }
    }
}
