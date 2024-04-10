package pl.gof;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator {

    public void doStep(GameOfLifeBoard board) {
        int width = board.getWidth();
        int height = board.getHeight();
        boolean[][] newBoard = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newBoard[i][j] = board.getBoard().get(i).get(j).nextState();
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.getBoard().get(i).get(j).updateState(newBoard[i][j]);
            }
        }
    }
}